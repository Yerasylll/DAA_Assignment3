package com.company.models.edgeRepresentations;

import com.company.models.metrics.Metrics;

import java.util.List;

public class MSTResult {
    private List<EdgeResult> mstEdges;
    private int totalCost;
    private Metrics metrics;

    public MSTResult(List<EdgeResult> mstEdges, int totalCost, Metrics metrics) {
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.metrics = metrics;
    }

    public List<EdgeResult> getMstEdges() {
        return mstEdges;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
