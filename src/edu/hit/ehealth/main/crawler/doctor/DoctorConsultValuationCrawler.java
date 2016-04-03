package edu.hit.ehealth.main.crawler.doctor;
//服务评价表两个字段找不到
import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.crawler.basestruct.NextPageTracker;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.doctor.DoctorConsultValuationDao;
import edu.hit.ehealth.main.util.Counter;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.doctor.DoctorConsultValuation;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ajax 请求居然是post操作，越来越复杂了，还是只能用selenium来
 * 我无意间又找到了ajax 真实地址：
 * http://zhaoquanming.haodf.com/index/ajaxShowFeedBack?from=dzone&doctorId=4269&userid=145556&nowPage=1&score=-1
 */
public class DoctorConsultValuationCrawler extends Crawler implements NextPageTracker {


    private int pageNum;

    public DoctorConsultValuationCrawler(Async async) {
        super(async);
    }


    protected int pageCount = 1;
    private DoctorConsultValuation valuation = new DoctorConsultValuation();
    private static DoctorConsultValuationDao valuationDao =
            GlobalApplicationContext.getContext().getBean(DoctorConsultValuationDao.class);

    public static void main(String[] args) {
        DoctorConsultValuationCrawler doctorConsultValuationCrawler = new DoctorConsultValuationCrawler(Resource.obtainAsync());
        doctorConsultValuationCrawler
                .crawl("http://zhouaiguo.haodf.com/index/ajaxShowFeedBack?from=dzone&doctorId=4269&userid=145556&nowPage=1&score=-1");
    }

    public static void run() {
        //FWPJ
        List<String> allValuaUrl = new ArrayList<String>();
        for (String homepage : Resource.sPhoneDoctorHomepage) {
            String valuaUrl = homepage +
                    "index/ajaxShowFeedBack?from=dzone&doctorId=4269&userid=145556&nowPage=1&score=-1";
            allValuaUrl.add(valuaUrl);
        }
        Counter counter = Counter.newInstance();
        DoctorConsultValuationCrawler valuationCrawler = new DoctorConsultValuationCrawler(Resource.obtainAsync());
        for (String valua : allValuaUrl) {
            counter.printCrawlCountAboutPhoneHomepage(DoctorConsultValuationCrawler.class);
            valuationCrawler.crawl(valua);
        }

    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        extractHomepageID(trackPageUrl());
        extractCharge(trackPageUrl());

        String line = null;
        boolean isBlock = false;
        StringBuilder valueBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
            if (line.contains("<div class=\"tel-service-user-details fl\">")) {
                isBlock = true;
            } else if (isBlock) {
                valueBlock.append(line + "\n");
            }
            if (isBlock && line.contains("</ul>")) {
                extractValueBlock(valueBlock.toString());
                valueBlock.setLength(0);
                isBlock = false;
            }
            if (line.contains("共&nbsp")) {
                pageNum = extractPageNum(line);
            }
        }
        pageCount++;
        if (pageCount <= pageNum) {
            crawl(getNextPageUrl());
        }

    }

    private void extractCharge(String s) throws IOException {
        String docID = RegexUtils.regexFind("http://(.+).haodf", s);
        String chargeUrl = "http://" + docID + ".haodf.com/payment/newintro#successAnswer";
        BufferedReader urlReader = Utils.getUrlReader(chargeUrl);
        String line = null;
        while ((line = urlReader.readLine()) != null) {
            if (line.contains("<span class=\"pr5\"><span class='show_price'>")) {
                String charge = RegexUtils.regexFind("show_price'>(.+)</span>", line)
                        + RegexUtils.regexFind("show_duration'>(.+)</span>", line);
                if (Utils.SHOULD_PRT) System.out.println("charge = " + charge);
                valuation.setChargeStandard(charge);
            }
        }
    }

    private void extractHomepageID(String s) {
        String docID = RegexUtils.regexFind("http://(.+).haodf", s);
        if (Utils.SHOULD_PRT) System.out.println("docID = " + docID);
        valuation.setHomepageID(docID);
    }

    private void extractValueBlock(String s) {
        valuation.setCrawlDate(Utils.getCurrentDate());
        valuation.setCrawlPageUrl(trackPageUrl());
        valuation.setPrimaryId(UUID.randomUUID().toString());
        for (String line : s.split("\n")) {
            if (line.contains("用户反馈: ")) {
                String feedback = RegexUtils.regexFind("<span>(.+)</span>", line);
                if (Utils.SHOULD_PRT) System.out.println("feedback = " + feedback);
                valuation.setUserFeedback(feedback);
            }
            if (line.contains("通话时间：")) {
                String callDate = RegexUtils.regexFind("通话时间：(.+)</li>", line);
                if (Utils.SHOULD_PRT) System.out.println("callDate = " + callDate);
                valuation.setCallDate(callDate);
            }
            if (line.contains("总体评价：")) {
                String valu = RegexUtils.regexFind("dzone_orange\">(.+)</span>", line);
                valuation.setValuation(valu);
            }
        }
        try {
            valuationDao.save(valuation);
        } catch (Exception ignored) {
        }
    }

    @Override
    public int extractPageNum(String line) {
        String pageNumStr = RegexUtils.regexFind("共&nbsp;(\\d+)&nbsp;页", line);
        pageNum = Integer.valueOf(pageNumStr);
        return pageNum;
    }

    @Override
    public String getNextPageUrl() {
        String currentUrl = trackPageUrl();
        String nextPageUrl = RegexUtils.regexReplace("(nowPage=\\d+)", currentUrl, "nowPage=" + pageCount);
        return nextPageUrl;
    }
}
