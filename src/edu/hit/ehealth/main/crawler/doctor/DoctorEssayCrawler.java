package edu.hit.ehealth.main.crawler.doctor;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.crawler.basestruct.NextPageTracker;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.doctor.DoctorEssayDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.doctor.DoctorEssay;
import org.apache.http.client.fluent.Async;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DoctorEssayCrawler extends Crawler implements NextPageTracker {

    public static DoctorEssayDao doctorEssayDao =
            GlobalApplicationContext.getContext().getBean(DoctorEssayDao.class);
    protected int pageCount = 1;
    private int pageNum;
    private DoctorEssay doctorEssay;


    public DoctorEssayCrawler(Async async) {
        super(async);
    }

    public static void run() {
        // WZLB
        List<String> allEssayUrl = new ArrayList<String>();

        for (String homepage : Resource.sAllDoctorHomepage) {
            String essayUrl = homepage + "lanmu";
            allEssayUrl.add(essayUrl);
        }
        DoctorEssayCrawler essayCrawler = new DoctorEssayCrawler(Resource.obtainAsync());
        for (String essay : allEssayUrl) {
            essayCrawler.crawl(essay);
        }

    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        String line = null;
        boolean isMainBlock = false;
        StringBuilder mainBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {

            if (line.contains("<a class=\"pr5 art_cate\"")) {
                isMainBlock = true;
            }
            if (isMainBlock) {
                mainBlock.append(line + "\n");
            }
            if (line.contains("共&nbsp")) {
                extractPageNum(line);
            }
            if (line.contains("<p class=\"art_dot\">")) {
                isMainBlock = false;
                extractMainBlock(mainBlock.toString());
                mainBlock.setLength(0);
            }
        }
        pageCount++;
        if (pageCount <= pageNum) {
            crawl(getNextPageUrl());
        }
    }

    private void extractMainBlock(String s) throws ParseException {
        if (Utils.SHOULD_PRT) System.out.println("s = " + s);
        doctorEssay = new DoctorEssay();
        doctorEssay.setCrawlPageUrl(trackPageUrl());
        String homepageID = RegexUtils.regexFind("http://(\\S+).haodf.com", trackPageUrl());
        if (Utils.SHOULD_PRT)
            System.out.println("homepageID = " + homepageID);
        doctorEssay.setHomepageID(homepageID);
        doctorEssay.setCrawlDate(Utils.getCurrentDate());

        for (String line : s.split("\n")) {
            if (line.contains("nbsp;发表于")) {
                String date = Utils.noWayParseDateText(line);
                doctorEssay.setPostDate(date);
            }
            if (line.contains("</span>人已读")) {
                String readNum = RegexUtils.regexFind("(\\d*)\\s*</span>人已读", line);
                doctorEssay.setReadNum(
                        Integer.valueOf(readNum)
                );
            }
            if (line.contains("class=\"pr5 art_cate")) {
                String essayType = RegexUtils.regexFind("\\[(\\S+)\\]", line);
                doctorEssay.setTypeName(essayType);
            }
            if (line.contains("target=\"_blank\" title=")) {
                String title = RegexUtils.regexFind("title=\"(.+)</a>", line);
                doctorEssay.setTitle(title);
            }
            if (line.contains("<a class=\"art_t\" href=")) {
                String pk = RegexUtils.regexFind("guandian/(.+).htm", line);
                doctorEssay.setPrimaryId(pk);
            }
        }

        try {
            doctorEssayDao.save(doctorEssay);
        } catch (DataIntegrityViolationException dve) {
//            dve.printStackTrace();
            /** 表示这个部分已经被爬取过了，在插入了相同的MD5值的数据时，会产生异常
             *  由于已经被爬取过了，不能重复插入，所以忽略这个异常
             */
        }
    }


    @Override
    public int extractPageNum(String line) {
        String pageNumStr = RegexUtils.regexFind("共&nbsp;(\\S+)&nbsp;页", line);
        pageNum = Integer.valueOf(pageNumStr);
        return pageNum;
    }

    @Override
    public String getNextPageUrl() {
        String currentUrl = trackPageUrl();
        String nextPageUrl = currentUrl.substring(0,
                currentUrl.lastIndexOf("lanmu")) + "lanmu_" + pageCount;
        if (Utils.SHOULD_PRT) System.out.println("nextPageUrl = " + nextPageUrl);
        return nextPageUrl;
    }

    public static void main(String[] args) {
        DoctorEssayCrawler c = new DoctorEssayCrawler(Resource.obtainAsync());
        c.crawl("http://lixueni.haodf.com/lanmu");
        run();
    }
}
