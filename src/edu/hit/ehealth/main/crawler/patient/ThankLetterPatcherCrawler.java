package edu.hit.ehealth.main.crawler.patient;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.patient.ThankLetterPatcherDao;
import edu.hit.ehealth.main.util.Counter;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.patient.ThankLetterPatcher;
import org.apache.http.client.fluent.Async;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ThankLetterPatcherCrawler extends Crawler {

    private int pageCount = 1;
    private int pageNum;

    public ThankLetterPatcherCrawler(Async async) {
        super(async);
    }

    private static ThankLetterPatcherDao patcherDao =
            GlobalApplicationContext.getContext().getBean(ThankLetterPatcherDao.class);

    private ThankLetterPatcher patcher = new ThankLetterPatcher();

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        String line = null;
        boolean isBlock = false;
        StringBuilder orderBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
            if (line.contains("<div class=\"tel-service-user1 fl\">")) {
                isBlock = true;
            } else if (isBlock) {
                orderBlock.append(line + "\n");
            }
            if (isBlock && line.contains("<em class=\"thankFeedBack cp blue4\">")) {
                isBlock = false;
                extractOrderBlock(orderBlock.toString());
                orderBlock.setLength(0);
                patcher.setCrawlDate(Utils.getCurrentDate());
                patcher.setCrawlPageUrl(trackPageUrl());
                patcher.setPrimaryId(UUID.randomUUID().toString());
                try {
                    patcherDao.save(patcher);
                } catch (DataIntegrityViolationException dev) {
                }
            }
            if (line.contains("共&nbsp")) {
//                if (Utils.SHOULD_PRT) System.out.println("line = " + line);
                String pageNumStr = RegexUtils.regexFind("共&nbsp;(\\d*)&nbsp;页", line);
                pageNum = Integer.valueOf(pageNumStr);
            }
        }

        pageCount++;
        if (pageCount <= pageNum) {
            String currentUrl = trackPageUrl();
            String nextPageUrl = RegexUtils.regexReplace("(nowPage=\\d*)", currentUrl, "nowPage=" + pageCount);
            if (Utils.SHOULD_PRT) System.out.println("nextPageUrl = " + nextPageUrl);
            crawl(nextPageUrl);
        }
    }

    private void extractOrderBlock(String s) {
        for (String line : s.split("\\r?\\n")) {
            if (line.contains("<p>地区：")) {
                String area = RegexUtils.regexFind("<p>地区：来自(.+)</p>", line);
                if (Utils.SHOULD_PRT) System.out.println("area = " + area);
                patcher.setArea(area);
            }
            if (line.contains("<span>") && line.contains("</span>")) {
                String content = RegexUtils.regexFind(">(.+)<", line);
                if (Utils.SHOULD_PRT) System.out.println("content = " + content);
                patcher.setContent(content);
            }
        }
    }

    public static void run() {

        //ThankLetterPatcher
        List<String> allLetterPatcherUrl = new ArrayList<String>();
        for (String homepage : Resource.sPhoneDoctorHomepage) {
            String patchUrl = homepage +
                    "payment/ajaxshowcommentganxie?nowPage=1";
            allLetterPatcherUrl.add(patchUrl);
        }
        Counter counter = Counter.newInstance();
        ThankLetterPatcherCrawler patcherCrawler = new ThankLetterPatcherCrawler(Resource.obtainAsync());
        for (String patcher : allLetterPatcherUrl) {
            counter.printCrawlCountAboutPhoneHomepage(ThankLetterPatcherCrawler.class);
            patcherCrawler.crawl(patcher);
        }

    }

    public static void main(String[] args) {
        ThankLetterPatcherCrawler c = new ThankLetterPatcherCrawler(Resource.obtainAsync());
        c.crawl("http://zhaoquanming.haodf.com/payment/ajaxshowcommentganxie?nowPage=1");
    }
}
