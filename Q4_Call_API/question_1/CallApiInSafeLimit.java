package com.company.question_1;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

// This class will demonstrate a way to call an API within a safe limit using Leaky Bucket.
// Solution to sub question 1
public class CallApiInSafeLimit {

    private final int maxCalls;
    private final long timeLimitMiliSeconds;
    private final Semaphore semaphore;

    public CallApiInSafeLimit(int maxCalls, long timeLimitMiliSeconds) {
        this.maxCalls = maxCalls;
        this.timeLimitMiliSeconds = timeLimitMiliSeconds;
        this.semaphore = new Semaphore(maxCalls);

        // Periodically refill permits
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> semaphore.release(maxCalls - semaphore.availablePermits()),
                timeLimitMiliSeconds, timeLimitMiliSeconds, TimeUnit.MILLISECONDS);
    }

    public String callApiSafely(String input) throws InterruptedException {
        semaphore.acquire();
        return call_me(input);
    }

    private String call_me(String input) {
        // Simulating the actual API call
        System.out.println("API called with input: " + input);
        return "Response for " + input;
    }

    public static void main(String[] args) throws InterruptedException {
        CallApiInSafeLimit rateLimiter = new CallApiInSafeLimit(15, 60000);

        // Simulating 15 calls
        for (int i = 1; i <= 15; i++) {
            final int callNumber = i;
            new Thread(() -> {
                try {
                    System.out.println(rateLimiter.callApiSafely("Input" + callNumber));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
