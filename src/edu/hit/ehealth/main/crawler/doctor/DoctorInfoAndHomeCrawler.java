package edu.hit.ehealth.main.crawler.doctor;

import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Counter;
import edu.hit.ehealth.main.util.mail.Mailer;
import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 爬取顺序：省份-医院-科室-医生信息-医生主页
 * <p/>
 * 读取所有科室，爬取并序列化所有信息中心、主页地址 从序列化文件中读出所有信息中心、所有主页地址
 */

public class DoctorInfoAndHomeCrawler {

    public static List<String> allDoctorHomepage = new ArrayList<String>();

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        final DoctorInfoAndHomeCrawler doctorInfoAndHomeCrawler = new DoctorInfoAndHomeCrawler();

        Thread homepageThr = new Thread() {
            @Override
            public void run() {
                doctorInfoAndHomeCrawler.crawlAllHomepage(); // ysgrwz
            }
        };
        Thread infoThr = new Thread() {
            @Override
            public void run() {
                doctorInfoAndHomeCrawler.crawlAllInfoCenter(); // xxzxy
            }
        };
        homepageThr.start();
        infoThr.start();

    }


    public void crawlAllInfoCenter() {
        DoctorInfoCenterCrawler doctorInfoCenterCrawler = new DoctorInfoCenterCrawler();
        List<String> allDoctorInfoCenter = Utils
                .readObjList(TextValue.Path.doctorInfoCenters);
        Counter counter = Counter.newInstance();
        for (String ic : allDoctorInfoCenter) {
            counter.printCrawlCountAboutInfoCenter(DoctorInfoCenterCrawler.class);
            if (Utils.SHOULD_PRT) System.out.println("crawling info center of " + ic);
            doctorInfoCenterCrawler.crawl(ic);
        }
    }

    private void crawlAllHomepage() {
        DoctorHomePageCrawler doctorHomePageCrawler = new DoctorHomePageCrawler(
                Resource.obtainAsync());
        List<String> allDoctorHomePage = Utils
                .readObjList(TextValue.Path.doctorHomepages);
        Counter counter = Counter.newInstance();
        for (String page : allDoctorHomePage) {
            counter.printCrawlCountAboutHomepage(DoctorHomePageCrawler.class);
            if (Utils.SHOULD_PRT) System.out.println("crawling homepage of " + page);
            doctorHomePageCrawler.crawl(page);
        }
    }
}
