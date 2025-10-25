package com.company.models.jsonData;

import com.company.models.edgeRepresentations.EdgeResult;
import com.company.models.edgeRepresentations.MSTResult;
import com.company.models.graphData.ResultData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONWriter {

    public static void writeResults(List<ResultData> results, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("{\n  \"results\": [\n");

        for (int i = 0; i < results.size(); i++) {
            ResultData result = results.get(i);
            writer.write("    {\n");
            writer.write("      \"graph_id\": " + result.getGraphId() + ",\n");
            writer.write("      \"input_stats\": {\n");
            writer.write("        \"vertices\": " + result.getVertices() + ",\n");
            writer.write("        \"edges\": " + result.getInputEdges() + "\n");
            writer.write("      },\n");

            writeAlgorithmResult(writer, "prim", result.getPrimResult());
            writer.write(",\n");
            writeAlgorithmResult(writer, "kruskal", result.getKruskalResult());
            writer.write("\n    }");
            if (i < results.size() - 1) writer.write(",");
            writer.write("\n");
        }

        writer.write("  ]\n}\n");
        writer.close();
    }

    private static void writeAlgorithmResult(FileWriter writer, String algName, MSTResult result) throws IOException {
        writer.write("      \"" + algName + "\": {\n");
        writer.write("        \"mst_edges\": [\n");

        List<EdgeResult> edges = result.getMstEdges();
        for (int i = 0; i < edges.size(); i++) {
            EdgeResult edge = edges.get(i);
            writer.write("          {\"from\": \"" + edge.getFrom() + "\", " +
                    "\"to\": \"" + edge.getTo() + "\", " +
                    "\"weight\": " + edge.getWeight() + "}");
            if (i < edges.size() - 1) writer.write(",");
            writer.write("\n");
        }

        writer.write("        ],\n");
        writer.write("        \"total_cost\": " + result.getTotalCost() + ",\n");
        writer.write("        \"operations_count\": " + result.getMetrics().getOperationsCount() + ",\n");
        writer.write("        \"execution_time_ms\": " + result.getMetrics().getExecutionTimeMs() + "\n");
        writer.write("      }");
    }
}
