package edu.hit.ehealth.main.crawler.doctor;
/*修复了时间字段有乱码的问题*/

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.doctor.DoctorGiftDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Counter;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.doctor.DoctorGift;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DoctorGiftCrawler extends Crawler {


    private DoctorGift doctorGift = new DoctorGift();
    private static DoctorGiftDao giftDao =
            GlobalApplicationContext.getContext().getBean(DoctorGiftDao.class);

    public DoctorGiftCrawler(Async async) {
        super(async);
    }

    public static void run() {
        // LW
        List<String> allGiftUrl = new ArrayList<String>();

        for (String info : Resource.sAllInfoCenter) {
            String key = RegexUtils.regexFind("doctor/(\\S+).htm", info);
            String giftUrl1 = "http://www.haodf.com/api/doctor/" + key + "/ajaxgetpresentlist.htm";
            allGiftUrl.add(giftUrl1);
        }
        DoctorGiftCrawler giftCrawler = new DoctorGiftCrawler(Resource.obtainAsync());
        Counter counter = Counter.newInstance();
        for (String giftUrl : allGiftUrl) {
            counter.printCrawlCountAboutInfoCenter(DoctorGiftCrawler.class);
            giftCrawler.crawl(giftUrl);
        }

    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        String line = null;
        boolean isMainBlock = false;
        extractInfoID(trackPageUrl());
        StringBuilder mainBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
            if (line.contains("<td width=\"50%\" class=\"gray\">")) {
                isMainBlock = true;
            }
            if (isMainBlock) {
                mainBlock.append(line + "\n");
            }

            if (line.contains("<div class=\"fr mt15 mr5\">")) {
                isMainBlock = false;
                extractMainBlock(mainBlock.toString());
                mainBlock.setLength(0);
                doctorGift.setCrawlPageUrl(trackPageUrl());
                doctorGift.setCrawlDate(Utils.getCurrentDate());
                doctorGift.setPrimaryId(UUID.randomUUID().toString());
                try {
                    giftDao.save(doctorGift);
                } catch (Exception e) {
                }
            }
        }
    }

    private void extractInfoID(String s) {
        String id = RegexUtils.regexFind("doctor/(.+)/ajax", s);
        if (Utils.SHOULD_PRT) System.out.println("id = " + id);
        doctorGift.setPersonInfoID(id);
    }

    private void extractMainBlock(String s) throws ParseException {
        for (String line : s.split("\n")) {
            if (line.contains("class=\"gray\">时间")) {
                String dateStr = RegexUtils.regexFind("时间：(.+)</tr>", line);
                if (Utils.SHOULD_PRT) System.out.println("dateStr = " + dateStr);
               // String date = Utils.noWayParseDateText(line);
                doctorGift.setDate(dateStr);
            }
            if (line.contains("<div class=\"fb mt5 f14\">")) {
                String giftWord = RegexUtils.regexFind("f14\">(.+)</div>", line);
                if (Utils.SHOULD_PRT) System.out.println("giftWord = " + giftWord);
                doctorGift.setContent(giftWord);
            }
        }
    }

    public static void main(String[] args) {
        DoctorGiftCrawler c = new DoctorGiftCrawler(Resource.obtainAsync());
        c.crawl("http://www.haodf.com/api/doctor/DE4rO-XCoLUEiq37rPeWduCBgU/ajaxgetpresentlist.htm");
    }
}


