package edu.hit.ehealth.main.crawler.basestruct;

import edu.hit.ehealth.main.dao.ErrorMessageDao;
import edu.hit.ehealth.main.dao.GlobalApplicationContext;
import edu.hit.ehealth.main.util.AntiCrawlerHacker;
import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.util.Utils;
import edu.hit.ehealth.main.vo.ErrorMessage;
import org.apache.http.client.fluent.Async;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public abstract class DynamicContentCrawler implements CrawlBehavior {


    ErrorMessage mErrorMessage = new ErrorMessage();
    private WebDriver webDriver;
    private String currentUrl;
    private ErrorMessageDao errorMessageDao
            = GlobalApplicationContext.getContext().getBean(ErrorMessageDao.class);

    public DynamicContentCrawler() {
        this.webDriver = Resource.getWebDriver();
    }

    public DynamicContentCrawler(Async asyncForError) {
        this();
    }

    protected String trackPageUrl() {
        return this.currentUrl;
    }

    protected String getPageSrc(String url) {
        this.webDriver.get(url);
        String pageSource = this.webDriver.getPageSource();
        if (Utils.SHOULD_PRT) System.out.println("pageSource = " + pageSource);
        return pageSource;
    }

    protected abstract void parseContent(String pageSrc) throws Exception;

    public void crawl(String url) {
        this.currentUrl = url;
        try {
            TimeUnit.SECONDS.sleep(AntiCrawlerHacker.randWaitSecond());
            String pageSrc = getPageSrc(url);
            parseContent(pageSrc);
        } catch (Exception e) {
            logException(e);
        }
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
