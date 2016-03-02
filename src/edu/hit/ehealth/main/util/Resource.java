package edu.hit.ehealth.main.util;

import edu.hit.ehealth.main.define.TextValue;
import org.apache.http.client.fluent.Async;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Resource {

    public static Async obtainAsync() {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        return Async.newInstance().use(threadPool);
    }

    public static Async publicAsync = Async.newInstance().use(Executors.newFixedThreadPool(20));

    public static List<String> sAllDoctorHomepage = Utils.readObjList(TextValue.Path.doctorHomepages);

    public static final int homepageTotalCount = sAllDoctorHomepage.size();

    public static List<String> sAllInfoCenter = Utils.readObjList(TextValue.Path.doctorInfoCenters);

    public static final int infoCenterTotalCount = sAllInfoCenter.size();

    public static List<String> sPhoneDoctorHomepage = Utils.readObjList(TextValue.Path.phoneDoctors);

    public static final int phoneDoctorHomepageTotalCount = sPhoneDoctorHomepage.size();

    public static List<String> sHospitals = Utils.readObjList(TextValue.Path.hospitals);

    private static WebDriver sWebDriver;

    public static WebDriver getWebDriver() {
        if (sWebDriver == null) {
            File driverFile = new File("utils\\chromedriver.exe");
            System.setProperty("webdriver.chrome.driver", driverFile.getAbsolutePath());
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments();
//        chromeOptions.addArguments("--no-startup-window");
//        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            sWebDriver = new ChromeDriver(chromeOptions);
        }
        return sWebDriver;
    }

}
