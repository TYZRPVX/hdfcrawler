package edu.hit.ehealth.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestCallable {

    public static void main(String[] args) {

        new TestCallable().test();

    }

    private void test() {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        List<Callable<String>> callables = productCallableList();
        ArrayList<Future<String>> futures = new ArrayList<>();
        for (Callable<String> callable : callables) {
            Future<String> future = threadPool.submit(callable);
            futures.add(future);
        }
        System.out.println("Does pool.submit() need time ?");


        for (Future<String> future : futures) {
            String order = getFuture(future);
            System.out.println("getFuture(),  order = " + order);

        }

        System.out.println("Does future.get() need time ?");
    }

    private String getFuture(Future<String> future) {
        try {
            String order = future.get();
            return order;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Can't get anything from future...");
    }

    private List<Callable<String>> productCallableList() {
        int LIST_LEN = 5;
        ArrayList<Callable<String>> callables = new ArrayList<Callable<String>>();
        for (int i = 0; i < LIST_LEN; i++) {
            final Integer order = i;
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    TimeUnit.SECONDS.sleep(order);
                    System.out.println("order.toString() = " + order.toString());
                    return order.toString();
                }
            };
            callables.add(callable);
        }
        return callables;
    }
}
