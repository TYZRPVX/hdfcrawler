package edu.hit.ehealth.main.util;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Reminder {


    public static void remindCallableStatus(Class[] allClasses, ArrayList<Future<Class>> futures) {
        for (Class runClass : allClasses) {

        }
        for (Future<Class> future : futures) {
            String canonicalName = null;
            try {
                while (!future.isDone()) {
                    canonicalName = future.get().getCanonicalName();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("canonicalName = " + canonicalName);
        }
    }

}
