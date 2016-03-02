package edu.hit.ehealth.main.crawler.hospital;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.hospital.HospitalListDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.hospital.HospitalList;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class HospitalListCrawler extends Crawler {

    private static String startUrl = "http://www.haodf.com/yiyuan/beijing/list.htm";
    private boolean needCollectProvince = false;
    private boolean needCrawlProvince = false;

    private List<String> provinceList = new ArrayList<String>() {{
        add(startUrl);
    }}; //31 all

    private static int listCount = -1;

    private HospitalListDao listDao =
            GlobalApplicationContext.getContext().getBean(HospitalListDao.class);
    private HospitalList hospitalList = new HospitalList();
    private String province;

    public HospitalListCrawler(Async async) {
        super(async);
    }


    @Override
    protected void parseContent(BufferedReader content) throws Exception {


        for (String s : provinceList) {
            System.out.println("province = " + s);
        }

        String currentUrl = trackPageUrl();
        if (currentUrl.equals(startUrl) && provinceList.size() < 31) {
            needCollectProvince = true;
        }

        String line = null;
        StringBuilder infoBlock = new StringBuilder();
        boolean isBlock = false;
        while ((line = content.readLine()) != null) {

            if (needCollectProvince && line.contains("<div class=\"kstl\">")) {
                needCrawlProvince = true;
            }

            if (needCrawlProvince) {
                if (line.contains("<a href=\"http://www.haodf.com/yiyuan/")
                        && !line.contains("</a></li>")
                        && line.contains("list.htm")) {
                    collectProvince(line);
                }
            }

            if (line.contains(currentUrl)) {
                province = RegexUtils.regexFind("htm\">(.+)</a>", line);
            }

            if (line.contains("target=\"_blank\"")
                    && line.contains("href=\"/hospital")
                    && line.contains("htm")) {
                infoBlock.append(line);
                isBlock = true;
            } else if (isBlock) {
                infoBlock.append(line + "\n");
            }
            if (isBlock && line.contains("</li>")) {
                isBlock = false;
                infoBlock.append(line);
                extractInfoBlock(infoBlock.toString());
                infoBlock.setLength(0);
            }
        }
        needCollectProvince = false;
        needCrawlProvince = false;
        listCount++;
        if (listCount < provinceList.size()) {
            System.out.println("listCount = " + listCount);
            System.out.println("provinceList = " + provinceList.size());
            String nextUrl = provinceList.get(listCount);
            crawl(nextUrl);
        }

    }

    private void collectProvince(String line) {
        String href = RegexUtils.regexFind("href=\"(\\S+)\"", line);
        if (Utils.SHOULD_PRT) {
            System.out.println("href = " + href);
        }
        if (!href.equals(startUrl)) {
            provinceList.add(href);
        }
    }

    private void extractInfoBlock(String s) {
        System.out.println("s = " + s);
        for (String line : s.split("\n")) {
            if (line.contains("href=\"/hospital/")) {
                String pk = RegexUtils.regexFind("hospital/(\\S+).htm", line);
                hospitalList.setPrimaryId(pk);
                hospitalList.setCrawlDate(Utils.getCurrentDate());
                hospitalList.setCrawlPageUrl(trackPageUrl());
            }
            if (line.contains("target=\"_blank")) {
                String name = RegexUtils.regexFind("_blank\">(.+)", line);
                hospitalList.setHospitalName(name);
            }
            if (line.contains("span")) {
                hospitalList.setSp(line);
            }
        }
        try {
            hospitalList.setProvince(province);
            listDao.save(hospitalList);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static void run() {

        new HospitalListCrawler(Resource.obtainAsync()).crawl(startUrl);
    }

    public static void main(String[] args) {
        run();
    }
}
