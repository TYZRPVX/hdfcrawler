package edu.hit.ehealth.main.crawler.list;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.crawler.basestruct.NextPageTracker;
import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class PhoneConsultListCrawler extends Crawler implements NextPageTracker {

    public static final String urlPat
            = "http://400.haodf.com/index/search?diseasename=&province=&facultyname=&hosfaculty=&hospitalname=&nowpage=";
    private static int pageNum;
    private static int pageCount = 1;
    private static List<String> phoneDoctorList = new ArrayList<String>();
    public PhoneConsultListCrawler(Async async) {
        super(async);
    }

    public static void run() {
        PhoneConsultListCrawler c = new PhoneConsultListCrawler(Resource.obtainAsync());
        for (int i = 1; i <= pageNum; i++) {
            c.crawl(urlPat + i);
        }
    }

    public static void main(String[] args) {
        run();
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        String line = null;
        while ((line = content.readLine()) != null) {
            if (line.contains("共&nbsp") && line.contains("&nbsp;页")) {
                extractPageNum(line);
            }
            if (line.contains("target=\"_blank\" class=\"green\"")) {
                String key = RegexUtils.regexFind("href=\"http://400.haodf.com/haodf/(\\S+)\" target=", line).toLowerCase();
//                System.out.println("key = " + key);
                String doctorUrl = "http://" + key + ".haodf.com/";
                phoneDoctorList.add(doctorUrl);
            }
        }
        // can't use recursion, to be cause stackoverflow
        pageCount++;
        if (pageCount > pageNum) {
            Utils.writeObjList(phoneDoctorList, TextValue.Path.phoneDoctors);
        }
    }

    @Override
    public int extractPageNum(String line) {
        String nStr = RegexUtils.regexFind("共&nbsp;(\\d*)&nbsp;页", line);
        pageNum = Integer.valueOf(nStr);
        return pageNum;
    }

    @Override
    public String getNextPageUrl() {
        return null;
    }
}
