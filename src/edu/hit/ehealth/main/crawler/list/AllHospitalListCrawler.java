package edu.hit.ehealth.main.crawler.list;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import org.apache.http.client.fluent.Async;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 从已知的省份中爬取所有的医院地址，并序列化到文件
 */

public class AllHospitalListCrawler extends Crawler {

    public static List<String> allHospitals = new ArrayList<String>();

    public AllHospitalListCrawler(Async async) {
        super(async);
    }


    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = content.readLine()) != null) {

            if (line.length() == 0) {
                continue;
            }
            sb.append(line);
        }
        String html = sb.toString();
        extractHospital(html);
    }

    private void extractHospital(String html) {
        Document doc = Jsoup.parse(html);
        Elements table = doc.getElementsByClass("jblb");
        for (Element e : table) {
            Elements links = e.getElementsByTag("a");
//            if (Utils.SHOULD_PRT) System.out.println(links);
            extractHospLinks(links);
        }
    }

    private void extractHospLinks(Elements links) {
        for (Element link : links) {
            String href = link.attr("href");
            if (Utils.SHOULD_PRT) System.out.println("href = " + href);
            allHospitals.add(href);
        }
    }

    public static void run() {
        AllHospitalListCrawler crawler = new AllHospitalListCrawler(Resource.obtainAsync());
        List<String> provinceList = Utils.readObjList(TextValue.Path.provinces);
        for (String p : provinceList) {
            crawler.crawl(p);
        }
        Utils.writeObjList(allHospitals, TextValue.Path.hospitals);
    }

    public static void main(String[] args) {
        run();
    }
}
