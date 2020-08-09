package ds.graph.EulerCircuitAndPath;

import java.util.*;

class DirectedGraph {

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<List<String>> input = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<String> list = new ArrayList<>();
            list.add(sc.next());
            list.add(sc.next());
            input.add(list);
        }
        List<String> res = findItinerary(input);
        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i) + " ");
        }
    }

    public static List<String> findItinerary(List<List<String>> tickets) {
        Set<String> ts = new HashSet();
        for (List<String> ticket : tickets) {
            String from = ticket.get(0), to = ticket.get(1);
            if (!ts.contains(from)) {
                ts.add(from);
            }
            if (!ts.contains(to)) {
                ts.add(to);
            }
        }
        List<String> list = new ArrayList(ts);
        Collections.sort(list);

        Map<String, Integer> map = new HashMap();
        for (int i = 0; i < ts.size(); i++) {
            map.put(list.get(i), i);
        }

        Graph graph = new Graph(ts.size());
        for (List<String> ticket : tickets) {
            String from = ticket.get(0), to = ticket.get(1);
            graph.addEdge(map.get(from), map.get(to));
        }

        List<Integer> eulerPath = graph.eulerPath(map.get("JFK"));
        List<String> res = new ArrayList();
        for (int vertex : eulerPath) {
            res.add(list.get(vertex));
        }
        return res;
    }

    static class Graph {
        int n;
        TreeSet<Integer>[] neighbors;
        int[] indegree;
        int[] outdegree;

        public Graph(int n) {
            this.n = n;
            neighbors = new TreeSet[n];
            indegree = new int[n];
            outdegree = new int[n];
            for(int i = 0; i < n; i++) {
                neighbors[i] = new TreeSet();
            }
        }

        public void addEdge(int src, int dest) {
            neighbors[src].add(dest);
            indegree[dest]++;
            outdegree[src]++;
        }

        // assumes eulerPath exists from start
        public List<Integer> eulerPath(int start) {
            int v1 = -1, v2 = -1;
            for (int i = 0; i < n; i++) {
                if (outdegree[i] - indegree[i] == 1) {
                    v1 = i;
                }
                if (indegree[i] - outdegree[i] == 1) {
                    v2 = i;
                }
            }

            if (v1 != -1) {
                neighbors[v2].add(v1);
            }
            List<Integer> eulerianCycle = eulerCycle(start);
            List<Integer> eulerPath;
            if (v1 != -1) {
                eulerPath = new ArrayList();
                for (int i = 0; i < eulerianCycle.size()-1; i++) {
                    if (eulerianCycle.get(i) == v2 && eulerianCycle.get(i+1) == v1){
                        for (int j = i+1; j < eulerianCycle.size(); j++) {
                            eulerPath.add(eulerianCycle.get(j));
                        }
                        for (int j = 1; j <= i; j++) {
                            eulerPath.add(eulerianCycle.get(j));
                        }
                        break;
                    }
                }
            }
            else {
                eulerPath = eulerianCycle;
            }
            return eulerPath;
        }

        // assumes eulerCycle exists from start
        public List<Integer> eulerCycle(int start) {
            Stack<Integer> st = new Stack();
            List<Integer> res = new ArrayList();
            st.push(start);
            while (!st.isEmpty()) {
                int cur = st.peek();
                if (neighbors[cur].isEmpty()) {
                    res.add(cur);
                    st.pop();
                }
                else {
                    int neighbor = neighbors[cur].pollFirst();
                    st.push(neighbor);
                }
            }
            List<Integer> finalRes = new ArrayList<>();
            for (int i = res.size()-1; i >= 0; i--) {
                finalRes.add(res.get(i));
            }
            return finalRes;
        }
    }
}