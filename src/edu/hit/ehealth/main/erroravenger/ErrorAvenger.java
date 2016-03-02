package edu.hit.ehealth.main.erroravenger;

import edu.hit.ehealth.main.util.Resource;
import edu.hit.ehealth.main.vo.ErrorMessage;
import org.apache.http.client.fluent.Async;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ErrorAvenger {

    public static final String CRAWL_METHOD_NAME = "crawl";
    private static ErrorAvenger errorAvenger = new ErrorAvenger();
    private static ErrorMessage errorMessage;

    private Async avengerAsync;

    private ErrorAvenger() {
        avengerAsync = Resource.obtainAsync();
    }


    public static ErrorAvenger getInstance(ErrorMessage em) {
        errorMessage = em;
        return errorAvenger;
    }

    private String getCrawlerName() {
        return errorMessage.getCrawlerName();
    }

    private String getExpType() {
        return errorMessage.getErrorReason();
    }

    private Async getAvengerAsync() {
        return avengerAsync;
    }

    private String getErrorUrl() {
        return errorMessage.getCrawlPageUrl();
    }

    private boolean needToAvenge(String ex) {
        //all
        return true;
    }


    public void reflectCrawlMethod() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!needToAvenge(getExpType())) {
            System.out.println("No need to crawl again.");
            return;
        }

        Class<?> crawlerClass = Class.forName(getCrawlerName());
        Constructor ctor = crawlerClass.getConstructor(Async.class);
        Object crawler = ctor.newInstance(getAvengerAsync());
        Method mtd = crawlerClass.getMethod(CRAWL_METHOD_NAME, String.class);
        mtd.invoke(crawler, getErrorUrl());
    }

}
