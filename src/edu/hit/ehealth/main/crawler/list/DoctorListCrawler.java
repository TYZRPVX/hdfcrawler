package edu.hit.ehealth.main.crawler.list;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.crawler.basestruct.NextPageTracker;
import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DoctorListCrawler extends Crawler implements Serializable, NextPageTracker {

    public static List<String> allDoctorHomepage = new ArrayList<String>();
    public static List<String> allDoctorInfoCenter = new ArrayList<String>();
    protected int pageCount = 1;
    protected int pageNum;


    public DoctorListCrawler(Async async) {
        super(async);
    }


    public static void main(String[] args) {
        DoctorListCrawler doctorListCrawler = new DoctorListCrawler(Resource.obtainAsync());
    }


    public static void run() {
        // faculty -> doctor's info list and doctor's homepage

        new FacultyListCrawler(Resource.obtainAsync()).crawlAllFaculty();

        List<String> allFacultys = Utils.readObjList(TextValue.Path.facultys);
        DoctorListCrawler crawler = new DoctorListCrawler(Resource.obtainAsync());
        for (String f : allFacultys) {
            crawler.crawl(f); //cost much time
        }
        Utils.writeObjList(allDoctorInfoCenter, TextValue.Path.doctorInfoCenters);
        Utils.writeObjList(allDoctorHomepage, TextValue.Path.doctorHomepages);
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        /**
         * given http://www.haodf.com/faculty/xxx.
         * crawl all info center and homepage
         */
        String line = null;
        String currentUrl = trackPageUrl();
        while ((line = content.readLine()) != null) {
            if (line.contains("<li><a class=\"name\" target=\"_blank")
                    && line.contains("doctor")) {
                extractDoctorUrl(line);
            }
            if (line.contains("共&nbsp")) {
                extractPageNum(line);
            }
            if (line.contains(".haodf.com")
                    && line.contains("访问个人网站")
                    && line.contains("title=")
                    ) {
                extractHomepageUrl(line);
            }

        }
        if (Utils.SHOULD_PRT) {
            System.out.println("allDoctorInfoCenter size: " + allDoctorInfoCenter.size());
            System.out.println("allDoctorHomepage size: " + allDoctorHomepage.size());
        }
        pageCount++;
        if (pageCount <= pageNum) {
            crawl(getNextPageUrl());
        }
    }

    private void extractHomepageUrl(String line) {
        String homepage = RegexUtils.regexFind("href=\"(\\S*)\"", line);
        System.out.println("homepage = " + homepage);
        allDoctorHomepage.add(homepage);
    }

    private void extractDoctorUrl(String line) {
        String url = RegexUtils.regexFind("href=\"(.+)\" title", line);
        if (Utils.SHOULD_PRT) System.out.println("url = " + url);
        allDoctorInfoCenter.add(url);
    }


    @Override
    public int extractPageNum(String line) {
        String pageNumStr = RegexUtils.regexFind("共&nbsp;(\\d+)&nbsp;页", line);
        if (Utils.SHOULD_PRT) System.out.println("pageNum = " + pageNumStr);
        pageNum = Integer.valueOf(pageNumStr);
        return pageNum;
    }

    @Override
    public String getNextPageUrl() {
        String currentUrl = trackPageUrl();
        String nextPageUrl = currentUrl.substring(0,
                currentUrl.lastIndexOf(".htm")) + "/menzhen_" + pageCount + ".htm";
        return nextPageUrl;
    }
}
