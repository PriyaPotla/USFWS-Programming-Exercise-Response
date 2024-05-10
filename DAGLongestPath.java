import java.util.*;

class Vertex {
    long id;

    public Vertex(long id) {
        this.id = id;
    }
}

class Edge {
    Vertex from;
    Vertex to;
    long weight;

    public Edge(Vertex from, Vertex to, long weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

public class DAGLongestPath {
    List<Edge> edges;
    int numVertices;

    public DAGLongestPath(int numVertices) {
        this.numVertices = numVertices;
        edges = new ArrayList<>();
    }

    public void addEdge(Vertex from, Vertex to, long weight) {
        edges.add(new Edge(from, to, weight));
    }

    // Perform Depth-First Search (DFS) to identify reachable vertices
    private void dfs(Vertex vertex, boolean[] visited) {
        visited[(int) vertex.id] = true;
        for (Edge edge : edges) {
            if (edge.from == vertex && !visited[(int) edge.to.id]) {
                dfs(edge.to, visited);
            }
        }
    }

    public List<Vertex> longestPath(Vertex start) {
        boolean[] reachable = new boolean[numVertices];
        dfs(start, reachable);

        long[] distances = new long[numVertices];
        Arrays.fill(distances, Long.MIN_VALUE);
        distances[(int) start.id] = 0;

        // Bellman-Ford algorithm
        for (int i = 0; i < numVertices - 1; i++) {
            for (Edge edge : edges) {
                if (reachable[(int) edge.from.id] && distances[(int) edge.from.id] != Long.MIN_VALUE &&
                        distances[(int) edge.from.id] + edge.weight > distances[(int) edge.to.id]) {
                    distances[(int) edge.to.id] = distances[(int) edge.from.id] + edge.weight;
                }
            }
        }

        // Check for negative cycles
        for (int i = 0; i < numVertices - 1; i++) {
            for (Edge edge : edges) {
                if (reachable[(int) edge.from.id] && distances[(int) edge.from.id] != Long.MIN_VALUE &&
                        distances[(int) edge.from.id] + edge.weight > distances[(int) edge.to.id]) {
                    // Negative cycle detected, return empty list
                    return new ArrayList<>();
                }
            }
        }

        // Reconstruct the longest path
        List<Vertex> longestPath = new ArrayList<>();
        Vertex current = start;
        while (current != null) {
            longestPath.add(current);
            long nextDistance = Long.MIN_VALUE;
            Vertex nextVertex = null;
            for (Edge edge : edges) {
                if (reachable[(int) edge.from.id] && edge.from == current && distances[(int) edge.to.id] > nextDistance) {
                    nextDistance = distances[(int) edge.to.id];
                    nextVertex = edge.to;
                }
            }
            current = nextVertex;
        }
        return longestPath;
    }

    public static void main(String[] args) {
        int numVertices = 4;
        DAGLongestPath dag = new DAGLongestPath(numVertices);
        Vertex A = new Vertex(0);
        Vertex B = new Vertex(1);
        Vertex C = new Vertex(2);
        Vertex D = new Vertex(3);

        dag.addEdge(A, B, 1);
        dag.addEdge(A, C, 2);
        dag.addEdge(B, C, -3); // Negative weight
        dag.addEdge(B, D, 4);
        dag.addEdge(C, D, 5);

        Vertex startVertex = B;
        List<Vertex> longestPath = dag.longestPath(startVertex);
        if (longestPath.isEmpty()) {
            System.out.println("Negative cycle detected, no longest path exists.");
        } else {
            System.out.println("Longest path from vertex " + startVertex.id + ":");
            for (Vertex vertex : longestPath) {
                System.out.print(vertex.id + " ");
            }
        }
    }
}
