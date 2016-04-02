package edu.hit.ehealth.main.crawler.doctor;
//注意测试的链接。程序没有问题
import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.doctor.DoctorConsultOrderDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.doctor.DoctorConsultOrder;
import org.apache.http.client.fluent.Async;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * http://400.haodf.com/index/search?facultyname=%C8%AB%B2%BF
 * 部分医生开通了电话功能
 */
// TODO: 2016/2/24 改版
public class DoctorConsultOrderCrawler extends Crawler {


    private static DoctorConsultOrderDao orderDao =
            GlobalApplicationContext.getContext().getBean(DoctorConsultOrderDao.class);

    private DoctorConsultOrder consultOrder = new DoctorConsultOrder();

    public DoctorConsultOrderCrawler(Async async) {
        super(async);
    }

    public static void run() {
        //ZXDD
        List<String> allOrderUrl = new ArrayList<String>();

        for (String homepage : Resource.sPhoneDoctorHomepage) {
            if (Utils.SHOULD_PRT) System.out.println("homepage = " + homepage);
            String orderUrl1 = homepage + "payment/ajaxshowtelorders";
            allOrderUrl.add(orderUrl1);
        }
        DoctorConsultOrderCrawler orderCrawler = new DoctorConsultOrderCrawler(Resource.obtainAsync());
        for (String orderUrl : allOrderUrl) {
            orderCrawler.crawl(orderUrl);
        }
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        String line = null;
        String currentUrl = trackPageUrl();
        extractPersonID(currentUrl);
        boolean isBlock = false;
        StringBuilder orderBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
            //System.out.println("line = " + line);
            if (line.contains("<div class=\"tel-service-user1 fl\">")) {
                isBlock = true;
            } else if (isBlock) {
                orderBlock.append(line + "\n");
            }
            if (isBlock && line.contains("<em class=\"thankFeedBack cp blue4\">")) {
                isBlock = false;
                extractOrderBlock(orderBlock.toString());
                System.out.println("orderBlock "+orderBlock.toString());
                orderBlock.setLength(0);
                consultOrder.setCrawlPageUrl(trackPageUrl());
                consultOrder.setCrawlDate(Utils.getCurrentDate());
                consultOrder.setPrimaryId(UUID.randomUUID().toString());
                try {
                    orderDao.save(consultOrder);
                } catch (DataIntegrityViolationException dve) {
                    dve.printStackTrace();
                }
            }
        }
    }

    private void extractOrderBlock(String s) {
        for (String line : s.split("\\r?\\n")) {
            if (line.contains("<p>地区：")) {
                extractArea(line);
            }
            if (line.contains("<p>订单号：")) {
                extractOrderNum(line);
            }
            if (line.contains("<p>状态：")) {
                extractState(line);
            }
            if (line.contains("<span>病情描述")) {
                extractDisContent(line);
            }
        }
    }

    private void extractDisContent(String line) {
        String content = RegexUtils.regexFind("<span>病情描述：(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("content = " + content);
        consultOrder.setContent(content);
    }

    private void extractState(String line) {
        String state = RegexUtils.regexFind("<p>状态：(.+)</p>", line);
        if (Utils.SHOULD_PRT) System.out.println("state = " + state);
        consultOrder.setOrderState(state);
    }

    private void extractOrderNum(String line) {
        String orderNum = RegexUtils.regexFind("<p>订单号：(\\S+)</p>", line);
        if (Utils.SHOULD_PRT) System.out.println("orderNum = " + orderNum);
        consultOrder.setOrderNumber(orderNum);
    }

    private void extractArea(String line) {
        String area = RegexUtils.regexFind("<p>地区：(.+)</p>", line);
        if (Utils.SHOULD_PRT) System.out.println("area = " + area);
        consultOrder.setLocation(area);
    }

    private void extractPersonID(String currentUrl) {
        String docID = RegexUtils.regexFind("http://(\\S+).haodf", currentUrl);
        if (Utils.SHOULD_PRT) System.out.println("docID = " + docID);
        consultOrder.setHomepageID(docID);
    }

    public static void main(String[] args) {
        DoctorConsultOrderCrawler c = new DoctorConsultOrderCrawler(Resource.obtainAsync());
        c.crawl("http://cdxcf6.haodf.com/payment/ajaxshowtelorders");
        //之前测试的链接写错了，应该是ajaxshowtelorders
    }

}
