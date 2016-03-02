package edu.hit.ehealth.main.crawler.patient;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.patient.ThankLetterDao;
import edu.hit.ehealth.main.exceptions.RegexException;
import edu.hit.ehealth.main.util.Counter;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.patient.ThankLetter;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ThankLetterCrawler extends Crawler {

    private int pageNum;
    protected int pageCount = 1;

    private static ThankLetterDao letterDao =
            GlobalApplicationContext.getContext().getBean(ThankLetterDao.class);
    private ThankLetter thankLetter = new ThankLetter();


    public ThankLetterCrawler(Async async) {
        super(async);
    }

    public static void run() {
        List<String> allLetterUrl = new ArrayList<String>();
        for (String infoCenter : Resource.sAllInfoCenter) {
            String letterUrl = RegexUtils.regexReplace("(.htm)", infoCenter, "/ganxiexin/1.htm");
            if (Utils.SHOULD_PRT) System.out.println("letterUrl = " + letterUrl);
            allLetterUrl.add(letterUrl);
        }

        //GXX
        ThankLetterCrawler thankLetterCrawler = new ThankLetterCrawler(Resource.obtainAsync());
        Counter counter = Counter.newInstance();
        for (String letter : allLetterUrl) {
            counter.printCrawlCountAboutInfoCenter(ThankLetterCrawler.class);
            thankLetterCrawler.crawl(letter);
        }


    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        String line = null;
        String currentUrl = trackPageUrl();
        String nextPageUrlPat = currentUrl.substring(0, currentUrl.lastIndexOf("/"));
        extractDocInfoID(currentUrl);
        boolean isMainBlock = false;
        StringBuilder mainBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
            if (line.contains("<td class=\"dlemd\">")) {
                isMainBlock = true;
            }
            if (isMainBlock) {
                mainBlock.append(line + "\n");
            }
            if (line.contains("<td class=\"midrt\"></td>")) {
                isMainBlock = false;
                extractMainBlock(mainBlock.toString());
                try {
                    letterDao.save(thankLetter);
                } catch (Exception ignored) {

                }
                mainBlock.setLength(0);
            }
            if (line.contains("共&nbsp;")) {
                extractPageNum(line);
            }
        }
        pageCount++;
        if (pageCount <= pageNum) {
            String nextPageUrl = nextPageUrlPat + "/" + pageCount + ".htm";
            crawl(nextPageUrl);
        }
    }

    private void extractPageNum(String line) {
        pageNum = Integer.valueOf(RegexUtils.regexFind("共&nbsp;(\\S+)&nbsp;页", line));
    }

    private void extractMainBlock(String mainBlock) throws ParseException {
        if (Utils.SHOULD_PRT) System.out.println("mainBlock = " + mainBlock);
        boolean isContent = false;
        StringBuilder letterContent = new StringBuilder();

        thankLetter.setPrimaryId(UUID.randomUUID().toString());
        thankLetter.setCrawlDate(Utils.getCurrentDate());
        thankLetter.setCrawlPageUrl(trackPageUrl());

        for (String line : mainBlock.split("\n")) {
            if (line.contains("class=\"gray\"") && line.contains("来自")) {
                String city = RegexUtils.regexFind("来自(.+)\\)", line);
                if (Utils.SHOULD_PRT) System.out.println("city = " + city);
                thankLetter.setFromCity(city);
            }
            if (line.contains("<span>疾病")) {
                String disease = "";
                try {
                    disease = RegexUtils.regexFind("none;\">(.+)</a>", line); //two different type of page style
                } catch (RegexException re) {
                    disease = RegexUtils.regexFind("</span>(.+)</td>", line);
                }
                if (Utils.SHOULD_PRT) System.out.println("disease = " + disease);
                thankLetter.setDisease(disease);
            }
            if (line.contains("<td width=\"34%\" class=\"gray\">时间")) {
//                String postDate = Utils.regexFind("时间：(.+)\\s*</td>", line);
//                if (Utils.SHOULD_PRT) System.out.println("postDate = " + postDate);
                thankLetter.setRecordDate(Utils.noWayParseDateText(line));
            }
            if (line.contains("width=\"34%\">疗效")) {
                String effect = RegexUtils.regexFind("class=\"orange\">(\\S+)</span>", line);
                if (Utils.SHOULD_PRT) System.out.println("effect = " + effect);
                thankLetter.setEffect(effect);
            }
            if (line.contains("width=\"34%\">态度")) {
                String attitude = RegexUtils.regexFind("class=\"orange\">(\\S+)</span>", line);
                if (Utils.SHOULD_PRT) System.out.println("attitude = " + attitude);
                thankLetter.setAttitude(attitude);
            }
            if (line.contains("class=\"spacejy\">")) {
                isContent = true;
            } else if (isContent) {
                letterContent.append(line);
            }
            if (isContent && line.contains("</td>")) {
                isContent = false;
                extractLetterContent(letterContent.toString());
            }
            if (line.contains("推荐)</span>")) {
                String recommandNum = RegexUtils.regexFind("(\\d*)人推荐", line);
                if (Utils.SHOULD_PRT) System.out.println("recommandNum = " + recommandNum);
                thankLetter.setRecommendNum(recommandNum);
            }
            if (line.contains("个回应")) {
                String replyNum = RegexUtils.regexFind("(\\d*)个回应", line);
                if (Utils.SHOULD_PRT) System.out.println("replyNum = " + replyNum);
                thankLetter.setReplyNum(replyNum);
            }
        }
    }

    private void extractLetterContent(String s) {
        if (Utils.SHOULD_PRT) System.out.println("s = " + s);
        thankLetter.setContent(s);
    }

    private void extractDocInfoID(String currentUrl) {
        String docID = RegexUtils.regexFind("doctor/(\\S+)/gan", currentUrl);
        if (Utils.SHOULD_PRT) System.out.println("docID = " + docID);
        thankLetter.setPersonInfoID(docID);
    }


    public static void main(String[] args) {

        ThankLetterCrawler c = new ThankLetterCrawler(Resource.obtainAsync());
        c.crawl("http://www.haodf.com/doctor/DE4r08xQdKSLBT0wXYSdpUn-8HdJ/ganxiexin/1.htm");
        run();
    }
}
