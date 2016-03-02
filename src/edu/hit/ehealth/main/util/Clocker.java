package edu.hit.ehealth.main.util;

import edu.hit.ehealth.main.crawler.doctor.*;
import edu.hit.ehealth.main.crawler.doctor.DoctorDailyCrawler;
import edu.hit.ehealth.main.crawler.doctor.DoctorInfoAndHomeCrawler;
import edu.hit.ehealth.main.crawler.hospital.*;
import edu.hit.ehealth.main.crawler.patient.PatientClubCrawler;
import edu.hit.ehealth.main.crawler.patient.PatientServiceAreaCrawler;
import edu.hit.ehealth.main.crawler.patient.ThankLetterCrawler;
import edu.hit.ehealth.main.crawler.patient.ThankLetterPatcherCrawler;
import edu.hit.ehealth.main.erroravenger.ErrorWiper;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class Clocker {

    static Class[] mustExistClasses = new Class[]{
//            AllHospitalListCrawler.class,
//            DoctorListCrawler.class,
//            PhoneConsultListCrawler.class,
    };
    static Class[] everyDayClasses = new Class[]{
            DoctorDailyCrawler.class,
    };

    private static Class[] oneMinuteClasses = new Class[]{
            ErrorWiper.class,
    };
    static Class[] everyMinutesClasses = new Class[]{
            SiteInspector.class,
    };
    static Class[] crawlersPack1 = new Class[]{
//            ErrorWiper.class, // should be run in one thread or progress
            DiseaseTableCrawler.class,
            DoctorExperienceCrawler.class,
            DoctorEssayCrawler.class,
    };
    static Class[] crawlersPack2 = new Class[]{
            DoctorInfoAndHomeCrawler.class,
            DoctorGiftCrawler.class,
            HospitalFacultyCrawler.class,
            HospitalInfoCrawler.class,
    };
    static Class[] crawlersPack3 = new Class[]{
            HospitalListCrawler.class,
            DoctorConsultOrderCrawler.class,
            PatientServiceAreaCrawler.class,
            PatientClubCrawler.class,
    };
    static Class[] crawlersPack4 = new Class[]{
            SpecialityListCrawler.class,
            ThankLetterCrawler.class,
            ThankLetterPatcherCrawler.class,
            DoctorConsultValuationCrawler.class,
    };

    public static Class[] getAllCrawlerClasses() {
        Class[] classes = edu.hit.ehealth.main.util.ArrayUtils.concatAll(
                mustExistClasses,
                everyDayClasses,
                crawlersPack1,
                crawlersPack2,
                crawlersPack3,
                crawlersPack4
        );
        return classes;
    }

    public static void main(String[] args) {
        String taskOrder = args[0];
        Class[] toRunClasses = new Class[0];
        if (taskOrder.equals("1")) {
            runClassesDailyConcurrently(everyDayClasses);
            runClassesEveryMinutesConcurrently(everyMinutesClasses);
            runClassesOneMinuteConcurrently(oneMinuteClasses);
            toRunClasses = (Class[]) ArrayUtils.addAll(mustExistClasses, crawlersPack1);
        } else if (taskOrder.equals("2")) {
            toRunClasses = (Class[]) ArrayUtils.addAll(mustExistClasses, crawlersPack2);
        } else if (taskOrder.equals("3")) {
            toRunClasses = (Class[]) ArrayUtils.addAll(mustExistClasses, crawlersPack3);
        } else if (taskOrder.equals("4")) {
            toRunClasses = (Class[]) ArrayUtils.addAll(mustExistClasses, crawlersPack4);
        }

        runClassesWeeklyConcurrently(toRunClasses);
    }

    private static void runClassesEveryMinutesConcurrently(Class[] everyHoursClasses) {
        Timer timer = new Timer();
        long aHourTime = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
        timer.schedule(new ConcurrentTasks(everyHoursClasses), Utils.getCurrentDate(), aHourTime);
    }

    public static void runClassesOneMinuteConcurrently(Class[] classes) {
        Timer timer = new Timer();
        long aWeekTime = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
        timer.schedule(new ConcurrentTasks(classes), Utils.getCurrentDate(), aWeekTime);
    }

    public static void runClassesWeeklyConcurrently(Class[] classes) {
        Timer timer = new Timer();
        long aWeekTime = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
        timer.schedule(new ConcurrentTasks(classes), Utils.getCurrentDate(), aWeekTime);
    }

    public static void runClassesDailyConcurrently(Class[] classes) {
        Timer timer = new Timer();
        long aDayTime = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        timer.schedule(new ConcurrentTasks(classes), Utils.getCurrentDate(), aDayTime);
    }

    public static void runClassesConcurrently(Class[] classes) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<Class>> futures = new ArrayList<>();
        for (final Class runnin : classes) {
            Callable<Class> callable = new Callable<Class>() {
                @Override
                public Class call() throws Exception {
                    Runner.run(runnin);
                    return runnin;
                }
            };
            Future<Class> future = exec.submit(callable);
            futures.add(future);
        }
    }

    private static class ConcurrentTasks extends TimerTask {

        private final Class[] runnins;

        public ConcurrentTasks(Class[] runnins) {
            this.runnins = runnins;
        }

        @Override
        public void run() {
            runClassesConcurrently(runnins);
        }
    }


}



