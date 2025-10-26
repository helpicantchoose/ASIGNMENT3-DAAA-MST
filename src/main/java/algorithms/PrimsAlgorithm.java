package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.PerformanceTracker;
import java.util.ArrayList;
import java.util.List;

public class PrimsAlgorithm {
    public static List<Edge> findMST(Graph graph, PerformanceTracker tracker) {
        tracker.start();
        int V = graph.getNumVertices();
        int[][] adjMatrix = graph.toAdjacencyMatrix();
        List<Edge> mstEdges = new ArrayList<>();
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] inMST = new boolean[V];

        for (int i = 0; i < V; i++) {
            key[i] = Integer.MAX_VALUE;
            inMST[i] = false;

            parent[i] = -1;
        }


        key[0] = 0;


        for (int count = 0; count < V; count++) {
            int u = findMinKeyVertex(key, inMST, V, tracker);


            if (u == -1) {
                break;
            }

            inMST[u] = true;

            for (int v = 0; v < V; v++) {
                tracker.incrementOperation("key_comparisons");
                if (adjMatrix[u][v] != 0 && !inMST[v] && adjMatrix[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = adjMatrix[u][v];
                }
            }
        }


        for (int i = 1; i < V; i++) {
            if (parent[i] != -1) {
                mstEdges.add(new Edge(parent[i], i, adjMatrix[i][parent[i]]));
            }
        }

        tracker.stop();
        return mstEdges;
    }

    private static int findMinKeyVertex(int[] key, boolean[] inMST, int V, PerformanceTracker tracker) {
        int minKey = Integer.MAX_VALUE;
        int minIndex = -1;
        for (int v = 0; v < V; v++) {
            tracker.incrementOperation("key_comparisons");
            if (!inMST[v] && key[v] < minKey) {
                minKey = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }
}