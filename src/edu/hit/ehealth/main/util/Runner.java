package edu.hit.ehealth.main.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
