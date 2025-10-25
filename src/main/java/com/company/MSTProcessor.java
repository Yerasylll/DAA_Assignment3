package com.company;

import com.company.algorithms.KruskalAlgorithm;
import com.company.algorithms.PrimAlgorithm;
import com.company.models.edgeRepresentations.MSTResult;
import com.company.models.graphData.GraphData;
import com.company.models.graphData.ResultData;
import com.company.models.jsonData.JSONReader;
import com.company.models.jsonData.JSONWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MSTProcessor {

    private static final String INPUT_DIR = "src/main/jsonInputs";
    private static final String OUTPUT_DIR = "src/main/jsonOutputs";

    public static void processGraphs(String inputFilename, String outputFilename) throws IOException {
        // Build full paths
        String inputPath = INPUT_DIR + inputFilename;
        String outputPath = OUTPUT_DIR + outputFilename;

        // Create output directory if it doesn't exist
        new java.io.File(OUTPUT_DIR).mkdirs();

        System.out.println("=".repeat(60));
        System.out.println("Reading from: " + inputPath);
        System.out.println("Writing to: " + outputPath);
        System.out.println("=".repeat(60));

        // Read graphs from input file
        List<GraphData> graphs = JSONReader.readGraphs(inputPath);
        List<ResultData> results = new ArrayList<>();

        System.out.println("\nProcessing " + graphs.size() + " graph(s)...\n");

        // Process each graph
        for (GraphData graph : graphs) {
            System.out.println("Graph " + graph.getId() + ": " + graph.getDescription());
            System.out.println("  Nodes: " + graph.getNodes().size() +
                    ", Edges: " + graph.getEdges().size());

            // Run Prim's algorithm
            MSTResult primResult = PrimAlgorithm.findMST(graph.getNodes(), graph.getEdges());
            System.out.println("Prim's:    Cost=" + primResult.getTotalCost() +
                    ", Operations=" + primResult.getMetrics().getOperationsCount() +
                    ", Time=" + primResult.getMetrics().getExecutionTimeMs() + "ms");

            // Run Kruskal's algorithm
            MSTResult kruskalResult = KruskalAlgorithm.findMST(graph.getNodes(), graph.getEdges());
            System.out.println("Kruskal's: Cost=" + kruskalResult.getTotalCost() +
                    ", Operations=" + kruskalResult.getMetrics().getOperationsCount() +
                    ", Time=" + kruskalResult.getMetrics().getExecutionTimeMs() + "ms");

            results.add(new ResultData(
                    graph.getId(),
                    graph.getNodes().size(),
                    graph.getEdges().size(),
                    primResult,
                    kruskalResult
            ));

            System.out.println();
        }

        // Write results to output file
        JSONWriter.writeResults(results, outputPath);

        System.out.println("=".repeat(60));
        System.out.println("Successfully processed " + graphs.size() + " graph(s)");
        System.out.println("Results written to: " + outputPath);
        System.out.println("=".repeat(60));
    }

    public static void main(String[] args) {
        try {
            // process the generated 30 graphs
            processGraphs("assign_3_input.json", "assign_3_output.json");

        } catch (IOException e) {
            System.err.println("\nError: " + e.getMessage());
            System.err.println("\nPlease check:");
            System.err.println("1. Input file exists in: src/main/jsonInputs/");
            System.err.println("2. JSON format is correct");
            System.err.println("3. File has read permissions");
            e.printStackTrace();
        }
    }
}