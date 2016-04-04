package edu.hit.ehealth.main.crawler.patient;
/*修复了发表时间的问题*/

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.patient.ClubThreadDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.patient.ClubThread;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.HashSet;
import java.util.UUID;

public class ClubThreadCrawler extends Crawler {

    public static HashSet<String> clubThreadSet = new HashSet<String>();
    protected ClubThread clubThread;
    public static ClubThreadDao ctdao;

    static {
        ctdao = GlobalApplicationContext.getContext().getBean(ClubThreadDao.class);
        clubThreadSet.add("http://zhaoquanming.haodf.com/huanyouhui/thread/3029112185.htm");

    }


    public ClubThreadCrawler(Async async) {
        super(async);
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        clubThread = new ClubThread();
        String line = null;
        String currentUrl = trackPageUrl();
        extractTopicID(currentUrl);
        extractHomepageID(currentUrl);
        clubThread.setCrawlDate(Utils.getCurrentDate());
        clubThread.setCrawlPageUrl(trackPageUrl());
        boolean isContent = false;
        boolean isPublisher = false;
        boolean isTitle = false;
        StringBuilder contentBlock = new StringBuilder();
        StringBuilder publisherBlock = new StringBuilder();
        StringBuilder titleBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
            if (line.contains("fl\">发表于")) {
                extractPostDate(line);
            }
            //content
            if (line.contains("huanyouhui_topic")) {
                isContent = true;
            } else if (isContent) {
                contentBlock.append(line);
            }
            if (line.contains("</div>")) {
                isContent = false;
            }
            //publisher
            if (line.contains("pb10 bb_d3")) {
                isPublisher = true;
            } else if (isPublisher) {
                publisherBlock.append(line);
            }
            if (line.contains("</div>")) {
                isPublisher = false;
            }
            //topicTitle
            if (line.contains("clearfix hui_talk mb10")) {
                isTitle = true;
            } else if (isTitle) {
                titleBlock.append(line);
            }
            if (line.contains("</div>")) {
                isTitle = false;
            }
        }
        extractContent(contentBlock.toString());
        extractPublisher(publisherBlock.toString());
        extractTopicTitle(titleBlock.toString());
        clubThread.setPrimaryId(UUID.randomUUID().toString());
        ctdao.save(clubThread);
    }

    private void extractTopicTitle(String s) {
        if (Utils.SHOULD_PRT) System.out.println("title = " + s);
        String title = RegexUtils.regexFind("class=\"fl\">(.+)</p>", s);
        clubThread.setTopicTitle(title);
    }

    private void extractPublisher(String s) {
        if (Utils.SHOULD_PRT) System.out.println("publisher = " + s);
        String publisher = RegexUtils.regexFind("<p>\\s*(\\S+)\\s*</div>", s);
        clubThread.setPublisher(publisher);
    }

    private void extractContent(String s) {
        if (Utils.SHOULD_PRT) System.out.println("content = " + s);
        String content = RegexUtils.regexFind("(.+)", s);
        clubThread.setContent(content);
    }

    private void extractPostDate(String line) throws ParseException {

        String dateStr = RegexUtils.regexFind(".+于：(.+)</p>", line);
        if (Utils.SHOULD_PRT) System.out.println("datestr = " + dateStr);
        //String date = Utils.noWayParseDateText(line);
        clubThread.setPostDate(dateStr);

    }

    private void extractHomepageID(String currentUrl) {
        String homepageID = RegexUtils.regexFind("//(\\S+).haodf", currentUrl);
        if (Utils.SHOULD_PRT) System.out.println("homepageID = " + homepageID);
        clubThread.setHomepageID(homepageID);
    }

    private void extractTopicID(String currentUrl) {
        String pk = RegexUtils.regexFind("thread/(\\S+).htm", currentUrl);
        if (Utils.SHOULD_PRT) System.out.println("topic id = " + pk);
        clubThread.setTopicID(pk);
    }

    public static void main(String[] args) {
        ClubThreadCrawler c = new ClubThreadCrawler(Resource.obtainAsync());
        c.crawl("http://zuofuguo.haodf.com/huanyouhui/thread/2745792677.htm");
    }
}
