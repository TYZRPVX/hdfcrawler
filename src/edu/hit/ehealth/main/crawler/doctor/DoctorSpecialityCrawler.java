package edu.hit.ehealth.main.crawler.doctor;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.io.IOException;

public class DoctorSpecialityCrawler extends Crawler {


    public DoctorSpecialityCrawler(Async async) {
        super(async);
    }

    public static void main(String[] args) {
        DoctorSpecialityCrawler c = new DoctorSpecialityCrawler(Resource.obtainAsync());
        c.crawl("http://www.haodf.com/faculty/DE4roiYGYZwExU5IqlG3HelVn.htm");
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        String line = null;
        String currentUrl = trackPageUrl();
        extractIntro(currentUrl);
        extractPrimaryKey(currentUrl);
//        boolean isIntro = false;
//        StringBuilder introBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
            if (line.contains("<a href=\"http://www.haodf.com/hospital")) {
                extractHospitalID(line);
            }
            if (line.contains("<a href=\"http://www.haodf.com/faculty") && line.contains("</a>&nbsp;&gt")) {
                extractSpecName(line);
            }
            if (line.contains("本科室可直接通话大夫")) {
                extractCallDoctorNum(line);
            }
            if (line.contains("已成功加号患者")) {
                extractAddPatientNum(line);
            }
            if (line.contains("本科室开通加号服务的")) {
                extractAddDoctorNum(line);
            }
//            if (line.contains("<td class=\"tdl\">科室介绍：</td>")) {
//                isIntro = true;
//            } else if (isIntro) {
//                introBlock.append(line);
//            }

        }
    }

    private void extractIntro(String currentUrl) {
        String introUrl = currentUrl.substring(0,
                currentUrl.lastIndexOf(".htm")) + "/jieshao.htm";
        BufferedReader introBR = Utils.getUrlReader(introUrl);
        String line = null;
        boolean isIntro = false;
        StringBuilder introBlock = new StringBuilder();
        try {
            while ((line = introBR.readLine()) != null) {
                if (line.contains("<div id=\"about_det\">")) {
                    isIntro = true;
                } else if (isIntro) {
                    introBlock.append(line);
                }
                if (line.contains("<div><a class=\"blue\"")) {
                    isIntro = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String introStr = introBlock.toString();
    }

    private void extractAddDoctorNum(String line) {
        String addDocNum = RegexUtils.regexFind("5px;\">(\\d*)</span>", line);
    }

    private void extractHospitalID(String line) {
        String id = RegexUtils.regexFind("hospital/(\\S+).htm\">", line);
    }

    private void extractSpecName(String line) {
        String specName = RegexUtils.regexFind("htm\">(.+)</a>&nbsp;&gt;", line);
    }

    private void extractCallDoctorNum(String line) {
        String doctorNum = RegexUtils.regexFind("5px;\">(\\d*)</span>", line);
    }

    private void extractAddPatientNum(String line) {
        String patientNum = RegexUtils.regexFind("5px;\">(\\d*)</span>", line);
    }

    private void extractPrimaryKey(String currentUrl) {
        String pk = RegexUtils.regexFind("faculty/(\\S+).htm", currentUrl);
    }
}
