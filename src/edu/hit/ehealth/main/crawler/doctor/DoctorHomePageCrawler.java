package edu.hit.ehealth.main.crawler.doctor;
//ysgrwz 缺的四个字段找不到,
/*修复了opentime 存在乱码的问题*/
import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.doctor.DoctorHomepageDao;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.doctor.DoctorHomepage;
import org.apache.http.client.fluent.Async;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

public class DoctorHomePageCrawler extends Crawler {

    public static Set<String> homepageSet = new HashSet<String>();

    protected DoctorHomepage doctorHomepage;

    private int servStar = 0;

    private static DoctorHomepageDao homepageDao = null;

    static {
        homepageDao = GlobalApplicationContext.getContext().getBean(DoctorHomepageDao.class);
        homepageSet.add("http://chenenguo.haodf.com/");
        homepageSet.add("http://lixueni.haodf.com/");
        homepageSet.add("http://cdxcf6.haodf.com/");
        homepageSet.add("http://zhoubaotong.haodf.com/");
    }


    public DoctorHomePageCrawler(Async async) {
        super(async);
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        doctorHomepage = new DoctorHomepage();
        doctorHomepage.setCrawlDate(Utils.getCurrentDate());
        doctorHomepage.setCrawlPageUrl(trackPageUrl());

        String line = null;
        boolean isCrawlID = false;
        StringBuilder voteContent = new StringBuilder();
        StringBuilder scienceTitle = new StringBuilder();
        StringBuffer skillContent = new StringBuffer();
        StringBuffer introContent = new StringBuffer();
        StringBuilder newYearContent = new StringBuilder();
        StringBuilder consultScopeContent = new StringBuilder();
        boolean isVoteContent = false;
        boolean isOfficeContent = false;
        boolean isSkill = false;
        boolean isIntro = false;
        boolean isNewYearWord = false;
        boolean isConsultScope = false;
        while ((line = content.readLine()) != null) {

            if (line.contains("<h1><a class=\"space_b_link_url\"")) {
                extractPK(line);
            }
            if (line.contains("总 访 问")) {
                extractVisitCount(line);
            }
            if (!isCrawlID && line.contains("www.haodf.com/doctor")) {
                extractInfoID(line);
                isCrawlID = true;
            }
            if (line.contains("开通时间")) {
                extractOpenDate(line);
            }
            if (line.contains("<li>心意礼物")) {
                extractGiftNum(line);
            }
            if (line.contains("<li>感 谢 信")) {
                extractThankNum(line);
            }
            if (line.contains("<li>患者投票")) {
                extractPatientVoteNum(line);
            }
            if (line.contains("总诊后报到患者")) {
                extractReportPatient(line);
            }
            if (line.contains("<li>总 患 者")) {
                extractPatientNum(line);
            }
            if (line.contains("<p class=\"f18 mr_title\">")) {
                extractDoctorName(line);
            }
            if (line.contains("class=\"doc_name f22 fl")) {
                extractDoctorTitle(line);
            }
            if (line.contains("<li>随访患者总数")) {
                extractFollowUpPatient(line);
            }
            // office content
            if (line.contains("div class=\"fl pr\">")) {
                isOfficeContent = true;
            } else if (isOfficeContent) {
                scienceTitle.append(line);
            }
            if (line.contains("<p class=\"space_b_store_site fs\">")) {
                isOfficeContent = false;
                extractOffice(scienceTitle.toString());
            }
            if (line.contains("爱心值")) {
                extractHeartValue(line);
            }
            if (line.contains("class=\"blue\" target =\"_blank")) {
                extractContriValue(line);
            }
            if (line.contains("http://www.haodf.com/hospital/")) {
                extractHospitalID(line);
            }
            // vote content
            if (line.contains("pa_vote clearfix p15\"")) {
                isVoteContent = true;
            } else if (isVoteContent) {
                voteContent.append(line);
            }
            if (line.contains("class=\"ml15 mr15 pt10 pb20\"")) {
                isVoteContent = false;
            }
            // new year
            if (line.contains("p_announce")) {
                isNewYearWord = true;
            } else if (isNewYearWord) {
                newYearContent.append(line);
                if (Utils.SHOULD_PRT) System.out.println("new year = " + line);
            }
            if (line.contains("wel_spread fs")) {
                isNewYearWord = false;
            }
            // consult scope
            if (line.contains("咨询范围")) {
                isConsultScope = true;
            } else if (isConsultScope) {
                consultScopeContent.append(line);
            }
            if (line.contains("</div>")) {
                isConsultScope = false;
            }
            if (line.contains("服务星值")) {
                extractServiceStar(line);
            }
            if (line.contains("服务星等级")) {
                // todo don't know how to crawl
            }
            if (line.contains("<li>总 文 章")) {
                extractEssayNum(line);
            }
            if (line.contains("微信诊后报到患者")) {
                extractWeChatNum(line);
            }
            if (line.contains("排名：")) {
                extractRank(line);
            }
            if (line.contains("\"gray\">已扫")) {
                extractScanNum(line);
            }
            if (line.contains("class=\"doctor_star_yellow\"></span>")) {
                servStar++;
            }
        }
        doctorHomepage.setServiceStarLevel(servStar);
        servStar = 0;
        extractVoteContent(voteContent.toString());
        extractNewYearWord(newYearContent.toString());
        extractConsultScope(consultScopeContent.toString());
        try {
            homepageDao.save(doctorHomepage);
        } catch (Exception i) {
        }
    }

    private void extractScanNum(String line) {
        String scanNum = RegexUtils.regexFind("已扫(.+)次", line);
        scanNum = scanNum.replaceAll(",", "");
        if (Utils.SHOULD_PRT) System.out.println("scanNum = " + scanNum);
        Integer scan = Integer.valueOf(scanNum);
        doctorHomepage.setScanCount(scan);
    }

    private void extractRank(String line) {
        String rankSt = RegexUtils.regexFind("排名：(.+)</a>", line);
        if (Utils.SHOULD_PRT) System.out.println("rank = " + rankSt);
        doctorHomepage.setRank(rankSt);
    }

    private void extractPK(String line) {
    	//加了个 .+
        String pk = RegexUtils.regexFind(".+space_b_url\">(\\S+).haodf.com</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("pk = " + pk);
        doctorHomepage.setPrimaryId(pk);
        extractSkillsAndIntro("http://" + pk + ".haodf.com/api/index/ajaxdoctorintro?uname=" + pk);
    }

    private void extractWeChatNum(String line) {
        String weChatNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("we chat = " + weChatNum);
        doctorHomepage.setWeChatReportNum(Integer.valueOf(weChatNum));
    }

    private void extractEssayNum(String line) {
        String essayNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("essay = " + essayNum);
        doctorHomepage.setEssayNum(Integer.valueOf(essayNum));
    }

    private void extractServiceStar(String line) {
        String star = RegexUtils.regexFind("(\\d*)分", line);
        if (Utils.SHOULD_PRT)
            System.out.println("star = " + star);
        doctorHomepage.setServiceStarValue(star);
    }

    private void extractConsultScope(String scope) {
        if (Utils.SHOULD_PRT) System.out.println("scope = " + scope);
        doctorHomepage.setConsultScope(scope);
    }

    private void extractNewYearWord(String word) {
        if (Utils.SHOULD_PRT) System.out.println("word = " + word);
        doctorHomepage.setNewYearWord(word);
    }


    private void extractHospitalID(String line) {
        String hospitalID = RegexUtils.regexFind("hospital/(\\S+).htm", line);
        if (Utils.SHOULD_PRT) System.out.println("hospitalID = " + hospitalID);
        doctorHomepage.setHospitalID(hospitalID);
    }

    private void extractContriValue(String line) {
        String contriValue = RegexUtils.regexFind(">(\\S+)</a>", line);
        if (Utils.SHOULD_PRT) System.out.println("contriValue = " + contriValue);
        doctorHomepage.setContriValue(Integer.valueOf(contriValue));
    }

    private void extractHeartValue(String line) {
        String value = RegexUtils.regexFind("爱心值:(\\S+)分", line);
        if (Utils.SHOULD_PRT) System.out.println("value = " + value);
        doctorHomepage.setHeartValue(Integer.valueOf(value));
    }

    private void extractOffice(String line) {
        String hospitalName = RegexUtils.regexFind("_blank\">(\\S+)</a>", line);
        String officeName = hospitalName +
                RegexUtils.regexFind("_blank\">(\\S+)</a>\\s", line);
        if (Utils.SHOULD_PRT) System.out.println("officeName = " + officeName);
        doctorHomepage.setOfficeName(officeName);
    }

    private void extractFollowUpPatient(String line) {
        String patientNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line).replaceAll(",", "");
        if (Utils.SHOULD_PRT) System.out.println("patientNum = " + patientNum);
        doctorHomepage.setFollowPatientNum(Integer.valueOf(patientNum));
    }

    private void extractDoctorTitle(String line) {
        String title = RegexUtils.regexFind("&nbsp;&nbsp;(.+)<", line);
        String docTitle = RegexUtils.regexFind("(\\S+)\\s+", title);
        if (Utils.SHOULD_PRT) System.out.println("title = " + docTitle);
        doctorHomepage.setDocTitle(docTitle);
        String scienceTitle = "";
        scienceTitle = RegexUtils.regexFind("\\s+(\\S+)\\s*", title);

        if (Utils.SHOULD_PRT) System.out.println("scienceTitle = " + scienceTitle);
        doctorHomepage.setScienceTitle(scienceTitle);
    }

    private void extractDoctorName(String line) {
        String doctorName = RegexUtils.regexFind("f18 mr_title\">(\\S+)大夫", line);
        if (Utils.SHOULD_PRT) System.out.println("doctorName = " + doctorName);
        doctorHomepage.setDoctorName(doctorName);
    }

    private void extractPatientNum(String line) {
        String allPatientNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println(" allpatientNum = " + allPatientNum);
        doctorHomepage.setAllPatientNum(Integer.valueOf(allPatientNum));
    }

    private void extractReportPatient(String line) {
        String patientNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("report patient = " + patientNum);
        doctorHomepage.setPatientReportNum(Integer.valueOf(patientNum));
    }

    private void extractVoteContent(String s) {
        if (Utils.SHOULD_PRT) System.out.println("s = " + s);
        doctorHomepage.setPatientVoteContent(s);
    }

    private void extractPatientVoteNum(String line) {
        String vote = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("vote = " + vote);
        doctorHomepage.setVoteNum(Integer.valueOf(vote));
    }

    private void extractThankNum(String line) {
        String thankNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("thankNum = " + thankNum);
        doctorHomepage.setThankLetterNum(Integer.valueOf(thankNum));
    }

    private void extractGiftNum(String line) {
        String giftNum = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("giftNum = " + giftNum);
        doctorHomepage.setHeartGiftNum(Integer.valueOf(giftNum));
    }

    private void extractLastOnlineDate(String line) {
        String date = RegexUtils.regexFind("orange1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("date = " + date);
//        doctorHomepage.set todo another table do
    }

    private void extractOpenDate(String line) throws ParseException {
        String dateTime = RegexUtils.regexFind("range1 pr5\">(.+)</span>", line);
        if (Utils.SHOULD_PRT) System.out.println("dateTime = " + dateTime);
//        String date = Utils.noWayParseDateText(line);
        doctorHomepage.setOpenTime(dateTime);
    }

    private void extractInfoID(String line) {
//        if (Utils.SHOULD_PRT) System.out.println("line = " + line);
        String id = RegexUtils.regexFind("doctor/(\\S+).htm", line);
        if (Utils.SHOULD_PRT) System.out.println("id = " + id);
        doctorHomepage.setPersonInfoID(id);
    }

    private void extractVisitCount(String line) {
        String rawVisitCount = RegexUtils.regexFind("pr5\">(\\S+)</span>", line);
        String visitCount = rawVisitCount.replaceAll("[,\\s]", "");
        Integer cnt = Integer.valueOf(visitCount);
        if (Utils.SHOULD_PRT) System.out.println("cnt = " + cnt);
        doctorHomepage.setVisitCount(cnt);
    }

    private void extractSkillsAndIntro(String url) {
        // ajax
        try {
            Document doc = Jsoup.connect(url).get();
//            if (Utils.SHOULD_PRT) System.out.println("doc = " + doc);
            Elements hh = doc.getElementsByClass("hh");
            Element skills = hh.get(0);
            Element intros = hh.get(1);
//            if (Utils.SHOULD_PRT) System.out.println("skills = " + skills);
            String skillText = skills.text();
            if (Utils.SHOULD_PRT) System.out.println("skillData = " + skillText);
            doctorHomepage.setSkills(skillText);
            String introText = intros.text();
            if (Utils.SHOULD_PRT) System.out.println("introText = " + introText);
            doctorHomepage.setDoctorIntro(introText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DoctorHomePageCrawler c = new DoctorHomePageCrawler(Resource.obtainAsync());
        for (String url : homepageSet) {
            c.crawl(url);
        }
    }
}
