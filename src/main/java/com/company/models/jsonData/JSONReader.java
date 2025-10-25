package com.company.models.jsonData;

import com.company.models.edgeRepresentations.EdgeInput;
import com.company.models.graphData.GraphData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONReader {

    public static List<GraphData> readGraphs(String filename) throws IOException {
        List<GraphData> graphs = new ArrayList<>();

        StringBuilder jsonContent = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = reader.readLine()) != null) {
                jsonContent.append(line.trim());
            }
        }

        String content = jsonContent.toString();

        int graphsArrayStart = content.indexOf("\"graphs\"");
        if(graphsArrayStart == -1) {
            throw new IOException("Invalid JSON: No 'graphs' array found");
        }

        int arrayStart = content.indexOf("[", graphsArrayStart);
        int arrayEnd = findMatchingBracket(content, arrayStart);

        String graphsArrayContent = content.substring(arrayStart + 1, arrayEnd);
        List<String> graphBlocks = extractGraphBlocks(graphsArrayContent);

        for (String block : graphBlocks) {
            GraphData graph = parseGraph(block);
            graphs.add(graph);
        }

        return graphs;
    }

    private static List<String> extractGraphBlocks(String content) {
        List<String> blocks = new ArrayList<>();
        int depth = 0;
        int start = -1;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start != -1) {
                    blocks.add(content.substring(start, i + 1));
                    start = -1;
                }
            }
        }
        return blocks;
    }

    private static int findMatchingBracket(String content, int openPos) {
        int depth = 0;
        for (int i = openPos; i < content.length(); i++) {
            if (content.charAt(i) == '[') depth++;
            else if (content.charAt(i) == ']') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    private static GraphData parseGraph(String block) {
        int id = extractIntValue(block, "id");
        String description = extractStringValue(block, "description");
        if (description.isEmpty()) description = "Graph " + id;

        List<String> nodes = extractNodesArray(block);
        List<EdgeInput> edges = extractEdgesArray(block);

        return new GraphData(id, description, nodes, edges);
    }

    private static int extractIntValue(String text, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }

    private static String extractStringValue(String text, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    private static List<String> extractNodesArray(String text) {
        List<String> nodes = new ArrayList<>();
        Pattern nodesPattern = Pattern.compile("\"nodes\"\\s*:\\s*\\[([^\\]]+)\\]");
        Matcher nodesMatcher = nodesPattern.matcher(text);

        if (nodesMatcher.find()) {
            Pattern stringPattern = Pattern.compile("\"([^\"]+)\"");
            Matcher stringMatcher = stringPattern.matcher(nodesMatcher.group(1));
            while (stringMatcher.find()) {
                nodes.add(stringMatcher.group(1));
            }
        }
        return nodes;
    }

    private static List<EdgeInput> extractEdgesArray(String text) {
        List<EdgeInput> edges = new ArrayList<>();
        int edgesStart = text.indexOf("\"edges\"");
        if (edgesStart == -1) return edges;

        int arrayStart = text.indexOf("[", edgesStart);
        int arrayEnd = findMatchingBracket(text, arrayStart);
        String edgesContent = text.substring(arrayStart + 1, arrayEnd);

        List<String> edgeBlocks = extractGraphBlocks(edgesContent);
        for (String edgeBlock : edgeBlocks) {
            String from = extractStringValue(edgeBlock, "from");
            String to = extractStringValue(edgeBlock, "to");
            int weight = extractIntValue(edgeBlock, "weight");
            if (!from.isEmpty() && !to.isEmpty()) {
                edges.add(new EdgeInput(from, to, weight));
            }
        }
        return edges;
    }
}
