package com.company.algorithms;

import com.company.models.edgeRepresentations.EdgeInput;
import com.company.models.edgeRepresentations.EdgeResult;
import com.company.models.edgeRepresentations.MSTResult;
import com.company.models.metrics.Metrics;

import java.util.*;

public class PrimAlgorithm {

    public static MSTResult findMST(List<String> nodes, List<EdgeInput> edges) {
        Metrics metrics = new Metrics();
        long startTime = System.nanoTime();

        // build adjacency list
        Map<String, List<EdgeInput>> adjacencyList = new HashMap<>();
        for (String node : nodes) {
            adjacencyList.put(node, new ArrayList<>());
        }

        for (EdgeInput edge : edges) {
            adjacencyList.get(edge.getFrom()).add(edge);
            adjacencyList.get(edge.getTo()).add(new EdgeInput(edge.getTo(), edge.getFrom(), edge.getWeight()));
            metrics.incOperations();
        }

        // prim algorithm
        Set<String> visited = new HashSet<>();
        PriorityQueue<EdgeInput> pq = new PriorityQueue<>((a, b) -> {
            metrics.incOperations();
            return Integer.compare(a.getWeight(), b.getWeight());
        });

        List<EdgeResult> mstEdges = new ArrayList<>();
        int totalCost = 0;

        // start from first node
        String startNode = nodes.get(0);
        visited.add(startNode);
        pq.addAll(adjacencyList.get(startNode));

        while(!pq.isEmpty() && visited.size() < nodes.size()) {
            EdgeInput edge = pq.poll();
            metrics.incOperations();

            String nextNode = visited.contains(edge.getFrom()) ? edge.getTo() : edge.getFrom();

            if(visited.contains(nextNode)) {
                continue;
            }

            visited.add(nextNode);
            mstEdges.add(new EdgeResult(edge.getFrom(), edge.getTo(), edge.getWeight()));
            totalCost += edge.getWeight();

            for(EdgeInput neighbor : adjacencyList.get(nextNode)) {
                if(!visited.contains(neighbor.getTo())) {
                    pq.add(neighbor);
                }
                metrics.incOperations();
            }
        }

        long endTime = System.nanoTime();
        metrics.setExecutionTimeMs((endTime - startTime) / 1_000_000);

        return new MSTResult(mstEdges, totalCost, metrics);
    }
}
