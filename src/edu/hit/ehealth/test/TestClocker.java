package edu.hit.ehealth.test;

import edu.hit.ehealth.main.crawler.doctor.DoctorDailyCrawler;
import edu.hit.ehealth.main.crawler.doctor.DoctorExperienceCrawler;
import edu.hit.ehealth.main.util.Counter;

import java.util.ArrayList;
import java.util.Arrays;

public class TestClocker {


    public static void main(String[] args) {
        Class[] classes = new Class[]{
                Task1.class,
                Task2.class,
        };
        ArrayList<Class> classes1 = new ArrayList<>(Arrays.asList(classes));
        Counter.reportCrawlCount(DoctorDailyCrawler.class, 1, 1);
        Counter.reportCrawlCount(DoctorExperienceCrawler.class, 1, 1);
    }
}



