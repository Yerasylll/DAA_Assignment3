package com.company.models.metrics;

public class Metrics {
    private int operationsCount;
    private long executionTimeMs;

    public Metrics() {
        this.operationsCount = 0;
        this.executionTimeMs = 0;
    }

    public int getOperationsCount() {
        return operationsCount;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void incOperations() {
        operationsCount++;
    }
    public void setExecutionTimeMs(long time) {
        this.executionTimeMs = time;
    }
}
