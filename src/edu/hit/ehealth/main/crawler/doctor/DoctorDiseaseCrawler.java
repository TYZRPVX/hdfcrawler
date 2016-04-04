package edu.hit.ehealth.main.crawler.doctor;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.crawler.basestruct.NextPageTracker;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class DoctorDiseaseCrawler extends Crawler implements NextPageTracker {

    private static Set<String> diseaseSet = new HashSet<String>();

    static {
        diseaseSet.add("http://www.haodf.com/jibing/xiaoerfeiyan.htm");
    }
//    private DoctorDisease doctorDisease;
//    private static DoctorDiseaseDao doctorDiseaseDao
//            = GlobalApplicationContext.getContext().getBean(DoctorDiseaseDao.class);

    protected int pageNum;
    protected int pageCount = 1;

    public DoctorDiseaseCrawler(Async async) {
        super(async);
    }

    private static String constructUrl(String disUrl) {
        String doctorUrl = disUrl.substring(0,
                disUrl.lastIndexOf(".htm")) + "/daifu.htm";
        if (Utils.SHOULD_PRT) System.out.println("doctorUrl = " + doctorUrl);
        return doctorUrl;
    }

    public static void main(String[] args) {
        DoctorDiseaseCrawler c = new DoctorDiseaseCrawler(Resource.obtainAsync());
        for (String disUrl : diseaseSet) {
            String docUrl = constructUrl(disUrl);
            c.crawl(docUrl);
        }
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        String line = null;
        String currentUrl = trackPageUrl();
        extractPrimaryKey(currentUrl);
        while ((line = content.readLine()) != null) {
//            doctorDisease = new DoctorDisease();
            if (line.contains("共<font class=\"black")) {
                extractPageNum(line);
            }
            if (line.contains("class=\"blue_a3\"")) {
                extractDoctorID(line);
            }
        }

        pageCount++;
        if (pageCount <= pageNum) {
            crawl(getNextPageUrl());
        }
    }

    private void extractPrimaryKey(String currentUrl) {
        String disID = RegexUtils.regexFind("jibing/(\\S+).htm", currentUrl);
//        doctorDisease.setDiseaseID(disID);

    }

    private void extractDoctorID(String line) {
        String doctorID = RegexUtils.regexFind("doctor/(\\S+).htm\" class=", line);
        if (Utils.SHOULD_PRT) System.out.println("doctorID = " + doctorID);
//        doctorDisease.setDoctorID(doctorID);
    }

    @Override
    public int extractPageNum(String line) {
        String pageNumStr = RegexUtils.regexFind("共<font class=\"black pl5 pr5\">(\\d*)</font>", line);
        pageNum = Integer.valueOf(pageNumStr);
        return pageNum;
    }

    public String getNextPageUrl() {
        String currentUrl = trackPageUrl();
        String nextPageUrl = currentUrl.substring(0,
                currentUrl.lastIndexOf("daifu")) + "daifu_" + pageCount + ".htm";
        return nextPageUrl;
    }
}
