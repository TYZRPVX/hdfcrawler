package edu.hit.ehealth.test;

import java.util.concurrent.TimeUnit;

public class Task2 {

    public static void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Task2 finished");
    }
}
