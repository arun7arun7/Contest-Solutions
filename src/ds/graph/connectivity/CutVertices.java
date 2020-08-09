package ds.graph.connectivity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class CutVertices {

    class Graph {
        private int n;
        private LinkedList<Integer>[] neighbours;
        private Set<Integer> cutVertices;
        private boolean[] visited;
        private int timer;
        private int[] tin;
        private int[] low;

        public Graph(int n) {
            this.n = n;
            neighbours = new LinkedList[n];
            for (int i = 0; i < n; i++) {
                neighbours[i] = new LinkedList<>();
            }
        }

        public void addEdge(int u, int v) {
            neighbours[u].add(v);
            neighbours[v].add(u);
        }

        public Set<Integer> fillCutVertices() {
            cutVertices = new HashSet<>();
            visited = new boolean[n];
            tin = new int[n];
            low = new int[n];
            timer = 0;

            Arrays.fill(tin, -1);
            Arrays.fill(low, -1);
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    dfs(i, -1);
                }
            }
            return cutVertices;
        }

        private void dfs(int v, int parent) {
            visited[v] = true;
            tin[v] = timer;
            low[v] = timer;
            timer++;
            int children = 0;
            for (int to : neighbours[v]) {
                if (to == parent) {
                    continue;
                }
                if (visited[to]) {
                    low[v] = Math.min(low[v], tin[to]);
                }
                else {
                    dfs(to, v);
                    low[v] = Math.min(low[v], low[to]);
                    if (low[to] >= tin[v] && parent != -1) {
                        cutVertices.add(v);
                    }
                    ++children;
                }
            }
            if (parent == -1 && children > 1) {
                cutVertices.add(v);
            }
        }

    }

}
