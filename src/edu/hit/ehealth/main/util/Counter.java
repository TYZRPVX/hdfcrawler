package edu.hit.ehealth.main.util;

import edu.hit.ehealth.main.util.mail.Mailer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Counter {

    private static final int NEAR_DEFINITION = 2000;
    private int count;

    private Counter() {
        count = 0;
    }

    public static Counter newInstance() {
        return new Counter();
    }

    private static Class[] allClasses = Clocker.getAllCrawlerClasses();

    private static List<Class> lastCrawlerClasses = new ArrayList<>(Arrays.asList(allClasses));

    public static void reportCrawlCount(Class clazz, int count, int total) {
        System.out.println("[Crawler Name]:" + clazz.getSimpleName() + " [count]:" + count + " [total]:" + total);
//        if (isNearInteger(count, total) && lastCrawlerClasses.contains(clazz)) {
//            lastCrawlerClasses.remove(clazz);
//            Mailer.sendToAddressList(clazz.getName() + " will finish crawling"
//                    , "下面是未完成爬取任务的爬虫\n"
//                            + getReadableClassesName(lastCrawlerClasses)
//            );
//        }
    }

    public static String getReadableClassesName(List<Class> classes) {
        StringBuilder builder = new StringBuilder();
        for (Class clazz : classes) {
            builder.append(clazz.getCanonicalName() + "\n");
        }
        return builder.toString();
    }

    private static boolean isNearInteger(int count, int total) {
        return Math.abs(count - total) < NEAR_DEFINITION;
    }


    public void printCrawlCountAboutHomepage(Class clazz) {
        count++;
        reportCrawlCount(clazz, count, Resource.homepageTotalCount);
    }

    public void printCrawlCountAboutPhoneHomepage(Class clazz) {
        count++;
        reportCrawlCount(clazz, count, Resource.phoneDoctorHomepageTotalCount);
    }

    public void printCrawlCountAboutInfoCenter(Class clazz) {
        count++;
        reportCrawlCount(clazz, count, Resource.infoCenterTotalCount);
    }

}
