package edu.hit.ehealth.test;

import java.util.concurrent.TimeUnit;

public class Task1 {
    public static void run() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Task1 finished");
    }
}
