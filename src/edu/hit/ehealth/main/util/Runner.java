package edu.hit.ehealth.main.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p> 通过反射调用每个类（爬虫、监控器）的 {@code run()}，注意方法必须是无参的，否则会在产生Runtime错误，难以调试
 *
 */

public class Runner {

    public static final String RUN_METHOD_NAME = "run";

    public static void run(Class runnerClass) {
        if (Utils.SHOULD_PRT) {
            System.out.println("Class: " + runnerClass.getSimpleName());
        }
        Method runMethod = null;
        try {
            runMethod = runnerClass.getMethod(RUN_METHOD_NAME);
            runMethod.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
