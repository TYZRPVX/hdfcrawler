package edu.hit.ehealth.main.crawler.patient;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.patient.PatientClubDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Counter;
import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.patient.PatientClub;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PatientClubCrawler extends Crawler {

    public static HashSet<String> patientClubSet = new HashSet<String>();
    protected PatientClub patientClub;
    private static List<String> clubThreadList = new ArrayList<String>();
    private static PatientClubDao dao = null;
    protected int pageCount = 1;

    static {
        dao = GlobalApplicationContext.getContext().getBean(PatientClubDao.class);
        patientClubSet.add("http://zhaoquanming.haodf.com/huanyouhui/index.htm");

    }

    private int pageNum;

    public PatientClubCrawler(Async async) {
        super(async);
    }

    public static void run() {
        // hyhlb and crawl thread

        List<String> allDoctorHomepage = Utils.readObjList(TextValue.Path.doctorHomepages);
        List<String> allClubUrl = new ArrayList<String>();
        for (String homepage : allDoctorHomepage) {
            String clubUrl = homepage + "huanyouhui/index.htm";
            allClubUrl.add(clubUrl);
        }
        PatientClubCrawler clubCrawler = new PatientClubCrawler(Resource.obtainAsync());
        Counter counter = Counter.newInstance();
        for (String club : allClubUrl) {
            counter.printCrawlCountAboutHomepage(PatientClubCrawler.class);
            clubCrawler.crawl(club); // HYHWB，每当解析到一个thread,就立刻开启thread爬虫,避免中间存储
        }

    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        patientClub = new PatientClub();
        patientClub.setCrawlPageUrl(trackPageUrl());
        String homeID = RegexUtils.regexFind("http://(\\S+).hao", trackPageUrl());
        patientClub.setHomePageID(homeID);
        if (Utils.SHOULD_PRT) System.out.println("trackPageUrl() = " + trackPageUrl());

        String line = null;
        boolean isTopic = false;

        StringBuffer topicContent = new StringBuffer();
        while ((line = content.readLine()) != null) {

            if (line.contains("<td title=\"话题")) {
                isTopic = true;
            }
            if (isTopic) {
                topicContent.append(line + "\n");
            }
            if (isTopic && line.contains("<td class=\"gray3\">")) {
                isTopic = false;
                extractEachTopic(topicContent.toString());
                patientClub.setCrawlDate(Utils.getCurrentDate());
                dao.save(patientClub);
                topicContent.setLength(0);
            }
            if (line.contains("共&nbsp")) {
                extractPageNum(line);// FIXME: 2016/2/14 JPA
            }
        }
        //handle next page
        pageCount++;
        String pk = RegexUtils.regexFind("http://(\\S+).haodf", trackPageUrl());
        String nextPageUrlPat = "http://" + pk
                + ".haodf.com/patient/index?role=browser&type=all&p=";
        String nextPageUrl = nextPageUrlPat + pageCount;
        if (pageCount <= pageNum) {
            if (Utils.SHOULD_PRT) System.out.println("nextPageUrl = " + nextPageUrl);
            crawl(nextPageUrl);
        }

    }

    private void extractPageNum(String line) {
        String pageNumStr = RegexUtils.regexFind("共&nbsp;(\\d*)&nbsp;页", line);
        pageNum = Integer.parseInt(pageNumStr);
        if (Utils.SHOULD_PRT) System.out.println("pageNum = " + pageNum);
    }

    private void extractEachTopic(String topic) throws ParseException {
        if (Utils.SHOULD_PRT) System.out.println("topic = " + topic);
        String[] lines = topic.split("\n");
        for (String line : lines) {
            if (line.contains("thread")) {
                extractThread(line);
            }
            if (line.contains("title")) {
                extractTitle(line);
            }
            if (line.contains("<td><a href")) {
                extractGroup(line);
            }
        }
        String author = RegexUtils.regexFind("<td>\\s*(\\S+)\\s*</td>", topic);
        if (Utils.SHOULD_PRT) System.out.println("author = " + author);
        patientClub.setAuthorName(author);
        String replyNum = RegexUtils.regexFind(".+<td>(\\d*)</td>", topic);
        if (Utils.SHOULD_PRT) System.out.println("replyNum = " + replyNum);
        patientClub.setReplyNum(Integer.valueOf(replyNum));
//        String replyDate = Utils.regexFind("\"gray3\">(.+)</td>", topic);
//        if (Utils.SHOULD_PRT) System.out.println("replyDate = " + replyDate);
        patientClub.setLastReplyDate(Utils.noWayParseDateText(topic));
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void extractThread(String line) {
        String threadUrl = RegexUtils.regexFind("href=\"(\\S+)\">", line);
        if (Utils.SHOULD_PRT) System.out.println("crawlUrl = " + threadUrl);
        patientClub.setCrawlPageUrl(threadUrl);
        String pk = RegexUtils.regexFind("thread/(\\S+).htm", line);
        if (Utils.SHOULD_PRT) System.out.println("pk = " + pk);
        patientClub.setPrimaryId(pk);

        /**
         * go into <code>clubThreadCrawler</code>
         */
        ClubThreadCrawler threadCrawler = new ClubThreadCrawler(Resource.obtainAsync());
        if (Utils.SHOULD_PRT) System.out.println("go into clubThreadCrawler " + threadUrl);
        threadCrawler.crawl(threadUrl);
    }


    private void extractGroup(String line) {
        String group = RegexUtils.regexFind("<td><a href.+\">(.+)</a>", line);
        if (Utils.SHOULD_PRT) System.out.println("group = " + group);
        patientClub.setGroupName(group);
    }

    private void extractTitle(String line) {
        String title = RegexUtils.regexFind("title=\"(.+)\">", line);
        if (Utils.SHOULD_PRT) System.out.println("title = " + title);
        patientClub.setTitle(title);
    }

    public static void main(String[] args) {
        PatientClubCrawler c = new PatientClubCrawler(Resource.obtainAsync());
        for (String url : patientClubSet) {
            c.crawl(url);
        }

    }
}
