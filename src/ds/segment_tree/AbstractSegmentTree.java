package ds.segment_tree;

import java.util.ArrayList;
import java.util.List;

class AbstractSegmentTree {
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
    public AbstractSegmentTree(int[] arr, int n) {
        this.arr = new int[n];
        for (int i = 0; i < n; i++) {
            this.arr[i] = arr[i];
        }
        this.n = n;
        int x = (int) (Math.ceil(Math.log(n) / Math.log(2)));
        int max_size = 2 * (int) Math.pow(2, x) + 5;
        this.st = new Node[max_size];
        build(1,0,n-1);
    }
    private void build(int node, int beg, int end) {
        if (beg == end) {
            st[node] = new Node(arr[beg], arr[beg], arr[beg], arr[beg]);
            return;
        }
        int mid = (beg + end) / 2;
        build(2*node, beg, mid);
        build(2*node+1, mid+1, end);
        st[node] = new Node(
                Math.max(st[2*node].maxSuffixSum + st[2*node+1].maxPrefixSum, Math.max(st[2*node].maxValue, st[2*node+1].maxValue)),
                Math.max(st[2*node].maxPrefixSum , st[2*node].sum + st[2*node+1].maxPrefixSum),
                Math.max(st[2*node+1].maxSuffixSum, st[2*node+1].sum + st[2*node].maxSuffixSum),
                st[2*node].sum + st[2*node+1].sum
        );
    }
    public void update(int ind, int ele) {
        update(1,ind,ele,0,n-1);
    }
    private void update(int node, int ind, int ele, int beg, int end) {
        if (beg == end) {
            arr[beg] = ele;
            st[node] = new Node(arr[beg], arr[beg], arr[beg], arr[beg]);
            return;
        }
        int mid = (beg + end) / 2;
        if (beg <= ind && ind <= mid) {
            update(2*node, ind, ele, beg, mid);
        }
        else {
            update(2*node+1, ind, ele, mid+1, end);
        }
        st[node] = new Node(
                Math.max(st[2*node].maxSuffixSum + st[2*node+1].maxPrefixSum, Math.max(st[2*node].maxValue, st[2*node+1].maxValue)),
                Math.max(st[2*node].maxPrefixSum , st[2*node].sum + st[2*node+1].maxPrefixSum),
                Math.max(st[2*node+1].maxSuffixSum, st[2*node+1].sum + st[2*node].maxSuffixSum),
                st[2*node].sum + st[2*node+1].sum
        );
    }
    public int query(int l, int r) {
        nodes.clear();
        addNodes(1,0,n-1,l,r);
        dp = new int[nodes.size()];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = -1;
        }
        func(0);
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < nodes.size(); i++) {
            ans = max(ans, nodes.get(i).maxValue, nodes.get(i).maxSuffixSum + (((i+1) == nodes.size()) ? 0 : dp[i+1]));
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
        addNodes(2*node, beg, mid, l, r);
        addNodes(2*node+1, mid+1, end, l, r);
    }
    private int func(int i) {
        if (i >= nodes.size()) {
            return 0;
        }
        if (dp[i] != -1) {
            return dp[i];
        }
        int ans = Math.max(nodes.get(i).maxPrefixSum, nodes.get(i).sum + func(i+1));
        dp[i] = ans;
        return ans;
    }
    private int max(int...elements) {
        int ans = Integer.MIN_VALUE;
        for (int element : elements) {
            ans = Math.max(ans, element);
        }
        return ans;
    }
}
