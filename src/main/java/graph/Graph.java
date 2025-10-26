package graph;

import java.util.ArrayList;
import java.util.List;


public class Graph {
    private final int V;
    private final List<Edge> edges;

    public Graph(int V) {
        this.V = V;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int src, int dest, int weight) {
        Edge edge = new Edge(src, dest, weight);
        edges.add(edge);
    }

    public int getNumVertices() {
        return V;
    }

    public List<Edge> getAllEdges() {
        return new ArrayList<>(edges);
    }


    public int[][] toAdjacencyMatrix() {
        int[][] matrix = new int[V][V];
        for (Edge edge : edges) {
            matrix[edge.src][edge.dest] = edge.weight;
            matrix[edge.dest][edge.src] = edge.weight;
        }
        return matrix;
    }
}
