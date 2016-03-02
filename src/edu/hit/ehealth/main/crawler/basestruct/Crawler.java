package edu.hit.ehealth.main.crawler.basestruct;

import edu.hit.ehealth.main.dao.ErrorMessageDao;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.exceptions.RegexException;
import edu.hit.ehealth.main.util.AntiCrawlerHacker;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.ErrorMessage;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class Crawler implements CrawlBehavior {

    private static ErrorMessageDao errorMessageDao;

    static {
        errorMessageDao = GlobalApplicationContext
                .getContext().getBean(ErrorMessageDao.class);
    }

    public static final int CYCLE_RETRY_TIMES = 2;

    ErrorMessage mErrorMessage = new ErrorMessage();
    private Async mAsync;
    private String currentUrl;


    public Crawler(Async async) {
        this.mAsync = async;
    }

    protected String trackPageUrl() {
        return this.currentUrl;
    }

    protected abstract void parseContent(BufferedReader content) throws Exception;

    private void logException(Throwable throwable) {
        throwable.printStackTrace();
        mErrorMessage.setCrawlPageUrl(trackPageUrl());
        mErrorMessage.setCrawlDate(Utils.getCurrentDate());
//        mErrorMessage.setExceptionMsg(ExceptionUtils.getStackTrace(throwable));
        mErrorMessage.setErrorReason(throwable.getClass().getSimpleName());
        mErrorMessage.setPrimaryId(trackPageUrl());
        mErrorMessage.setCrawlerName(getClass().getName());
        try {
            errorMessageDao.save(mErrorMessage);
            System.out.println("saved error message");
        } catch (Exception mess) {
            mess.printStackTrace();
        }
    }

    protected BufferedReader cycleReTryFetch(String url) {
        Future<Content> future = mAsync.execute(Request.Get(url), new FutureListener());
        Content content = null;
        for (int i = 0; i < CYCLE_RETRY_TIMES; i++) {
            try {
                content = future.get(5, TimeUnit.SECONDS);
                BufferedReader br = new BufferedReader(new InputStreamReader(content.asStream(), "gb2312"));
                return br;
            } catch (Exception e) {
                if (i == CYCLE_RETRY_TIMES - 1) {
                    logException(e);
                }
            }
        }
        throw new NullPointerException();
    }

    public void crawl(String url) {
        this.currentUrl = url;
        try (BufferedReader br = cycleReTryFetch(url)) {
            TimeUnit.SECONDS.sleep(AntiCrawlerHacker.randWaitSecond());
            parseContent(br);
        } catch (RegexException | InterruptedException | DataIntegrityViolationException | NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Utils.obtainLogger().error(ExceptionUtils.getStackTrace(e));
        }
    }


}
