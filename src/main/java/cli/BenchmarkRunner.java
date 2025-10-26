package cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import algorithms.KruskalsAlgorithm;
import algorithms.PrimsAlgorithm;
import graph.Edge;
import graph.Graph;
import metrics.PerformanceTracker;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BenchmarkRunner {
    static class GraphData { int vertices; List<List<Integer>> edges; }
    static class InputData { Map<String, GraphData> graphs; }

    private static final int BENCHMARK_RUNS = 1000;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar <jar-file> <input.json> <output.json>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];
        String csvFile = "results_summary.csv";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Map<String, Object>> allResults = new LinkedHashMap<>();

        try (FileReader reader = new FileReader(inputFile)) {
            Type inputType = new TypeToken<InputData>() {}.getType();
            InputData inputData = gson.fromJson(reader, inputType);

            for (Map.Entry<String, GraphData> entry : inputData.graphs.entrySet()) {
                String graphName = entry.getKey();
                GraphData data = entry.getValue();
                Graph graph = new Graph(data.vertices);
                data.edges.forEach(e -> graph.addEdge(e.get(0), e.get(1), e.get(2)));

                System.out.println("--- Processing Graph: " + graphName + " ---");
                Map<String, Object> graphResults = new LinkedHashMap<>();


                long primsTotalTimeNanos = 0;
                Map<String, Long> primsOperations = null;
                List<Edge> primsMstResult = null;

                for (int i = 0; i < BENCHMARK_RUNS; i++) {
                    PerformanceTracker primsTracker = new PerformanceTracker();
                    primsMstResult = PrimsAlgorithm.findMST(graph, primsTracker);


                    primsTotalTimeNanos += primsTracker.getExecutionTimeNanos();

                    if (primsOperations == null) {
                        primsOperations = primsTracker.getOperations();
                    }
                }
                double primsAvgTimeMillis = (double) primsTotalTimeNanos / BENCHMARK_RUNS / 1_000_000.0;
                Map<String, Object> primsResultMap = createResultMap(graph, primsMstResult, primsAvgTimeMillis, primsOperations);
                graphResults.put("Prim", primsResultMap);
                printResultSummary("Prim", primsResultMap);



                long kruskalsTotalTimeNanos = 0;
                Map<String, Long> kruskalsOperations = null;
                List<Edge> kruskalsMstResult = null;

                for (int i = 0; i < BENCHMARK_RUNS; i++) {
                    PerformanceTracker kruskalsTracker = new PerformanceTracker();
                    kruskalsMstResult = KruskalsAlgorithm.findMST(graph, kruskalsTracker);


                    kruskalsTotalTimeNanos += kruskalsTracker.getExecutionTimeNanos();

                    if (kruskalsOperations == null) {
                        kruskalsOperations = kruskalsTracker.getOperations();
                    }
                }
                double kruskalsAvgTimeMillis = (double) kruskalsTotalTimeNanos / BENCHMARK_RUNS / 1_000_000.0;
                Map<String, Object> kruskalsResultMap = createResultMap(graph, kruskalsMstResult, kruskalsAvgTimeMillis, kruskalsOperations);
                graphResults.put("Kruskal", kruskalsResultMap);
                printResultSummary("Kruskal", kruskalsResultMap);

                allResults.put(graphName, graphResults);
                System.out.println("Completed: " + graphName + " (" + BENCHMARK_RUNS + " runs per algorithm)\n");
            }


            try (FileWriter writer = new FileWriter(outputFile)) {
                gson.toJson(allResults, writer);
                System.out.println("Detailed results written to " + outputFile);
            }

            writeResultsToCsv(allResults, csvFile, gson);
            System.out.println("Summary results written to " + csvFile);

        } catch (IOException e) { e.printStackTrace(); }
    }


    private static Map<String, Object> createResultMap(Graph g, List<Edge> mst, double avgTimeMillis, Map<String, Long> operations) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total_cost", mst.stream().mapToInt(e -> e.weight).sum());
        result.put("original_vertices", g.getNumVertices());
        result.put("original_edges", g.getAllEdges().size());
        result.put("detailed_metrics", operations);
        result.put("average_execution_time_ms", avgTimeMillis);
        result.put("mst_edges", mst.toString());
        return result;
    }


    private static void printResultSummary(String algorithmName, Map<String, Object> results) {
        System.out.printf("  %-10s -> Cost: %-5d | Metrics: %-40s | Avg Time: %.4f ms\n",
                algorithmName,
                (int) results.get("total_cost"),
                results.get("detailed_metrics").toString(),
                (double) results.get("average_execution_time_ms")
        );
    }


    private static void writeResultsToCsv(Map<String, Map<String, Object>> allResults, String filename, Gson gson) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Graph Name,Algorithm,MST Cost,Vertices,Edges,Detailed Metrics,Average Execution Time (ms)");

            for (Map.Entry<String, Map<String, Object>> graphEntry : allResults.entrySet()) {
                String graphName = graphEntry.getKey();
                for (Map.Entry<String, Object> algoEntry : graphEntry.getValue().entrySet()) {
                    String algoName = algoEntry.getKey();
                    Map<String, Object> metrics = (Map<String, Object>) algoEntry.getValue();
                    String metricsJson = gson.toJson(metrics.get("detailed_metrics"));

                    writer.printf("%s,%s,%d,%d,%d,\"%s\",%.4f\n",
                            graphName,
                            algoName,
                            metrics.get("total_cost"),
                            metrics.get("original_vertices"),
                            metrics.get("original_edges"),
                            metricsJson.replace("\"", "'"),
                            metrics.get("average_execution_time_ms")
                    );
                }
            }
        }
    }
}
