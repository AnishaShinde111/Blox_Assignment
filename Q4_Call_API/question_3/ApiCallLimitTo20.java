package com.company.question_3;

public class ApiCallLimitTo20 {
    private int tokens = 15;
    private final int maxTokens = 15;
    private long lastRefillTime = System.currentTimeMillis();

    public synchronized boolean allowRequest() {
        refillTokens();

        if (tokens > 0) {
            tokens--;
            return true;
        } else {
            penalize();
            return false;
        }
    }

    private void refillTokens() {
        long now = System.currentTimeMillis();
        long timeElapsed = now - lastRefillTime;

        if (timeElapsed > 60000) { // 1 minute
            tokens = maxTokens;
            lastRefillTime = now;
        }
    }

    private void penalize() {
        lastRefillTime += 60000; // Add penalty of 1 minute
    }
}
