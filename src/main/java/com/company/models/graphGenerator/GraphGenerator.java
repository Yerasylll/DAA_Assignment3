package com.company.models.graphGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GraphGenerator {

    private static class Edge {
        String from, to;
        int weight;
        Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    private static class Graph {
        int id;
        String description;
        List<String> nodes;
        List<Edge> edges;
        Graph(int id, String description, List<String> nodes, List<Edge> edges) {
            this.id = id;
            this.description = description;
            this.nodes = nodes;
            this.edges = edges;
        }
    }

    public static void generateAllVariants() throws IOException {
        List<Graph> graphs = new ArrayList<>();
        int graphId = 1;

        // Small (5 graphs)
        int[] smallSizes = {5, 10, 15, 20, 30};
        double[] smallDensities = {0.3, 0.5, 0.4, 0.6, 0.5};
        for (int i = 0; i < smallSizes.length; i++) {
            graphs.add(generateGraph(graphId++, smallSizes[i], smallDensities[i]));
        }

        // Medium (10 graphs)
        int[] mediumSizes = {50, 75, 100, 125, 150, 175, 200, 225, 275, 300};
        double[] mediumDensities = {0.2, 0.3, 0.25, 0.35, 0.3, 0.4, 0.35, 0.3, 0.25, 0.4};
        for (int i = 0; i < mediumSizes.length; i++) {
            graphs.add(generateGraph(graphId++, mediumSizes[i], mediumDensities[i]));
        }

        // Large (10 graphs)
        int[] largeSizes = {350, 400, 450, 500, 600, 700, 800, 900, 950, 1000};
        double[] largeDensities = {0.15, 0.2, 0.18, 0.22, 0.2, 0.25, 0.2, 0.18, 0.15, 0.3};
        for (int i = 0; i < largeSizes.length; i++) {
            graphs.add(generateGraph(graphId++, largeSizes[i], largeDensities[i]));
        }

        // Extra (5 graphs)
        int[] extraSizes = {1500, 2000, 2250, 2750, 3000};
        double[] extraDensities = {0.1, 0.12, 0.15, 0.1, 0.2};
        for (int i = 0; i < extraSizes.length; i++) {
            graphs.add(generateGraph(graphId++, extraSizes[i], extraDensities[i]));
        }

        writeToJSON(graphs, "src/main/jsonInputs/assign_3_input.json");
        System.out.println("Generated 30 graphs in: src/main/jsonInputs/assign_3_input.json");
    }

    private static Graph generateGraph(int id, int numNodes, double density) {
        Random random = new Random(id * 100L);
        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) nodes.add("N" + i);

        List<Edge> edges = new ArrayList<>();
        Set<String> edgeSet = new HashSet<>();

        // Create spanning tree
        for (int i = 1; i < numNodes; i++) {
            int parent = random.nextInt(i);
            String from = nodes.get(Math.min(parent, i));
            String to = nodes.get(Math.max(parent, i));
            edges.add(new Edge(from, to, random.nextInt(100) + 1));
            edgeSet.add(from + "-" + to);
        }

        // Add extra edges
        int maxEdges = (numNodes * (numNodes - 1)) / 2;
        int targetEdges = Math.min(maxEdges, (int)(maxEdges * density));
        int attempts = 0;

        while (edges.size() < targetEdges && attempts < targetEdges * 10) {
            int i = random.nextInt(numNodes);
            int j = random.nextInt(numNodes);
            if (i != j) {
                String from = nodes.get(Math.min(i, j));
                String to = nodes.get(Math.max(i, j));
                if (!edgeSet.contains(from + "-" + to)) {
                    edges.add(new Edge(from, to, random.nextInt(100) + 1));
                    edgeSet.add(from + "-" + to);
                }
            }
            attempts++;
        }

        return new Graph(id, "Graph with " + numNodes + " nodes", nodes, edges);
    }

    private static void writeToJSON(List<Graph> graphs, String filename) throws IOException {
        new java.io.File(filename).getParentFile().mkdirs();
        FileWriter w = new FileWriter(filename);
        w.write("{\n  \"graphs\": [\n");
        for (int i = 0; i < graphs.size(); i++) {
            Graph g = graphs.get(i);
            w.write("    {\n      \"id\": " + g.id + ",\n");
            w.write("      \"description\": \"" + g.description + "\",\n");
            w.write("      \"nodes\": [");
            for (int j = 0; j < g.nodes.size(); j++) {
                w.write("\"" + g.nodes.get(j) + "\"");
                if (j < g.nodes.size() - 1) w.write(", ");
            }
            w.write("],\n      \"edges\": [\n");
            for (int j = 0; j < g.edges.size(); j++) {
                Edge e = g.edges.get(j);
                w.write("        {\"from\": \"" + e.from + "\", \"to\": \"" + e.to + "\", \"weight\": " + e.weight + "}");
                if (j < g.edges.size() - 1) w.write(",");
                w.write("\n");
            }
            w.write("      ]\n    }");
            if (i < graphs.size() - 1) w.write(",");
            w.write("\n");
        }
        w.write("  ]\n}\n");
        w.close();
    }

    public static void main(String[] args) {
        try {
            generateAllVariants();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
