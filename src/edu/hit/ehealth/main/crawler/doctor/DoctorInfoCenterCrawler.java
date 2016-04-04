package edu.hit.ehealth.main.crawler.doctor;

import edu.hit.ehealth.main.crawler.basestruct.DynamicContentCrawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.doctor.DoctorInfoCenterDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.doctor.DoctorInfoCenter;
import org.apache.http.client.fluent.Async;

import java.util.HashSet;
import java.util.Set;

/**
 * 爬取医生信息中心页
 */

public class DoctorInfoCenterCrawler extends DynamicContentCrawler {

    public static Set<String> doctorInfoCenterSet = new HashSet<String>();
    private static DoctorInfoCenterDao infoDao = null;

    static {
        infoDao = GlobalApplicationContext.getContext().getBean(DoctorInfoCenterDao.class);
    }

    private DoctorInfoCenter doctorInfoCenter = new DoctorInfoCenter();

    public DoctorInfoCenterCrawler() {
        super();
    }

    //XXX: for invoking
    public DoctorInfoCenterCrawler(Async asyncForError) {
        super(asyncForError);
    }

    public static void main(String[] args) {
        DoctorInfoCenterCrawler c = new DoctorInfoCenterCrawler();
//        c.crawl("http://www.haodf.com/doctor/DE4rO-XCoLUIy74Djfh42XHZ0l.htm");
//        c.crawl("http://www.haodf.com/doctor/DE4r08xQdKSLFCq0kgzM21bXlzuR.htm");
//        c.crawl("http://www.haodf.com/doctor/DE4r0BCkuHzduSNTnHT0-22oNxVxB.htm");
        c.crawl("http://www.haodf.com/doctor/DE4rO-XCoLUniOEmi88QpNK6eR.htm");

    }

    @Override
    protected void parseContent(String pageSrc) {
        String currentUrl = trackPageUrl();
        extractPK(currentUrl);
        doctorInfoCenter.setCrawlPageUrl(currentUrl);
        doctorInfoCenter.setCrawlDate(Utils.getCurrentDate());
        boolean isNearEffect = false;
        boolean isNearAttitude = false;
        boolean isNearHomepage = false;
        int star = 0;
        for (String line : pageSrc.split("\n")) {
            if (line.contains("<span id=\"hitcnt\" class=\"orange bold\">")) {
                extractVisitCount(line);
                extractVisitCountDate(line);
            }
            if (line.contains("width=\"19%\">疗效")) {
                isNearEffect = true;
            }
            if (isNearEffect && line.contains("满意")) {
                extractEffect(line);
                isNearEffect = false;
            }
            if (line.contains("width=\"19%\">态度")) {
                isNearAttitude = true;
            }
            if (isNearAttitude && line.contains("满意")) {
                extractAttitude(line);
                isNearAttitude = false;
            }
            doctorInfoCenter.setCanPhoneCall("0");
            if (line.contains("<td valign=\"top\">电话咨询：</td>")) {
                doctorInfoCenter.setCanPhoneCall("1");
            }
            doctorInfoCenter.setCanAdd("0");
            if (line.contains("<td valign=\"top\">预约加号：</td>")) {
                doctorInfoCenter.setCanAdd("1");
            }
            doctorInfoCenter.setCanConsult("0");
            if (line.contains("<td valign=\"top\">网上咨询：</td>")) {
                doctorInfoCenter.setCanConsult("1");
            }
            if (line.contains("valign=\"top\"><a href=\"http://www.haodf.com/faculty")) {
                extractFacultyID(line);
            }
            if (line.contains("<a href=\"http://www.haodf.com/hospital")) {
                extractHospitalID(line);
            }
            if (line.contains("个人网站：")) {
                isNearHomepage = true;
            }
            if (isNearHomepage && line.contains("<a href=")
                    && line.contains("target=\"_blank\"")) {
                storeHomepageUrl(line);
                isNearHomepage = false;
            }
            if (line.contains("诊治过的患者数")) {
                String allPatients = RegexUtils.regexFind("\\s+<td>(\\S+)</td>", line);
                if (Utils.SHOULD_PRT) System.out.println("患者总数 = " + allPatients);
                doctorInfoCenter.setAllPatients(allPatients);
            }
            if (line.contains("随访中的患者数")) {
                String sfPatients = RegexUtils.regexFind("\\s+<td>(\\S+)</td>", line);
                if (Utils.SHOULD_PRT) System.out.println("随访患者数= " + sfPatients);
                doctorInfoCenter.setSfPatients(sfPatients);
            }
            if (line.contains("starRightliang")) {
                star++;
            }
        }
        System.out.println("星级  " + star);
        doctorInfoCenter.setStar(String.valueOf(star));
        infoDao.save(doctorInfoCenter);
    }

    private void storeHomepageUrl(String line) {
        String homepage = RegexUtils.regexFind("<a href=\"(\\S+)\"", line);
        if (Utils.SHOULD_PRT) System.out.println("homepage = " + homepage);
        DoctorInfoAndHomeCrawler.allDoctorHomepage.add(homepage);
    }

    private void extractHospitalID(String line) {
        String hospID = RegexUtils.regexFind("href=\"(\\S+)\"", line);
        if (Utils.SHOULD_PRT) System.out.println("hospID = " + hospID);
        doctorInfoCenter.setHospitalID(hospID);
    }

    private void extractFacultyID(String line) {
        String facultyID = RegexUtils.regexFind("href=\"(\\S+)\"", line);
        if (Utils.SHOULD_PRT) System.out.println("facultyID = " + facultyID);
        doctorInfoCenter.setOfficeID(facultyID);
    }

    private void extractAttitude(String line) {
        String attitude = RegexUtils.regexFind(">(.+)</td", line);
        if (Utils.SHOULD_PRT) System.out.println("attitude = " + attitude);
        doctorInfoCenter.setAttitude(attitude);
    }

    private void extractEffect(String line) {
        String effect = RegexUtils.regexFind(">(.+)</td", line);
        if (Utils.SHOULD_PRT) System.out.println("effect = " + effect);
        doctorInfoCenter.setEffect(effect);
    }

    private void extractVisitCountDate(String line) {
        String dateStr = RegexUtils.regexFind("class=\"gray\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("dateStr = " + dateStr);
        doctorInfoCenter.setVisitCountDate(dateStr);
    }

    private void extractVisitCount(String line) {
        String cntStr = RegexUtils.regexFind("class=\"orange bold\">(\\d*)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("cnt = " + cntStr);
        doctorInfoCenter.setVisitCount(cntStr);
    }

    private void extractPK(String currentUrl) {
        String pk = RegexUtils.regexFind("doctor/(.+).htm", currentUrl);
        if (Utils.SHOULD_PRT) System.out.println("pk = " + pk);
        doctorInfoCenter.setPrimaryId(pk);
    }
}
