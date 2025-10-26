package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.PerformanceTracker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalsAlgorithm {
    private static class DSU {
        int[] parent;
        PerformanceTracker tracker;

        DSU(int n, PerformanceTracker tracker) {
            this.parent = new int[n];
            this.tracker = tracker;
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int i) {
            tracker.incrementOperation("find_operations");
            if (parent[i] == i) return i;
            return find(parent[i]);
        }

        void union(int x, int y) {
            tracker.incrementOperation("union_operations");
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) parent[rootX] = rootY;
        }
    }

    public static List<Edge> findMST(Graph graph, PerformanceTracker tracker) {
        tracker.start();
        int V = graph.getNumVertices();
        List<Edge> allEdges = graph.getAllEdges();
        List<Edge> mstEdges = new ArrayList<>();

        Collections.sort(allEdges);
        tracker.incrementOperation("edge_sorts");

        DSU dsu = new DSU(V, tracker);

        for (Edge edge : allEdges) {
            if (dsu.find(edge.src) != dsu.find(edge.dest)) {
                mstEdges.add(edge);
                dsu.union(edge.src, edge.dest);
                if (mstEdges.size() == V - 1) break;
            }
        }
        tracker.stop();
        return mstEdges;
    }
}
