package edu.hit.ehealth.main.crawler.hospital;
//解决了地址乱码问题，JHDF，JHHZ找不到
import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.hospital.HospitalInfoDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.hospital.HospitalInfo;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HospitalInfoCrawler extends Crawler {

    public static Set<String> hospitalUrlSet = new HashSet<String>();
    private static HospitalInfoDao hidao =
            GlobalApplicationContext.getContext().getBean(HospitalInfoDao.class);

    static {
        hospitalUrlSet.add("http://www.haodf.com/hospital/DE4raCNSz6OmG3OUNZWCWNv0.htm");
        hospitalUrlSet.add("http://www.haodf.com/hospital/DE4r0Fy0C9LuZRZkwYg3YJgRNvXH4DNxe.htm");
    }

    private HospitalInfo hospitalInfo = new HospitalInfo();

    public HospitalInfoCrawler(Async async) {
        super(async);
    }

    public static void run() {
        HospitalInfoCrawler crawler = new HospitalInfoCrawler(Resource.obtainAsync());
        for (String hospital : Resource.sHospitals) {
            crawler.crawl(hospital);
        }
    }


    public static void main(String[] args) {

        HospitalInfoCrawler c = new HospitalInfoCrawler(Resource.obtainAsync());
        for (String url : hospitalUrlSet) {
            c.crawl(url);
        }
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        String curUrl = trackPageUrl();
        StringBuffer mainContent = new StringBuffer();
        String line = null;
        String pk = RegexUtils.regexFind("hospital/(\\S+)\\.htm", curUrl);
        if (Utils.SHOULD_PRT) System.out.println("pk = " + pk);
        hospitalInfo.setPrimaryId(pk);
        hospitalInfo.setCrawlPageUrl(curUrl);
        hospitalInfo.setCrawlDate(Utils.getCurrentDate());

        boolean isMainContent = false;
        while ((line = content.readLine()) != null) {
            if (line.contains("地　　址")) {
                extractAddress(line);
            }
            if (line.contains("咨询大夫") && line.contains("位大夫在线")) { // include number
                if (Utils.SHOULD_PRT) System.out.println("dd fu:" + line);
                extractConsultDoctorNumber(line);
            }
            if (line.contains("已成功加号患者")) {
                if (Utils.SHOULD_PRT) System.out.println(line);
                extractPatientNumber(line);
            }
            if (line.contains("本院开通加号服务")) {
                if (Utils.SHOULD_PRT) System.out.println(line);
                extractAddedDoctorNumber(line);
            }
            if (line.contains("人)</span>")) {
                if (Utils.SHOULD_PRT) System.out.println(line);
                extractOfficeAndDoctorNum(line);
            }
            if (line.contains("<div class=\"panelA_blue\">")) {
                isMainContent = true;
            }
            if (isMainContent && line.contains("<a href=")) {
                if (Utils.SHOULD_PRT) System.out.println(line);
                extractNameAndClass(line);
                isMainContent = false;
            }
            if (line.contains("info.haodf.com")) {
                extractDetailIntro(line);
            }
        }
        try {
            hidao.save(hospitalInfo);
        } catch (Exception ignored) {
        }
    }

    private void extractDetailIntro(String line) {
        String introUrl = RegexUtils.regexFind("href=\"(\\S+)jieshao.htm", line) + "jieshao.htm";
        if (Utils.SHOULD_PRT) System.out.println("introUrl = " + introUrl);
        extractDetailInfo(introUrl);
    }

    private void extractNameAndClass(String line) {
        String hospitalHref = RegexUtils.regexFind("href=\"(\\S+)\">", line);
        if (Utils.SHOULD_PRT) System.out.println("hospitalHref = " + hospitalHref);

        String name = RegexUtils.regexFind("htm\">(.+)</a", line);
        if (Utils.SHOULD_PRT) System.out.println("name = " + name);
        String hospitalClass = "";
        try {
            hospitalClass = RegexUtils.regexFind("\\((.*)\\)", line); //no class crawl nothing
        } catch (Exception ignored) {
            //as default
        }
        if (Utils.SHOULD_PRT) System.out.println("hospitalClass = " + hospitalClass);

        hospitalInfo.setHospitalName(name);
        hospitalInfo.setHospitalClass(hospitalClass);

    }

    private void extractOfficeAndDoctorNum(String line) {
        String officeNum = RegexUtils.regexFind("科室\\D*(\\d+)", line); //error
        String doctorNum = RegexUtils.regexFind("大夫\\D*(\\d+)", line);
        if (Utils.SHOULD_PRT) System.out.println("doctorNum = " + doctorNum);
        if (Utils.SHOULD_PRT) System.out.println("officeNum = " + officeNum);
        Integer docNumberInt = -1;
        Integer offNumberInt = -1;
        try {
            docNumberInt = Integer.valueOf(doctorNum);
            offNumberInt = Integer.valueOf(officeNum);
        } catch (Exception i) {
        }
        hospitalInfo.setOfficeNumber(docNumberInt);
        hospitalInfo.setDoctorNumber(offNumberInt);
    }

    private void extractAddedDoctorNumber(String line) {
        String addedDocNum = RegexUtils.regexFind("\"orange\">(\\d*)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("addedDocNum = " + addedDocNum);
        Integer numberInt = -1;
        try {
            numberInt = Integer.valueOf(addedDocNum);
        } catch (Exception i) {
        }
        hospitalInfo.setAddedDoctorNumber(numberInt);
    }

    private void extractPatientNumber(String line) {
        String number = RegexUtils.regexFind("orange\">(\\d*)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("patient = " + number);
        Integer numberInt = -1;
        try {
            numberInt = Integer.valueOf(number);
        } catch (Exception i) {
        }
        hospitalInfo.setAddedPatientNumber(numberInt);
    }

    private void extractConsultDoctorNumber(String line) {
        String numberStr = RegexUtils.regexFind("orange\">(\\d*)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("number = " + numberStr);
        Integer numberInt = -1;
        try {
            numberInt = Integer.valueOf(numberStr);
        } catch (Exception i) {
        }
        hospitalInfo.setConsultDoctorNumber(numberInt);
    }

    private void extractAddress(String line) {
        String addr = RegexUtils.regexFind(".+</nobr>(.+)</td>", line); //should crawl blank
        if (Utils.SHOULD_PRT) System.out.println("addr: " + addr);
        hospitalInfo.setAddress(addr);
    }

    private void extractDetailInfo(String detailHref) {
        BufferedReader br = null;
        br = Utils.getUrlReader(detailHref);
        String line = null;
        boolean isInMainBlock = false;
        boolean isMainContent = false;
        StringBuffer sb = new StringBuffer();
        while (true) {
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (line.contains("<td class=\"middle_right\"></td>")) {
                isInMainBlock = false;
                isMainContent = false;
                break;
            }
            if (isMainContent) {
                sb.append("\n" + line);
            }
            if (line.contains("<td class=\"middle_left\"></td>")) {
                isInMainBlock = true;
            }
            if (isInMainBlock && line.contains("colspan=")) {
                isMainContent = true;
            }
        }
        String mainBlock = sb.toString();
        hospitalInfo.setHospitalIntro(mainBlock);
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
