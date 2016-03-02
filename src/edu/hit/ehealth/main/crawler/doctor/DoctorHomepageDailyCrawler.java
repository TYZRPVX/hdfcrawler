package edu.hit.ehealth.main.crawler.doctor;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.doctor.DoctorHomepageDailyDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.doctor.DoctorHomepageDaily;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.UUID;

public class DoctorHomepageDailyCrawler extends Crawler {

    public DoctorHomepageDailyCrawler(Async async) {
        super(async);
    }

    protected DoctorHomepageDaily doctorHomepageDaily;

    protected static DoctorHomepageDailyDao dailyDao;

    static {
        dailyDao = GlobalApplicationContext.getContext().getBean(DoctorHomepageDailyDao.class);
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        doctorHomepageDaily = new DoctorHomepageDaily();
        doctorHomepageDaily.setCrawlDate(Utils.getCurrentDate());
        doctorHomepageDaily.setCrawlPageUrl(trackPageUrl());
        doctorHomepageDaily.setPrimaryId(UUID.randomUUID().toString());

        String line = null;
        boolean isCrawlID = false;
        while ((line = content.readLine()) != null) {

            if (line.contains("<h1><a class=\"space_b_link_url\"")) {
                extractHome(line);
            }
            if (line.contains("总 访 问")) {
                extractVisitCount(line);
            }
            if (!isCrawlID && line.contains("www.haodf.com/doctor")) {
                extractInfoID(line);
                isCrawlID = true;
            }
            if (line.contains("总诊后报到患者")) {
                extractReportPatient(line);
            }
            if (line.contains("<li>总 患 者")) {
                extractPatientNum(line);
            }
            if (line.contains("<li>上次在线")) {
                extractLastOnlineDate(line);
            }
        }
        try {
            dailyDao.save(doctorHomepageDaily);
        } catch (Exception ignored) {
        }
    }

    private void extractRank(String line) {
        String rankSt = RegexUtils.regexFind("排名：(.+)</a>", line);
        if (Utils.SHOULD_PRT) System.out.println("rank = " + rankSt);
    }

    private void extractHome(String line) {
        String homepageID = RegexUtils.regexFind("space_b_url\">(\\S+).haodf.com</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("pk = " + homepageID);
        doctorHomepageDaily.setHomepageID(homepageID);
    }

    private void extractLastOnlineDate(String line) throws ParseException {
//        String dateStr = Utils.regexFind("orange1 pr5\">(.+)</span>", line);
        String date = Utils.noWayParseDateText(line);
        if (Utils.SHOULD_PRT) System.out.println("date = " + date);

        doctorHomepageDaily.setLastOnlineDate(date);
    }

    private void extractWeChatNum(String line) {
        String weChatNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("we chat = " + weChatNum);
    }

    private void extractEssayNum(String line) {
        String essayNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("essay = " + essayNum);
    }


    private void extractPatientNum(String line) {
        String allPatientNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println(" allpatientNum = " + allPatientNum);
    }

    private void extractReportPatient(String line) {
        String patientNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("report patient = " + patientNum);
        doctorHomepageDaily.setYesterReportPatientNum(Integer.valueOf(patientNum));
    }


    private void extractInfoID(String line) {
//        if (Utils.SHOULD_PRT) System.out.println("line = " + line);
        String id = RegexUtils.regexFind("doctor/(\\S+).htm", line);
        if (Utils.SHOULD_PRT) System.out.println("id = " + id);
        doctorHomepageDaily.setPersonInfoID(id);
    }

    private void extractVisitCount(String line) {
        String rawVisitCount = RegexUtils.regexFind("pr5\">(\\S+)</span>", line);
        String visitCount = rawVisitCount.replaceAll("[,\\s]", "");
        Integer cnt = Integer.valueOf(visitCount);
        if (Utils.SHOULD_PRT) System.out.println("cnt = " + cnt);
        doctorHomepageDaily.setYesterVisitCount(cnt);
    }


    public static void main(String[] args) {
        DoctorHomepageDailyCrawler c = new DoctorHomepageDailyCrawler(Resource.obtainAsync());
        c.crawl("http://wuyongjian.haodf.com/");
    }
}
