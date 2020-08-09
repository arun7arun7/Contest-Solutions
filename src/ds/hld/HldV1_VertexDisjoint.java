package ds.hld;

import java.util.*;

/**
 * NOTE : Needs to be tested
 **/
public class HldV1_VertexDisjoint {

    int n;
    // contains head node(beginning node where heavy edge starts) for node i
    int[] head;
    // contains rearrangement of nodes where each heavy edge path nodes in the tree have consecutive values
    int[] pos;
    int[] nodeCost;
    int curPos;

    Tree tree;
    SegmentTree segmentTree; // Can be replaced by any data structure to perform on the path

    /**
     *
     * @param n no of nodes in tree where nodes are numbered from 0 to n-1(both inclusive)
     * @param src n-1 edges src node no
     * @param dest n-1 edges dest node no
     * @param root node no which should be rooted(can be any)
     * @param nodesValue node values where nodesValue[i] contains value for node number i
     */
    public HldV1_VertexDisjoint(int n, int[] src, int[] dest, int root, int[] nodesValue) {
        this.tree = new Tree(n, src, dest, root);
        this.n = n;
        curPos = 0;

        head = new int[n];
        pos = new int[n];
        nodeCost = new int[n];
        for (int i = 0; i < n;i++) {
            nodeCost[i] = nodesValue[i];
        }
        Arrays.fill(head, -1);
        decompose(root, root);
        constructSegmentTree();
    }

    private void constructSegmentTree() {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[pos[i]] = nodeCost[i];
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

    }

    /**
     *
     * @param a node no
     * @param b node no
     * @return  perform operation on all nodes between a and b(both inclusive)
     */
    public int compute(int a, int b) {
        int ans = 0;
        for (; head[a] != head[b]; b = tree.getParent(head[b])) {
            if (tree.getDepth(head[a]) > tree.getDepth(head[b])) {
                int temp = a;
                a = b;
                b = temp;
            }
            ans += segmentTree.query(pos[head[b]], pos[b]); // Any Operation
        }
        if (tree.getDepth(a) > tree.getDepth(b)) {
            int temp = a;
            a = b;
            b = temp;
        }
        ans += segmentTree.query(pos[a], pos[b]); // Any Operation
        return ans;
    }

    static class SegmentTree {
        private class Node {
            int maxValue, maxPrefixSum, maxSuffixSum, sum;

            public Node(int maxValue, int maxPrefixSum, int maxSuffixSum, int sum) {
                this.maxValue = maxValue;
                this.maxPrefixSum = maxPrefixSum;
                this.maxSuffixSum = maxSuffixSum;
                this.sum = sum;
            }
        }

        private Node[] st;
        private int[] arr;
        private int n;
        private List<Node> nodes = new ArrayList<>();
        private int[] dp;

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
                st[node] = new Node(arr[beg], arr[beg], arr[beg], arr[beg]);
                return;
            }
            int mid = (beg + end) / 2;
            build(2 * node, beg, mid);
            build(2 * node + 1, mid + 1, end);
            st[node] = new Node(
                    Math.max(st[2 * node].maxSuffixSum + st[2 * node + 1].maxPrefixSum, Math.max(st[2 * node].maxValue, st[2 * node + 1].maxValue)),
                    Math.max(st[2 * node].maxPrefixSum, st[2 * node].sum + st[2 * node + 1].maxPrefixSum),
                    Math.max(st[2 * node + 1].maxSuffixSum, st[2 * node + 1].sum + st[2 * node].maxSuffixSum),
                    st[2 * node].sum + st[2 * node + 1].sum
            );
        }

        public void update(int ind, int ele) {
            update(1, ind, ele, 0, n - 1);
        }

        private void update(int node, int ind, int ele, int beg, int end) {
            if (beg == end) {
                arr[beg] = ele;
                st[node] = new Node(arr[beg], arr[beg], arr[beg], arr[beg]);
                return;
            }
            int mid = (beg + end) / 2;
            if (beg <= ind && ind <= mid) {
                update(2 * node, ind, ele, beg, mid);
            } else {
                update(2 * node + 1, ind, ele, mid + 1, end);
            }
            st[node] = new Node(
                    Math.max(st[2 * node].maxSuffixSum + st[2 * node + 1].maxPrefixSum, Math.max(st[2 * node].maxValue, st[2 * node + 1].maxValue)),
                    Math.max(st[2 * node].maxPrefixSum, st[2 * node].sum + st[2 * node + 1].maxPrefixSum),
                    Math.max(st[2 * node + 1].maxSuffixSum, st[2 * node + 1].sum + st[2 * node].maxSuffixSum),
                    st[2 * node].sum + st[2 * node + 1].sum
            );
        }

        public int query(int l, int r) {
            nodes.clear();
            addNodes(1, 0, n - 1, l, r);
            dp = new int[nodes.size()];
            for (int i = 0; i < dp.length; i++) {
                dp[i] = -1;
            }
            func(0);
            int ans = Integer.MIN_VALUE;
            for (int i = 0; i < nodes.size(); i++) {
                ans = max(ans, nodes.get(i).maxValue, nodes.get(i).maxSuffixSum + (((i + 1) == nodes.size()) ? 0 : dp[i + 1]));
            }
            return ans;
        }

        private void addNodes(int node, int beg, int end, int l, int r) {
            if (end < l || beg > r) {
                return;
            }
            if (beg >= l && end <= r) {
                nodes.add(st[node]);
                return;
            }
            int mid = (beg + end) / 2;
            addNodes(2 * node, beg, mid, l, r);
            addNodes(2 * node + 1, mid + 1, end, l, r);
        }

        private int func(int i) {
            if (i >= nodes.size()) {
                return 0;
            }
            if (dp[i] != -1) {
                return dp[i];
            }
            int ans = Math.max(nodes.get(i).maxPrefixSum, nodes.get(i).sum + func(i + 1));
            dp[i] = ans;
            return ans;
        }

        private int max(int... elements) {
            int ans = Integer.MIN_VALUE;
            for (int element : elements) {
                ans = Math.max(ans, element);
            }
            return ans;
        }
    }

    static class Tree {

        private int n;
        private Set<Integer>[] neighbours;
        private int[] depth;
        private int[] subtreeSize;
        private int[] parent;

        public Tree(int n) {
            this.n = n;
            neighbours = new HashSet[n];
            for (int i = 0; i < n; i++) {
                neighbours[i] = new HashSet<>();
            }
            depth = new int[n];
            subtreeSize = new int[n];
            parent = new int[n];
        }

        public Tree(int n, int[] src, int[] dest, int root) {
            this.n = n;
            neighbours = new HashSet[n];
            for (int i = 0; i < n; i++) {
                neighbours[i] = new HashSet<>();
            }
            depth = new int[n];
            subtreeSize = new int[n];
            parent = new int[n];
            for (int i = 0; i < n-1; i++) {
                addEdge(src[i], dest[i]);
            }
            dfs(root);
        }

        public void addEdge(int a, int b) {
            neighbours[a].add(b);
            neighbours[b].add(a);
        }

        public void dfs(int start) {
            dfs(start, -1, 0);
        }

        private void dfs(int cur, int par, int dep) {
            depth[cur] = dep;
            subtreeSize[cur] = 1;
            parent[cur] = par;
            for (int neighbour : neighbours[cur]) {
                if (neighbour != par) {
                    dfs(neighbour, cur, dep+1);
                    subtreeSize[cur] += subtreeSize[neighbour];
                }
            }
        }

        public int getN() {
            return n;
        }

        public Set<Integer> getNeighbours(int node) {
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
            for (int neighbour : neighbours[node]) {
                if (neighbour != parent[node]) {
                    childNodes.add(neighbour);
                }
            }
            return childNodes;
        }
    }

}

