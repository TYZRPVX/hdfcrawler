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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 运行完就可抛弃
 */
public class ProvinceListCrawler extends Crawler implements Serializable {

    public static final String startUrl = "http://q.haodf.com/";

    public ProvinceListCrawler(Async async) {
        super(async);
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = content.readLine()) != null) {

            if (line.length() == 0) continue;

            sb.append(line);
        }
        String html = sb.toString();
        extractProvince(html);
    }


    private void extractProvince(String html) {
        List<String> allProvinces = new ArrayList<String>();
        if (Utils.SHOULD_PRT) System.out.println(html);
        Document doc = Jsoup.parse(html);
        Element content = doc.getElementById("blue");
        Elements links = content.getElementsByTag("a");
        for (Element link : links) {

            String href = link.attr("href");
            if (href.endsWith("haodf.com")) {
                allProvinces.add(href);
                if (Utils.SHOULD_PRT) System.out.println(href);
            }
        }
        writeToFile(allProvinces);
    }

    private void writeToFile(List<String> allProvinces) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(TextValue.Path.provinces));
            oos.writeObject(allProvinces);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ProvinceListCrawler provinceListCrawler = new ProvinceListCrawler(Resource.obtainAsync());
        provinceListCrawler.crawl(startUrl);
    }
}
