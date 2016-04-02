package edu.hit.ehealth.main.crawler.patient;
/*修复了最后发表有乱码的问题*/
import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.patient.PatientServiceAreaDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Counter;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.patient.PatientServiceArea;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PatientServiceAreaCrawler extends Crawler {


    private int pageNum;
    protected int pageCount = 1;
    private PatientServiceArea patientServiceArea = new PatientServiceArea();
    private static PatientServiceAreaDao areaDao =
            GlobalApplicationContext.getContext().getBean(PatientServiceAreaDao.class);


    public PatientServiceAreaCrawler(Async async) {
        super(async);
    }

    public static void run() {
        //H_HZFWQLB
        List<String> allAreaUrl = new ArrayList<String>();

        for (String homepage : Resource.sAllDoctorHomepage) {
            String areaUrl = homepage + "zixun/list.htm";
            allAreaUrl.add(areaUrl);
        }
        PatientServiceAreaCrawler areaCrawler = new PatientServiceAreaCrawler(Resource.obtainAsync());
        Counter counter = Counter.newInstance();
        for (String area : allAreaUrl) {
            counter.printCrawlCountAboutHomepage(PatientServiceAreaCrawler.class);
            areaCrawler.crawl(area); //HZFWQWB
        }

    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        String line = null;
        boolean isMainBlock = false;
        String nextPagePat = "http://zhaoquanming.haodf.com/zixun/list.htm?type=&p=";
        StringBuilder mainBlock = new StringBuilder();
        String currentUrl = trackPageUrl();

        extractHomepageID(currentUrl);
        while ((line = content.readLine()) != null) {
            if (line.contains("class=\"td_link\"")) {
                isMainBlock = true;
            }
            if (isMainBlock) {
                mainBlock.append(line + "\n");
            }
            if (line.contains("<td></td>")) {
                isMainBlock = false;
                extractMainBlock(mainBlock.toString());
                patientServiceArea.setCrawlPageUrl(trackPageUrl());
                patientServiceArea.setCrawlDate(Utils.getCurrentDate());
                try {
                    areaDao.save(patientServiceArea);
                } catch (Exception e) {
                    if (Utils.SHOULD_PRT) System.out.println("e.getMessage() = " + e.getMessage());
                }
                mainBlock.setLength(0);
            }
            if (line.contains("共&nbsp")) {
//                if (Utils.SHOULD_PRT) System.out.println("line = " + line);
                extractPageNum(line);
            }
        }
        pageCount++;
        if (pageCount < pageNum) {
            String nextPageUrl = nextPagePat + pageCount;
            crawl(nextPageUrl);
        }
    }

    private void extractHomepageID(String currentUrl) {
        String homeID = RegexUtils.regexFind("http://(\\S+).hao", currentUrl);
        patientServiceArea.setHomePageID(homeID);
    }

    private void extractPageNum(String line) {
        String pageNumStr = RegexUtils.regexFind("共&nbsp;(\\d*)&nbsp;页", line);
        pageNum = Integer.parseInt(pageNumStr);
        if (Utils.SHOULD_PRT) System.out.println("pageNum = " + pageNum);
    }


    private void extractMainBlock(String block) throws Exception {
        if (Utils.SHOULD_PRT) System.out.println("block = " + block);
        StringBuilder tagsBuilder = new StringBuilder();
        String[] lines = block.split("\\r?\\n");
        for (String line : lines) {
            if (line.contains("class=\"td_link\" rel=\"nofollow\"")
                    || line.contains("class=\"rela_dis\" rel=\"nofollow\"")) {
                continue;
            }
            if (line.contains("class=\"td_link\"")) {
                extractPrimaryID(line);
                extractTitle(line);
            }
            if (line.contains("class=\"gray3\"")) {
                extractLastDate(line);
            }
            if (line.contains("green3 pl5 pr5\">")) {
                extractTalkTimes(line);
            }
            if (line.contains("class=\"rela_dis\"")) {
                extractRelevantDis(line);
            }
            if (line.contains("http://i1.hdfimg.com/doctorzone/images/zixun_icon2.png")) {
                String money = RegexUtils.regexFind("title=(.*)>", line);
                tagsBuilder.append(money).append(", ");
            }
            if (line.contains("src=\"http://i1.hdfimg.com/doctorzone/images/zixun_icon9")) {
                String gift = RegexUtils.regexFind("title=(.*)>", line);
                tagsBuilder.append(gift).append(", ");
            }
            if (line.contains("src=\"http://i1.hdfimg.com/doctorzone/images/zixun_icon4")) {
                String phone = RegexUtils.regexFind("title=(.*)>", line);
                tagsBuilder.append(phone).append(", ");
            }
            if (line.contains("src=\"http://i1.hdfimg.com/doctorzone/images/zixun_icon5")) {
                String privacy = RegexUtils.regexFind("title=(.*)>", line);
                tagsBuilder.append(privacy).append(", ");
            }
            if (line.contains("src=\"http://i1.hdfimg.com/doctorzone/images/zixun_icon11")) {
                String heart = RegexUtils.regexFind("title=(.*)>", line);
                tagsBuilder.append(heart).append(", ");
            }
        }
        patientServiceArea.setTags(tagsBuilder.toString());
        if (Utils.SHOULD_PRT) System.out.println("tagsBuilder = " + tagsBuilder);
    }

    private void extractRelevantDis(String line) {
        String relevantDis = RegexUtils.regexFind("class=\"rela_dis\"  >(.*)</a>", line);
        if (Utils.SHOULD_PRT) System.out.println("relevantDis = " + relevantDis);
        patientServiceArea.setRelevantDis(relevantDis);
    }

    private void extractTitle(String line) {
        String title = RegexUtils.regexFind("class=\"td_link\"\\s>(.*)</a>", line);
        if (Utils.SHOULD_PRT) System.out.println("title = " + title);
        patientServiceArea.setTitle(title);
    }

    private void extractTalkTimes(String line) {
        String talkTimes = RegexUtils.regexFind("green3 pl5 pr5\">(.*)</font>", line);
        if (Utils.SHOULD_PRT) System.out.println("talkTimes = " + talkTimes);
        String[] dAndPTimes = talkTimes.split("/");
        Integer dTimes = Integer.valueOf(dAndPTimes[0]);
        Integer pTimes = Integer.valueOf(dAndPTimes[1]);
        patientServiceArea.setDoctorTalkNum(dTimes);
        patientServiceArea.setPatientTalkNum(pTimes);
    }

    private void extractLastDate(String line) throws ParseException {
        String dateStr = RegexUtils.regexFind("gray3\">(.+)<br/>", line);
        //String lastDate = Utils.noWayParseDateText(line);
        if (Utils.SHOULD_PRT) System.out.println("lastDate = " + dateStr);
        patientServiceArea.setLastPostDate(dateStr);
    }

    protected void extractPrimaryID(String line) {
        String pk = RegexUtils.regexFind("wenda/(\\S+).htm", line);
        if (Utils.SHOULD_PRT) System.out.println("pk = " + pk);
        patientServiceArea.setPrimaryId(pk);
//        if (Utils.SHOULD_PRT) System.out.println("line = " + line);
        extractAreaTextUrl(line);
    }

    /**
     * HZFWQWB crawl text
     *
     * @param line
     */
    private void extractAreaTextUrl(String line) {
        String areaTextUrl = RegexUtils.regexFind("href=\"(\\S+)\"", line);
        PatientServiceAreaTextCrawler textCrawler = new PatientServiceAreaTextCrawler(Resource.obtainAsync());
        if (Utils.SHOULD_PRT) System.out.println("go into textCrawler " + areaTextUrl);
        textCrawler.crawl(areaTextUrl);
    }

    public static void main(String[] args) {
        PatientServiceAreaCrawler c = new PatientServiceAreaCrawler(Resource.obtainAsync());
        c.crawl("http://judy815.haodf.com/zixun/list.htm");
        
    }
}
