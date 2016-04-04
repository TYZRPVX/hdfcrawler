package edu.hit.ehealth.main.crawler.hospital;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.dao.hospital.SpecialityListDao;
import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.hospital.SpecialityList;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;


/**
 * http://www.haodf.com/keshi/list.htm
 */
public class SpecialityListCrawler extends Crawler {


    SpecialityListDao listDao =
            GlobalApplicationContext.getContext().getBean(SpecialityListDao.class);
    SpecialityList specialityList = new SpecialityList();
    private List<String> allSpecialitys = new ArrayList<String>();

    public SpecialityListCrawler(Async async) {
        super(async);
    }

    public static void run() {
        SpecialityListCrawler c = new SpecialityListCrawler(Resource.obtainAsync());
        c.crawl("http://www.haodf.com/keshi/list.htm");
    }

    public static void main(String[] args) {
        run();
    }

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        String line = null;

        while ((line = content.readLine()) != null) {
            if (line.contains("class=\"black_link\">")) {
                String specName = RegexUtils.regexFind("\"black_link\">(.+)</a></li>", line);
                if (Utils.SHOULD_PRT) System.out.println("specName = " + specName);
                specialityList.setSpecName(specName);
                String pk = RegexUtils.regexFind("<li><a href=\"/keshi/(\\S+).htm\" title", line);
                if (Utils.SHOULD_PRT) System.out.println("pk = " + pk);
                specialityList.setPrimaryId(pk);
                String specUrl = "http://www.haodf.com/keshi/" + pk + ".htm";
                if (Utils.SHOULD_PRT) System.out.println("specUrl = " + specUrl);
                allSpecialitys.add(specUrl);
                specialityList.setCrawlPageUrl(specUrl);
                specialityList.setCrawlDate(Utils.getCurrentDate());
                try {
                    listDao.save(specialityList);
                } catch (Exception ignored) {

                }
            }
        }

        Utils.writeObjList(allSpecialitys, TextValue.Path.specialitys);
    }
}
