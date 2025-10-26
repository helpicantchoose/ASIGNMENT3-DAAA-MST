package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.PerformanceTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PrimsAlgorithmTest {
    private Graph connectedGraph, disconnectedGraph;
    private PerformanceTracker tracker;

    @BeforeEach
    void setUp() {
        connectedGraph = new Graph(4);
        connectedGraph.addEdge(0, 1, 10);
        connectedGraph.addEdge(0, 2, 6);
        connectedGraph.addEdge(0, 3, 5);
        connectedGraph.addEdge(1, 3, 15);
        connectedGraph.addEdge(2, 3, 4);

        disconnectedGraph = new Graph(5);
        disconnectedGraph.addEdge(0, 1, 1);
        disconnectedGraph.addEdge(1, 2, 5); // Connect 1 to 2 for a component of 3 vertices
        disconnectedGraph.addEdge(3, 4, 2);
        tracker = new PerformanceTracker();
    }

    @Test
    void testMSTonConnectedGraph() {
        List<Edge> mst = PrimsAlgorithm.findMST(connectedGraph, tracker);
        assertEquals(3, mst.size());
        assertEquals(19, mst.stream().mapToInt(e -> e.weight).sum());
        assertFalse(tracker.getOperations().isEmpty(), "Operations map should not be empty");
    }

    @Test
    void testMSTonDisconnectedGraph() {
        List<Edge> mst = PrimsAlgorithm.findMST(disconnectedGraph, tracker);
        assertEquals(2, mst.size());
        assertEquals(6, mst.stream().mapToInt(e -> e.weight).sum());
    }
}
