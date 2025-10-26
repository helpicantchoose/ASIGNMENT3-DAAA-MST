
# Assignment 3: Analytical Report on City Transportation Network Optimization

## 1. Summary of Input Data and Algorithm Results

The objective of this assignment was in finding MINIMUM SPANNING TREE in simulation of public transport ways, with implementations of both Kruskals and Prims algorithms. The performance of each algorithm was measured and collected with metrics.

The datasets were taken from an `input.json` file, and the  results were recorded in `results_summary.csv` and `output.json`. 

|Graph Name        |Algorithm|MST Cost|Vertices|Edges|Detailed Metrics                                                         | Average Execution Time (ms)       |
|------------------|---------|--------|--------|-----|-------------------------------------------------------------------------|-----------------------------------|
|small_graph       |Prim     |19      |4       |5    |{   'key_comparisons': 32 }                                              | 0,0034                            |
|small_graph       |Kruskal  |19      |4       |5    |{   'edge_sorts': 1,   'find_operations': 18,   'union_operations': 3 }  | 0,0042                            |
|medium_graph      |Prim     |48      |10      |15   |{   'key_comparisons': 200 }                                             | 0,0087                            |
|medium_graph      |Kruskal  |48      |10      |15   |{   'edge_sorts': 1,   'find_operations': 69,   'union_operations': 9 }  | 0,0036                            |
|large_graph       |Prim     |218     |25      |31   |{   'key_comparisons': 1250 }                                            | 0,0315                            |
|large_graph       |Kruskal  |218     |25      |31   |{   'edge_sorts': 1,   'find_operations': 176,   'union_operations': 24 }| 0,0065                            |
|disconnected_graph|Prim     |6       |5       |3    |{   'key_comparisons': 35 }                                              | 0,0007                            |
|disconnected_graph|Kruskal  |8       |5       |3    |{   'edge_sorts': 1,   'find_operations': 12,   'union_operations': 3 }  | 0,0005                            |

*(Note: It may differ from file in github due to being written here .)*

---

## 2. Comparison Between Prim’s and Kruskal’s Algorithms

### a. Theoretical Efficiency

The theoretical performance of these algorithms provides a baseline for our practical analysis.(It clearly shows why I shouldn't have been be an Software Engineer)

*   **Prim’s Algorithm:** When implemented with an **adjacency matrix** and a simple linear scan to find the minimum key (as done in this project), the time complexity is **O(V²)**, where V is the number of vertices. The performance is primarily dependent on the number of vertices, not the number of edges. Final complexity leaves us dominant O(V²) **.

*   **Kruskal’s Algorithm:** So the main parts of Kruskals algorithm is being sorting edges by weight and using DSU. Other things may be neglected as they're not being as heavy as both these things. Sorting algorithms in Java taking O(E Log E) time in the worst case. About DSU operation it's primitive recursive loop parent pointers may create chain O(E)*O(V)=O(EV). In the end combining it, it becomes O(ElogE+E*V)

### b. Practical Performance and Efficiency Analysis

The results from our benchmark align well with the theoretical complexities.

**As we see in the table both algorithms having similar low running time in small graphs, however as number of _Vertices_ grows we can notice that Kruskals Algorithm outperforms Prims Algorithm. With bigger graph the difference is clear, prims algorithm grows quadratically meanwhile Kruskals keeps being linear-ish**

*   **Handling of Disconnected Graphs:** There is some difference in `disconnected_graph` test.
    *   **Kruskal's algorithm correctly computed the Minimum Spanning Forest (MSF)** by finding the MST for each disconnected component, with a total cost of 8.
    *   The basic implementation of **Prim's algorithm only found the MST of the first component** it explored (the one containing the starting vertex 0), yielding an incomplete result with a cost of 6. However it's expected behaviour of PRIMS algorithm in that scenario.

---

## 3. Conclusions: Algorithm Preference Under Different Conditions

Based on the theoretical analysis and practical results, the choice between Prim's and Kruskal's algorithm is highly dependent on the characteristics of the graph.

1.  **Graph Density:**
    *   **Prefer Kruskal's for Sparse Graphs:** If the city network is sparse (i.e., E is much less than V²), Kruskal's is the superior choice due to its lower operational cost and faster execution time.
    *   **Prefer Prim's for Dense Graphs:** If the network is dense (i.e., E is close to V²), Prim's algorithm (especially the O(V²) adjacency matrix version) becomes more efficient as it avoids the expensive step of sorting a very large list of edges.

2.  **Disconnected Components:**
    *   If the network might contain isolated districts (disconnected components) and the goal is to find the most efficient road network for all districts combined (an MSF), **Kruskal's is the only reliable choice** of the two. Because as we saw Prim's algorithm failed...

In summary, for a typical real-world city map which is likely to be sparse and could potentially have disconnected zones, **Kruskal's algorithm is generally the more preferable solution.**

Limitations: Due to lack of optimisation(Because i'm bad at this) full potential of Prims algorithm could be not shown in this emperical validation.
---

## 4. References

https://www.w3schools.com/dsa/dsa_algo_mst_kruskal.php

https://www.w3schools.com/dsa/dsa_algo_mst_prim.php

https://www.quora.com/How-do-I-overcome-the-regret-of-choosing-the-wrong-major
