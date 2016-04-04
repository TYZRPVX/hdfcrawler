package edu.hit.ehealth.main.erroravenger;

import edu.hit.ehealth.main.vo.ErrorMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 错误恢复程序应该为常驻服务
 * 反复检测「错误信息表」中存在的超时错误，
 * 通过反射找到爬虫重新爬取，但数据新旧就难以保障，需要折中
 */

public class ErrorWiper {

    private static ErrorDBReader reader = new ErrorDBReader();

    private static ErrorWiper INSTANCE;

    private ErrorWiper() {
    }

    public static ErrorWiper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ErrorWiper();
        }
        return INSTANCE;
    }

    private static void reCrawl(ErrorMessage errorMessage) {
        try {
            ErrorAvenger.getInstance(errorMessage).
                    reflectCrawlMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run() {
        try {
            ErrorWiper.getInstance().wiperAllError();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        run();
    }

    private void wiperAllError() throws ExecutionException, InterruptedException {
        Iterable<ErrorMessage> allErrorMsg = reader.getAllErrorMsg();
        List<ErrorMessage> dupErrorMsg = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        HashSet<Future<String>> futures = new HashSet<>();
        for (ErrorMessage errorMessage : allErrorMsg) {
            dupErrorMsg.add(errorMessage);
            reader.deleteErrorMsg(errorMessage);
        }
        for (ErrorMessage errorMessage : dupErrorMsg) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    reCrawl(errorMessage);
                }
            };
            threadPool.execute(runnable);
        }
    }
}
