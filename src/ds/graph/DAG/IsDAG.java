package ds.graph.DAG;

import java.util.ArrayList;
import java.util.List;

class IsDAG {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        Graph graph = new Graph(numCourses);
        for (int i = 0; i < prerequisites.length; i++) {
            graph.addEdge(prerequisites[i][1], prerequisites[i][0]);
        }
        return !graph.hasCycle();
    }

    class Graph {
        int n;
        List<Integer>[] neighbors;

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

        public boolean hasCycle() {
            boolean[] visited = new boolean[n];
            boolean[] recStack = new boolean[n];
            for (int i = 0; i < n; i++) {
                if (!visited[i] && dfs(i, visited, recStack)) {
                    return true;
                }
            }
            return false;
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
            return false;
        }
    }
}
