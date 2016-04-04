package edu.hit.ehealth.main.crawler.doctor;
/*修复了疗效、态度、治疗方式有乱码的问题,部分记录有空的字段，是因为网页数据确实是空的*/

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.crawler.basestruct.NextPageTracker;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.doctor.DoctorExperienceDao;
import edu.hit.ehealth.main.util.Counter;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.doctor.DoctorExperience;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DoctorExperienceCrawler extends Crawler implements NextPageTracker {

    protected int pageCount = 1;
    private int pageNum;


    public DoctorExperienceCrawler(Async async) {
        super(async);
    }

    private static DoctorExperienceDao experienceDao =
            GlobalApplicationContext.getContext().getBean(DoctorExperienceDao.class);

    private DoctorExperience experience = new DoctorExperience();

    public static void run() {
        //KBJY
        List<String> allExpUrl = new ArrayList<String>();
        int count = 0;
        for (String info : Resource.sAllInfoCenter) {
            String expUrl1 = RegexUtils.regexReplace("(.htm)", info, "/kanbingjingyan/1.htm");
            allExpUrl.add(expUrl1);
        }
        Counter counter = Counter.newInstance();
        DoctorExperienceCrawler experienceCrawler = new DoctorExperienceCrawler(Resource.obtainAsync());
        for (String expUrl : allExpUrl) {
            counter.printCrawlCountAboutInfoCenter(DoctorExperienceCrawler.class);
            experienceCrawler.crawl(expUrl);
        }

    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {

        String line = null;
        String currentUrl = trackPageUrl();
        extractInfoID(currentUrl);
        boolean isMainBlock = false;
        StringBuilder mainBlock = new StringBuilder();
        while ((line = content.readLine()) != null) {
            if (line.contains("共&nbsp")) {
                extractPageNum(line);
            }
            if (line.contains("<td class=\"dlemd\">")) {
                isMainBlock = true;
            }
            if (isMainBlock) {
                mainBlock.append(line + "\n");
            }
            if (line.contains("<td class=\"midrt\"></td>")) {
                isMainBlock = false;
                extractMainBlock(mainBlock.toString());
                mainBlock.setLength(0);
                experience.setCrawlDate(Utils.getCurrentDate());
                experience.setCrawlPageUrl(trackPageUrl());
                experience.setPrimaryId(UUID.randomUUID().toString());
                try {
                    experienceDao.save(experience);
                } catch (Exception e) {
                }
            }
        }
        pageCount++;
        if (pageCount <= pageNum) {
            crawl(getNextPageUrl());
        }
    }

    @Override
    public int extractPageNum(String line) {
        String pageNumStr = RegexUtils.regexFind("共&nbsp;(\\S+)&nbsp;页", line);
        pageNum = Integer.valueOf(pageNumStr);
        return pageNum;
    }

    private void extractMainBlock(String block) {
//        if (Utils.SHOULD_PRT) System.out.println("block = " + block);
        boolean isCureProcess = false;
        StringBuilder cureBlock = new StringBuilder();
        for (String line : block.split("\n")) {
            if (line.contains("class=\"gray\"") && line.contains("来自")) {
                String city = RegexUtils.regexFind("来自(.+)\\)", line);
                if (Utils.SHOULD_PRT) System.out.println("city = " + city);
                experience.setFromCity(city);
            }
            if (line.contains("<span>疾病")) {
                if (line.contains("href")) {
                    String disease = RegexUtils.regexFind(".+none;\">(\\S+)</a>", line);
                    System.out.println("disease = " + disease);
                    experience.setDisease(disease);
                } else {
                    String disease = RegexUtils.regexFind(".+span>(.*)</td>", line);
                    System.out.println("disease = " + disease);
                    experience.setDisease(disease);
                }
               /* if (Utils.SHOULD_PRT) System.out.println("disease = " + disease);
                experience.setDisease(disease);*/
            }
            if (line.contains("class=\"gray\">时间")) {
                String postDate = RegexUtils.regexFind("class=\"gray\">时间.(.+)\\s*</td>", line);
                if (Utils.SHOULD_PRT) System.out.println("postDate = " + postDate);
                experience.setRecordDate(postDate);
            }
            if (line.contains("width=\"34%\">疗效")) {
                String effect1 = RegexUtils.regexFind("class=\"orange\">(.+)</span>", line);
                String effect2 = RegexUtils.regexFind("class=\"gray\">(.+)</span>", line);

                if (effect1.length() > effect2.length()) experience.setEffect(effect2);
                else experience.setEffect(effect1);
//                String effect = line;
              /*  if (Utils.SHOULD_PRT) System.out.println("effect = " + effect);
                experience.setEffect(effect);*/
            }
            if (line.contains("width=\"34%\">态度")) {
                String attitude1 = RegexUtils.regexFind("class=\"orange\">(.+)</span>", line);
                String attitude2 = RegexUtils.regexFind("class=\"gray\">(.+)</span>", line);

                if (attitude1.length() > attitude2.length()) {
                    experience.setAttitude(attitude2);
//                	System.out.println("attitude = " + attitude2);
                } else {

                    experience.setAttitude(attitude1);
//                	System.out.println("attitude = " + attitude1);
                }
//                String attitude = RegexUtils.regexFind("class=\"orange\">(.+)</span>", line);
//                String attitude = line;
  /*              if (Utils.SHOULD_PRT) System.out.println("attitude = " + attitude);
                experience.setAttitude(attitude);*/
            }
            if (line.contains("class=gray>治疗方式")) {
                if (line.contains("未填")) {
                    experience.setCureMethod("未填");
                } else {
                    String cureMethod = RegexUtils.regexFind("class=gray>治疗方式：</span>(.+)<br>", line);
                    experience.setCureMethod(cureMethod);
                }
            }
            if (line.contains("class=\"spacejy\">")) {
                isCureProcess = true;
            } else if (isCureProcess) {
                cureBlock.append(line);
            }
            if (isCureProcess && line.contains("</td>")) {
                isCureProcess = false;
                extractCureProcess(cureBlock.toString());
            }
            if (line.contains("人推荐)")) {
                extractRecommendNum(line);
            }
            if (line.contains("个回应</a>")) {
                extractReplyNum(line);
            }
        }
    }

    @Override
    public String getNextPageUrl() {
        String currentUrl = trackPageUrl();
        String nextPageUrl = currentUrl.substring(0,
                currentUrl.lastIndexOf("/")) + "/" + pageCount + ".htm";
        return nextPageUrl;
    }

    private void extractReplyNum(String line) {
        String replyNum = RegexUtils.regexFind("(\\d*)个回应", line);
        if (Utils.SHOULD_PRT) System.out.println("replyNum = " + replyNum);
        experience.setReplyNum(replyNum);
    }

    private void extractRecommendNum(String line) {
        String recNum = RegexUtils.regexFind("(\\d*)人推荐", line);
        if (Utils.SHOULD_PRT) System.out.println("recNum = " + recNum);
        experience.setRecommandNum(recNum);
    }

    private void extractCureProcess(String s) {
        String cureProcess = RegexUtils.regexFind("(.+)", s);
        if (Utils.SHOULD_PRT) System.out.println("cureProcess = " + cureProcess);
        experience.setCureProcess(cureProcess);
    }

    private void extractInfoID(String currentUrl) {
        String id = RegexUtils.regexFind("doctor/(.+)/kanbing", currentUrl);
        if (Utils.SHOULD_PRT) System.out.println("id = " + id);
        experience.setPersonInfoID(id);
    }

    public static void main(String[] args) {
        DoctorExperienceCrawler c = new DoctorExperienceCrawler(Resource.obtainAsync());
//        c.crawl("http://www.haodf.com/doctor/DE4r08xQdKSLBT0wXYSdpUn-8HdJ/kanbingjingyan/3.htm");
        c.crawl("http://www.haodf.com/doctor/DE4r0BCkuHzduSNTnHT0-22oNxVxB/kanbingjingyan/1.htm");
        //run();
    }
}
