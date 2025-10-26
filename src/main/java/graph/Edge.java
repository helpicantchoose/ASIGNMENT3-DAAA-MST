package graph;


public class Edge implements Comparable<Edge> {
    public final int src;
    public final int dest;
    public final int weight;

    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }


    @Override
    public int compareTo(Edge other) {
        return this.weight - other.weight;
    }

    @Override
    public String toString() {
        return String.format("(%d -- %d, w:%d)", src, dest, weight);
    }
}
