package edu.hit.ehealth.main.crawler.basestruct;

import edu.hit.ehealth.main.dao.ErrorMessageDao;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.define.RegexException;
import edu.hit.ehealth.main.util.AntiCrawlerHacker;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.ErrorMessage;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class Crawler implements CrawlBehavior {

    public static final int CYCLE_RETRY_TIMES = 2;
    private static ErrorMessageDao errorMessageDao;

    static {
        errorMessageDao = GlobalApplicationContext
                .getContext().getBean(ErrorMessageDao.class);
    }

    ErrorMessage mErrorMessage = new ErrorMessage();
    private Async mAsync;
    private String currentUrl;


    public Crawler(Async async) {
        this.mAsync = async;
    }

    /**
     * @return 当前爬取的网页url
     */
    protected String trackPageUrl() {
        return this.currentUrl;
    }

    protected abstract void parseContent(BufferedReader content) throws Exception;

    /**
     * {@link #crawl(String)} 中捕获到的异常部分会被记录到数据库中
     *
     * @param throwable
     */
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


    /**
     * 多次轮询url，获取网页源代码，并分发给子类爬虫的 {@link #parseContent(BufferedReader)}
     *
     * @param url 将要爬取网页链接
     */
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
