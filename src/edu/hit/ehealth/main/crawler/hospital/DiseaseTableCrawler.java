package edu.hit.ehealth.main.crawler.hospital;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.hospital.DiseaseTableDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.hospital.DiseaseTable;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DiseaseTableCrawler extends Crawler {


    private static Set<String> specUrlSet = new HashSet<String>();

    private DiseaseTable diseaseTable;

    public DiseaseTableCrawler(Async async) {
        super(async);
    }


    DiseaseTableDao diseaseTableDao =
            GlobalApplicationContext.getContext().getBean(DiseaseTableDao.class);


    private static void crawlAllSpecDis() {
        String line = null;
        BufferedReader disBR = Utils.getUrlReader("http://www.haodf.com/jibing/list.htm");
        try {
            while ((line = disBR.readLine()) != null) {
                if (line.contains("list.htm")
                        && !line.contains("href=\"http://www.haodf.com/jibing/list.htm\"")
                        && line.contains("href=\"/jibing")) {
//                    if (Utils.SHOULD_PRT) System.out.println("line = " + line);
                    String diseasePostfix = RegexUtils.regexFind("href=\"\\s*(\\S+)\\s*\"", line);
                    String specDisUrl = "http://www.haodf.com" + diseasePostfix;
                    specUrlSet.add(specDisUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        //need crawl disease by spec
        String line = null;
        diseaseTable = new DiseaseTable();
        String currentUrl = trackPageUrl();
        diseaseTable.setCrawlPageUrl(currentUrl);
        diseaseTable.setCrawlDate(Utils.getCurrentDate());
        String specID = RegexUtils.regexFind("jibing/(\\S+)/list", currentUrl);
        diseaseTable.setSpecID(specID);
        while ((line = content.readLine()) != null) {
            if (line.contains("target=\"_blank\"")
                    && line.contains("</a></li>")) {
                extractDisName(line);
                extractPrimaryID(line);
                try {
                    diseaseTableDao.save(diseaseTable);
                } catch (Exception ignored) {

                }
            }
        }
    }

    protected void extractPrimaryID(String line) {
        String pk = RegexUtils.regexFind("jibing/(\\S+).htm", line);
        if (Utils.SHOULD_PRT) System.out.println("pk = " + pk);
        diseaseTable.setPrimaryId(pk);
    }

    private void extractDisName(String line) {
//        if (Utils.SHOULD_PRT) System.out.println("line = " + line);
        String disName = RegexUtils.regexFind("target=\"_blank\".+>(.+)</a>", line);
        if (Utils.SHOULD_PRT) System.out.println("disName = " + disName);
        diseaseTable.setDisName(disName);
    }

    public static void run() {
        crawlAllSpecDis();
        Crawler c = new DiseaseTableCrawler(Resource.obtainAsync());
        for (String url : specUrlSet) {
            if (Utils.SHOULD_PRT) System.out.println("url = " + url);
            c.crawl(url);
        }
    }


    public static void main(String[] args) {
        run();
    }
}
