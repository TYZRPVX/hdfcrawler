package edu.hit.ehealth.main.crawler.hospital;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.hospital.HospitalFacultyDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.hospital.HospitalFaculty;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.util.List;

//H_YYKS

public class HospitalFacultyCrawler extends Crawler {


    public HospitalFacultyCrawler(Async async) {
        super(async);
    }

    private static HospitalFacultyDao facultyDao
            = GlobalApplicationContext.getContext().getBean(HospitalFacultyDao.class);
    private HospitalFaculty hospitalFaculty = new HospitalFaculty();

    public static void main(String[] args) {
        HospitalFacultyCrawler hospitalFacultyCrawler = new HospitalFacultyCrawler(Resource.obtainAsync());
        hospitalFacultyCrawler.crawl("http://www.haodf.com/faculty/DE4roiYGYZwExU5IqlG3HelVn.htm");
    }

    protected void extractPrimaryID(String line) {
        String pk = RegexUtils.regexFind("faculty/(.+).htm", line);
        if (Utils.SHOULD_PRT)
            System.out.println("pk = " + pk);
        hospitalFaculty.setPrimaryId(pk);
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        String line = null;
        String currentUrl = trackPageUrl();
        extractPrimaryID(currentUrl);
        boolean isNearFaculty = false;
        boolean isIntro = false;
        StringBuilder introBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
            if (line.contains("http://www.haodf.com/hospital")) {
                extractHospID(line);
                isNearFaculty = true;
            }
            if (isNearFaculty && line.contains("href=\"http://www.haodf.com/faculty")) {
                extractSpecID(line);
                extractFacultyName(line);
                isNearFaculty = false;
            }
            if (line.contains("直接通话大夫")
                    && line.contains("span")) {
                extractConsultNum(line);
            }
            if (line.contains("加号患者")) {
                extractAddedPatNum(line);
            }
            if (line.contains("加号服务的大夫")) {
                extractAddedDocNum(line);
            }
            if (line.contains("科室介绍") && line.contains("class=\"tdl\"")) {
                isIntro = true;
            }
            if (isIntro) {
                introBlock.append(line + "\n");
            }
            if (isIntro && line.contains("jieshao.htm")) {
                isIntro = false;
                if (Utils.SHOULD_PRT)
                    System.out.println("introBlock = " + introBlock);
            }
        }
        hospitalFaculty.setFacultyIntro(introBlock.toString());

        hospitalFaculty.setCrawlPageUrl(currentUrl);
        hospitalFaculty.setCrawlDate(Utils.getCurrentDate());
        try {
            facultyDao.save(hospitalFaculty);
        } catch (Exception ignored) {
        }
    }


    private void extractAddedDocNum(String line) {
        String addedNum = RegexUtils.regexFind("大夫.+>(\\d*)</span", line);
        Integer num = Integer.valueOf(addedNum);
        if (Utils.SHOULD_PRT)
            System.out.println("num = " + num);
        hospitalFaculty.setAddedDocNum(num);
    }

    private void extractAddedPatNum(String line) {
        String addedNum = RegexUtils.regexFind("患者.+>(\\d*)</span", line);
        Integer num = Integer.valueOf(addedNum);
        if (Utils.SHOULD_PRT)
            System.out.println("num = " + num);
        hospitalFaculty.setAddedPatNum(num);
    }

    private void extractConsultNum(String line) {
        String conNum = RegexUtils.regexFind("大夫.+>(\\d*)</span", line);
        int num = Integer.valueOf(conNum);
        if (Utils.SHOULD_PRT)
            System.out.println("num = " + num);
        hospitalFaculty.setConsultDocNum(num);
    }

    private void extractFacultyName(String line) {
        String faculty = RegexUtils.regexFind("\">(.+)<", line);
        if (Utils.SHOULD_PRT)
            System.out.println("faculty = " + faculty);
        hospitalFaculty.setFacultyName(faculty);
    }

    private void extractSpecID(String line) {
        // can;t get ID but name

    }

    private void extractHospID(String line) {
        String hospID = RegexUtils.regexFind("hospital/(.+).htm", line);
        if (Utils.SHOULD_PRT)
            System.out.println("hospID = " + hospID);
        hospitalFaculty.setHospitalID(hospID);
    }

    public static void run() {
        List<String> facultys = Utils.readObjList(TextValue.Path.facultys);
        HospitalFacultyCrawler hospitalFacultyCrawler = new HospitalFacultyCrawler(Resource.obtainAsync());
        for (String faculty : facultys) {
            hospitalFacultyCrawler.crawl(faculty);
        }
    }

}
