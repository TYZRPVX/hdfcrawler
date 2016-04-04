package edu.hit.ehealth.test;

import edu.hit.ehealth.main.define.TextValue;
import edu.hit.ehealth.main.util.Utils;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// 想要不弹出窗口的selenium 太难了
public class TestSel {

    static {
        File driverFile = new File("D:\\science\\code\\haodf2\\utils\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", driverFile.getAbsolutePath());
    }

    private WebDriver driver;

    public static void main(String[] args) {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--no-startup-window");
////I tried this line also: options.addArguments("--silent-launch");
////        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
////        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//        WebDriver driver = new ChromeDriver(options);
//        System.out.println("driver start");
//        driver.get("http://www.baidu.com");
//        System.out.println("driver = " + driver.getCurrentUrl());
        List<String> allDoctorInfoCenter = new ArrayList<String>();
//        allDoctorInfoCenter.add("http://www.haodf.com/doctor/DE4r0BCkuHzdeZXi59T0-22oNxVxB.htm");
//        allDoctorInfoCenter.add("http://www.haodf.com/doctor/DE4rO-XCoLUmj2q51t4xLBGs4w.htm");
        allDoctorInfoCenter.add("http://www.haodf.com/doctor/DE4r08xQdKSLVhEzPz9LoalFRy9w.htm");
        Utils.writeObjList(allDoctorInfoCenter, TextValue.Path.doctorInfoCenters);

        List<String> allDoctorHome = new ArrayList<String>();
//        allDoctorHome.add("http://zhouyaou.haodf.com/");
//        allDoctorHome.add("http://fanbifa.haodf.com/");
        allDoctorHome.add("http://congli74.haodf.com/");
        Utils.writeObjList(allDoctorHome, TextValue.Path.doctorHomepages);
        TestSel testSel = new TestSel();
        testSel.m1();
    }

    private void m1() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (StackTraceElement ste : trace) {
            System.out.println(
                    "called by "
                            + ste.getClassName() + "." +
                            ste.getMethodName() + "/" +
                            ste.getFileName()
            );
        }
    }


}
