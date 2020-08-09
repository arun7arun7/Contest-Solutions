package ds.graph.DAG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class TopologicalSort {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        Graph graph = new Graph(numCourses);
        for (int i = 0; i < prerequisites.length; i++) {
            graph.addEdge(prerequisites[i][1], prerequisites[i][0]);
        }
        List<Integer> topologicalSort = graph.findTopologicalSort();
        int[] res = new int[topologicalSort.size()];
        int c = 0;
        for (int node : topologicalSort) {
            res[c++] = node;
        }
        return res;
    }

    class Graph {
        int n;
        List<Integer>[] neighbors;
        LinkedList<Integer> topologicalSort = new LinkedList();

        public Graph(int n) {
            this.n = n;
            neighbors = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                neighbors[i] = new ArrayList();
            }
        }

        public void addEdge(int src, int dest) {
            neighbors[src].add(dest);
        }

        public List<Integer> findTopologicalSort() {
            boolean[] visited = new boolean[n];
            boolean[] recStack = new boolean[n];

            for (int i = 0; i < n; i++) {
                if (!visited[i] && dfs(i, visited, recStack)) {
                    return new LinkedList<Integer>();
                }
            }
            return topologicalSort;
        }

        private boolean dfs(int root, boolean[] visited, boolean[] recStack) {
            if (recStack[root]) {
                return true;
            }
            if (visited[root]) {
                return false;
            }
            visited[root] = true;
            recStack[root] = true;
            for(int neighbor : neighbors[root]) {
                if (dfs(neighbor, visited, recStack)) {
                    return true;
                }
            }
            recStack[root] = false;
            topologicalSort.addFirst(root);
            return false;
        }
    }
}
