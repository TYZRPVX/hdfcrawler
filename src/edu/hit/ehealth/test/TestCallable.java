package edu.hit.ehealth.test;

import java.util.concurrent.*;

public class TestCallable {

    public static void main(String[] args) {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("callable");
                return "FINISH";
            }
        };

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        Future<String> future = threadPool.submit(callable);
//        while (!future.isDone()) {
//            String s = null;
//            try {
//                s = future.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//            System.out.println("s = " + s);
//        }
//        try {
//            String s = future.get();
//            System.out.println("s = " + s);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        System.out.println("go");
    }
}
