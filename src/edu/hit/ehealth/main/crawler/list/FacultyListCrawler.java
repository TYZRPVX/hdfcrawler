package edu.hit.ehealth.main.crawler.list;

import edu.hit.ehealth.main.crawler.basestruct.Crawler;
import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.RegexUtils;
import edu.hit.ehealth.main.util.Utils;
import org.apache.http.client.fluent.Async;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class FacultyListCrawler extends Crawler {

    public FacultyListCrawler(Async async) {
        super(async);
    }

    List<String> facultyList = new ArrayList<String>();

    @Override
    protected void parseContent(BufferedReader content) throws Exception {
        /**
         * given http://www.haodf.com/hospital/DE4raCNSz6OmG3OUNZWCWNv0.htm
         * crawl all "http://www.haodf.com/faculty/..." and write to obj
         */
        String line = null;
        while ((line = content.readLine()) != null) {
            if (line.contains("faculty")
                    && line.contains("target=\"_blank\" class=\"blue\"")) {
                extractFaculty(line);
            }
        }
        Utils.writeObjList(facultyList, TextValue.Path.facultys);
    }

    private void extractFaculty(String line) {
        String url = RegexUtils.regexFind("href=\"(\\S+)\"", line);
        if (Utils.SHOULD_PRT) System.out.println("url = " + url);
        facultyList.add(url);
    }

    public void crawlAllFaculty() {
        List<String> allHospitals = Utils.readObjList(TextValue.Path.hospitals);
        for (String hospital : allHospitals) {
            crawl(hospital);
        }
        Utils.writeObjList(facultyList, TextValue.Path.facultys);
    }

    public static void main(String[] args) {
    }
}
