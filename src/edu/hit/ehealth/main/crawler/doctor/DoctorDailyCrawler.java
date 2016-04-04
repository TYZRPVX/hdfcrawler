package edu.hit.ehealth.main.crawler.doctor;

import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.Counter;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;

import java.util.List;

public class DoctorDailyCrawler {

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        DoctorHomepageDailyCrawler dailyCrawler = new DoctorHomepageDailyCrawler(Resource.obtainAsync());
        List<String> allHomepages = Utils.readObjList(TextValue.Path.doctorHomepages);
        Counter counter = Counter.newInstance();
        for (String homepage : allHomepages) {
            counter.printCrawlCountAboutHomepage(DoctorDailyCrawler.class);
            dailyCrawler.crawl(homepage);
        }
    }

}
