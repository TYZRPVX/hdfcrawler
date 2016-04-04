package edu.hit.ehealth.main.util;

import edu.hit.ehealth.main.crawler.basestruct.FutureListener;
import edu.hit.ehealth.main.erroravenger.ErrorDBReader;
import edu.hit.ehealth.main.util.mail.Mailer;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 检测IP 是否可用，并提供邮件提醒
 */
public class SiteInspector {

    private static final int SHOULD_SEND_MAIL_ERROR_COUNT = 10000;
    private static final int SHOULD_WIPE_ERROR_COUNT = 500;

    public static void run() {
        String url = "http://www.haodf.com/";
        boolean isSiteAvailable = testSite(url);
        if (!isSiteAvailable) {
            Mailer.reportSiteInspector(url + " is not available, need change IP");
        }
        int count = countError();
        if (count > SHOULD_SEND_MAIL_ERROR_COUNT) {
            Mailer.reportSiteInspector("need change IP, count(Error) > " + SHOULD_SEND_MAIL_ERROR_COUNT);
        }
    }

    private static int countError() {
        ErrorDBReader errorDBReader = new ErrorDBReader();
        int errorCount = errorDBReader.getErrorCount();
        return errorCount;
    }

    /**
     * 连续多次访问链接，若全部都访问超时，判定IP被封
     * @param url
     * @return
     */
    private static boolean testSite(String url) {
        final int testTimes = 20;
        boolean isSiteAvailable = false;
        for (int i = 0; i < testTimes; i++) {
            isSiteAvailable = isSiteAvailable || canSiteVisited(url);
        }
        return isSiteAvailable;
    }

    private static boolean canSiteVisited(String url) {
        boolean canVisited = true;
        Future<Content> future = Resource.publicAsync.execute(Request.Get(url), new FutureListener());
        try {
            Content content = future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            canVisited = false;
        }
        return canVisited;
    }

    public static void main(String[] args) {
        run();
    }
}
