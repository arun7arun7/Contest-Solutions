package ds.hld;

import java.util.*;

/**
 * NOTE : Needs to be tested
 **/

public class HldV1_EdgeDisjoint {

    int n;
    // contains head node(beginning node where heavy edge starts) for node i
    int[] head;
    // contains tail node(ending node where heavy edge ends) for node i
    int[] tail;
    // contains rearrangement of nodes where each heavy edge path nodes in the tree have consecutive values
    int[] pos;
    int curPos;
    int tailNode;

    Tree tree;
    SegmentTree segmentTree; // Can be replaced by any data structure to perform on the path

    long[] nSum = new long[100005];

    /**
     * @param n      no of nodes in tree where nodes are numbered from 0 to n-1(both inclusive)
     * @param src    n-1 edges src node no
     * @param dest   n-1 edges dest node no
     * @param weight weight of edge between src and dest
     * @param root   node no which should be rooted(can be any)
     */
    public HldV1_EdgeDisjoint(int n, int[] src, int[] dest, int[] weight, int root) {
        this.tree = new Tree(n, src, dest, weight, root);
        this.n = n;
        curPos = 0;

        head = new int[n];
        tail = new int[n];
        pos = new int[n];
        Arrays.fill(head, -1);
        Arrays.fill(tail, -1);
        decompose(root, root);
        constructSegmentTree();
        for (int i = 1; i < nSum.length; i++) {
            nSum[i] = nSum[i-1] + i;
        }
    }

    private void constructSegmentTree() {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[pos[i]] = tree.getParentWeightEdge(i);
        }
        segmentTree = new SegmentTree(arr, n);
    }

    private void decompose(int cur, int h) {
        pos[cur] = curPos++;
        head[cur] = h;

        // finding heavy edge
        int vertex = -1, max = -1;
        for (int child : tree.getChildNodes(cur)) {
            if (tree.getSubtreeSize(child) > max) {
                max = tree.getSubtreeSize(child);
                vertex = child;
            }
        }

        if (vertex != -1) {
            decompose(vertex, h);
            for (int child : tree.getChildNodes(cur)) {
                if (child != vertex) {
                    decompose(child, child);
                }
            }
        }
        tail[cur] = (vertex != -1) ? tail[vertex] : cur;
    }

    public void update(int s, int d, int litres) {
        int dist = upperBound(nSum, nSum.length, litres) - 1;
        int distBetweenSandD = tree.distance(s, d);
        if (distBetweenSandD <= dist) {
            update(s, d, 1, tree.distance(s, d));
        }
        else {
            int remainingLitres = (int)(litres - nSum[dist]);
            int lca = tree.lca(s, d);
            int dist1 = tree.distance(s, lca);
            int node;
            if (dist == dist1) {
                node = lca;
            }
            else if (dist < dist1) {
                node = tree.findKthAncestor(s, dist);
            }
            else {
                node = tree.findKthAncestor(d, distBetweenSandD - dist);
            }
            update(s, node, 1, dist);
            if (remainingLitres > 0) {
                update(node, tree.findKthAncestor(d, distBetweenSandD - dist - 1),remainingLitres, remainingLitres);
            }
        }
    }

    private void update(int src, int dest, int start, int end) {
        for (; head[src] != head[dest]; ) {
            if (tree.getDepth(head[src]) > tree.getDepth(head[dest])) {
                start += tree.distance(src, head[src]);
                segmentTree.update(pos[head[src]], pos[src], start, false);
                src = tree.getParent(head[src]);
                start += 1;
            }
            else {
                end -= tree.distance(dest, head[dest]);
                segmentTree.update(pos[head[dest]], pos[dest], end, true);
                dest = tree.getParent(head[dest]);
                end -= 1;
            }
        }
        if (src != dest) {
            if (tree.getDepth(src) > tree.getDepth(dest)) {
                segmentTree.update(pos[dest]+1, pos[src], end, false);
            }
            else {
                segmentTree.update(pos[src]+1, dest, start, true);
            }
        }
    }

    /**
     * @param a node no
     * @param b node no
     * @return perform operation on all nodes between a and b(both inclusive)
     */
    public long compute(int a, int b) {
        long ans = 0;
        for (; head[a] != head[b]; b = tree.getParent(head[b])) {
            if (tree.getDepth(head[a]) > tree.getDepth(head[b])) {
                int temp = a;
                a = b;
                b = temp;
            }
            ans += segmentTree.query(pos[head[b]], pos[b]); // Any Operation
        }
        if (a != b) {
            if (tree.getDepth(a) > tree.getDepth(b)) {
                int temp = a;
                a = b;
                b = temp;
            }
            ans += segmentTree.query(pos[a] + 1, pos[b]); // Any Operation
        }
        return ans;
    }

    // It returns index of first element which is grater than searched value.<br>
    // If searched element is bigger than any array element function returns first index after last element.<br>
    public static int upperBound(long[] array, int length, int value) {
        int low = 0;
        int high = length;
        while (low < high) {
            final int mid = (low + high) / 2;
            if (value >= array[mid]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    /**
     * Lower bound search algorithm.<br>
     * Lower bound is kind of binary search algorithm but:<br>
     * -If searched element doesn't exist function returns index of first element which is bigger than searched value.<br>
     * -If searched element is bigger than any array element function returns first index after last element.<br>
     * -If searched element is lower than any array element function returns index of first element.<br>
     * -If there are many values equals searched value function returns first occurrence.<br>
     */
    public static int lowerBound(int[] array, int length, int value) {
        int low = 0;
        int high = length;
        while (low < high) {
            final int mid = (low + high) / 2;
            if (value <= array[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    static class SegmentTree {
        private class Node {
            long addend;
            long addDiff;
            long subend;
            long subDiff;
            long sum;
        }

        private Node[] st;
        private int[] arr;
        private int n;

        public SegmentTree(int[] arr, int n) {
            this.arr = new int[n];
            for (int i = 0; i < n; i++) {
                this.arr[i] = arr[i];
            }
            this.n = n;
            int x = (int) (Math.ceil(Math.log(n) / Math.log(2)));
            int max_size = 2 * (int) Math.pow(2, x) + 5;
            this.st = new Node[max_size];
            build(1, 0, n - 1);
        }

        private void build(int node, int beg, int end) {
            if (beg == end) {
                st[node] = new Node();
                return;
            }
            int mid = (beg + end) / 2;
            build(2 * node, beg, mid);
            build(2 * node + 1, mid + 1, end);
            st[node] = new Node();
        }

        public void update(int l, int r, int ele, boolean isIncreasinAP) {
            update(1, l, r, ele, 0, n - 1, isIncreasinAP);
        }

        private void update(int node, int l, int r, int ele, int beg, int end, boolean isIncreasinAP) {
            if (end < l || beg > r) {
                return;
            } else if (beg == l && end == r) {
                if (isIncreasinAP) {
                    st[node].addend += ele;
                    st[node].addDiff += 1;
                    st[node].sum += (((2 * ele + (end - beg)) * (end - beg + 1)) / 2);
                } else {
                    st[node].subend += ele;
                    st[node].subDiff += 1;
                    st[node].sum += (((2 * ele - (end - beg)) * (end - beg + 1)) / 2);
                }
            } else {
                push(node, beg, end);
                int mid = (beg + end) / 2;
                update(2 * node, l, r, ele, beg, mid, isIncreasinAP);
                update(2 * node + 1, l, r, ele, mid + 1, end, isIncreasinAP);
                st[node].sum = st[2 * node].sum + st[2 * node + 1].sum;
            }
        }

        public long query(int l, int r) {
            return sum(1, l, r, 0, n - 1);
        }

        private long sum(int node, int l, int r, int beg, int end) {
            if (end < l || beg > r) {
                return 0;
            } else if (beg >= l && end <= r) {
                return st[node].sum;
            } else {
                push(node, beg, end);
                int mid = (beg + end) / 2;
                return sum(2 * node, l, r, beg, mid) + sum(2 * node + 1, l, r, mid + 1, end);
            }
        }

        private void push(int node, int beg, int end) {
            int mid = (beg + end) / 2;
            st[2 * node].addend += st[node].addend;
            st[2 * node].addDiff += st[node].addDiff;
            st[2 * node].subend += st[node].subend;
            st[2 * node].subDiff += st[node].subDiff;
            calcSum(2 * node, beg, mid);

            int dist = (((beg + end) / 2) + 1) - beg;
            st[2 * node + 1].addend = st[node].addend + (dist) * st[node].addDiff;
            st[2 * node + 1].addDiff += st[node].addDiff;
            st[2 * node + 1].subend += st[node].subend - (dist) * st[node].subDiff;
            st[2 * node + 1].subDiff += st[node].subDiff;
            calcSum(2 * node + 1, mid + 1, end);

            st[node].addend = 0;
            st[node].addDiff = 0;
            st[node].subend = 0;
            st[node].subDiff = 0;
        }

        private void calcSum(int node, int beg, int end) {
            st[node].sum += ((2 * st[node].addend + (end - beg) * st[node].addDiff) * (end - beg + 1)) / 2;
            st[node].sum += ((2 * st[node].subend - (end - beg) * st[node].subDiff) * (end - beg + 1)) / 2;
        }

    }

    static class Tree {

        static class Edge {
            private int from;
            private int to;
            private int weight;

            public Edge(int from, int to, int weight) {
                this.from = from;
                this.to = to;
                this.weight = weight;
            }

            public int getFrom() {
                return from;
            }

            public int getTo() {
                return to;
            }

            public int getWeight() {
                return weight;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Edge edge = (Edge) o;
                return from == edge.from &&
                        to == edge.to &&
                        weight == edge.weight;
            }

            @Override
            public int hashCode() {
                return Objects.hash(from, to, weight);
            }
        }

        private int n, maxLog;
        private Set<Edge>[] neighbours;
        private int[] depth;
        private int[] subtreeSize;
        private int[] parent;
        private int[] parentEdgeWeight;
        private int[][] st;

        public Tree(int n) {
            this.n = n;
            neighbours = new HashSet[n];
            for (int i = 0; i < n; i++) {
                neighbours[i] = new HashSet<>();
            }
            depth = new int[n];
            subtreeSize = new int[n];
            parent = new int[n];
            parentEdgeWeight = new int[n];
            preprocess();
        }

        public Tree(int n, int[] src, int[] dest, int[] weight, int root) {
            this.n = n;
            neighbours = new HashSet[n];
            for (int i = 0; i < n; i++) {
                neighbours[i] = new HashSet<>();
            }
            depth = new int[n];
            subtreeSize = new int[n];
            parent = new int[n];
            parentEdgeWeight = new int[n];
            for (int i = 0; i < n - 1; i++) {
                addEdge(src[i], dest[i], weight[i]);
            }
            dfs(root);
        }

        public void addEdge(int a, int b, int weight) {
            neighbours[a].add(new Edge(a, b, weight));
            neighbours[b].add(new Edge(b, a, weight));
        }

        public void dfs(int start) {
            dfs(start, -1, 0, -1);
        }

        private void dfs(int cur, int par, int dep, int weight) {
            depth[cur] = dep;
            subtreeSize[cur] = 1;
            parent[cur] = par;
            parentEdgeWeight[cur] = weight;
            for (Edge neighbour : neighbours[cur]) {
                if (neighbour.to != par) {
                    int child = neighbour.to;
                    dfs(child, cur, dep + 1, neighbour.weight);
                    subtreeSize[cur] += subtreeSize[child];
                }
            }
        }

        private void preprocess() {
            maxLog = (int)(Math.log(n) / Math.log(2)) + 5;
            st = new int[n][maxLog];
            for (int i = 0; i < n; i++) {
                Arrays.fill(st[i], -1);
            }

            for (int i = 0; i < n; i++) {
                st[i][0] = getParent(i);
            }

            for (int j = 1; j < maxLog; j++) {
                for (int i = 0; i < n; i++) {
                    if (st[i][j-1] != -1) {
                        st[i][j] = st[st[i][j-1]][j-1];
                    }
                }
            }
        }

        /**
         *
         * @param a node no
         * @param b node no
         * @return
         */
        public int lca(int a, int b) {
            if (getDepth(a) > getDepth(b)) {
                int temp = a;
                a = b;
                b = temp;
            }
            int dist = getDepth(b) - getDepth(a);
            if (dist > 0) {
                b = findKthAncestor(b, dist);
            }
            if (a == b) {
                return a;
            }
            for(int j = maxLog-1; j >=0; j--)
            {
                if((st[a][j] != -1) && (st[a][j] != st[b][j]))
                {
                    a = st[a][j];
                    b = st[b][j];
                }
            }
            return parent[a];
        }

        /**
         *
         * @param v node no
         * @param k k >= 0
         * @return node no of kth ancesstor of node v or -1 if it does not exist
         */
        public int findKthAncestor(int v, int k) {
            for (int j = maxLog-1; j >= 0; j--) {
                if ((k & (1 << j)) > 0) {
                    v = st[v][j];
                    if (v == -1) {
                        return -1;
                    }
                }
            }
            return v;
        }

        public int distance(int src, int dest) {
            return getDepth(src) + getDepth(dest) - 2*getDepth(lca(src, dest));
        }

        public int getN() {
            return n;
        }

        public Set<Edge> getNeighbours(int node) {
            return neighbours[node];
        }

        public int getDepth(int node) {
            return depth[node];
        }

        public int getSubtreeSize(int node) {
            return subtreeSize[node];
        }

        public int getParent(int node) {
            return parent[node];
        }

        public Set<Integer> getChildNodes(int node) {
            Set<Integer> childNodes = new HashSet<>();
            for (Edge neighbour : neighbours[node]) {
                if (neighbour.to != parent[node]) {
                    childNodes.add(neighbour.to);
                }
            }
            return childNodes;
        }

        public int getParentWeightEdge(int node) {
            return parentEdgeWeight[node];
        }
    }

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for(int z = 1; z <= t; z++) {
            long ans = Long.MIN_VALUE;
            int n = sc.nextInt();
            int[] src = new int[n-1];
            int[] dest = new int[n-1];
            int[] weight = new int[n-1];
            for (int i = 0; i < n-1; i++) {
                src[i] = sc.nextInt();
                dest[i] = sc.nextInt();
            }
            HldV1_EdgeDisjoint hld = new HldV1_EdgeDisjoint(n, src, dest, weight, 0);
            int m = sc.nextInt();
            for (int i = 0; i < m;i++) {
                int s = sc.nextInt(), d = sc.nextInt(), l = sc.nextInt();
                if (s != d) {
                    hld.update(s - 1, d - 1, l);
                }
            }
            for (int i = 0; i < n; i++) {
                long litresFromRootToI = hld.compute(0, i);
                if (litresFromRootToI > ans) {
                    ans = litresFromRootToI;
                }
            }
            System.out.println(ans);
        }
    }
}




