package edu.hit.ehealth.main.crawler.patient;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.patient.PatientServiceAreaTextDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.patient.PatientServiceAreaText;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.UUID;

public class PatientServiceAreaTextCrawler extends Crawler {

    private static PatientServiceAreaTextDao textDao =
            GlobalApplicationContext.getContext().getBean(PatientServiceAreaTextDao.class);
    private PatientServiceAreaText areaText = new PatientServiceAreaText();

    public PatientServiceAreaTextCrawler(Async async) {
        super(async);
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        String currentUrl = trackPageUrl();
        extractQueryID(currentUrl);
        String line = null;
        boolean isPostDateEnd = false;
        boolean isMainBlock = false;
        StringBuilder mainBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
//            if (Utils.SHOULD_PRT) System.out.println("line = " + line);
            if (line.contains("space_b_link_url")) {
                extractHomepageID(line);
            }
            if (line.contains("zzx_yh_stream")) {
                isMainBlock = true;
            }
            if (isMainBlock) {
                mainBlock.append(line + "\n");
            }
            if (line.contains("发表于")) {
                isMainBlock = false;
                String mainBStr = mainBlock.toString();
                extractMain(mainBStr);
                mainBlock.setLength(0);
                areaText.setCrawlDate(Utils.getCurrentDate());
                areaText.setCrawlPageUrl(trackPageUrl());
                areaText.setPrimaryId(UUID.randomUUID().toString());
                try {
                    if (Utils.SHOULD_PRT)
                        System.out.println("save area text");
                    textDao.save(areaText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void extractMain(String mainBStr) throws ParseException {
        System.out.println("mainBStr = " + mainBStr);
        areaText.setContent(mainBStr);
        for (String line : mainBStr.split("\n")) {
//            if (Utils.SHOULD_PRT) System.out.println("line = " + line);
            if (line.contains("发表于")) {
                extractPostDate(line);
            }
            if (line.contains("状态：")) {
                extractState(line);
            }
        }
    }

    private void extractState(String line) {
        String state = RegexUtils.regexFind("状态：(.+)", line);
        if (Utils.SHOULD_PRT) System.out.println("state = " + state);
        areaText.setOrderState(state);
    }

    private void extractPostDate(String line) throws ParseException {
    	
    	String dateStr = RegexUtils.regexFind(".+于：(.+).+", line);
        if (Utils.SHOULD_PRT) System.out.println("dateStr = " + dateStr);
        areaText.setPostDateStr(dateStr);
        
        
    }


    private void extractHomepageID(String line) {
        String homepageID = RegexUtils.regexFind("http://(\\S+).haodf", line);
        if (Utils.SHOULD_PRT) System.out.println("homepageID = " + homepageID);
        areaText.setHomepageID(homepageID);
    }

    private void extractQueryID(String currentUrl) {
        String queryID = RegexUtils.regexFind("wenda/(\\S+).htm", currentUrl);
        if (Utils.SHOULD_PRT) System.out.println("qid = " + queryID);
        areaText.setQueryID(queryID);

    }

    public static void main(String[] args) {
        PatientServiceAreaTextCrawler c = new PatientServiceAreaTextCrawler(Resource.obtainAsync());
        c.crawl("http://www.haodf.com/wenda/zhaoquanming_g_3802909492.htm");
        c.crawl("http://www.haodf.com/wenda/surgwy_g_2098341767.htm");
        c.crawl("http://www.haodf.com/wenda/xuyishengdr_g_4125437020.htm");
    }
}
