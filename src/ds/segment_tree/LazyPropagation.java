package ds.segment_tree;

import java.util.*;

public class LazyPropagation {

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] positions = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                positions[i][j] = sc.nextInt();
            }
        }
        List<Integer> ans = fallingSquares(positions);
        for (int i = 0; i < ans.size(); i++) {
            System.out.print(ans.get(i)+ " ");
        }
    }

    public static List<Integer> fallingSquares(int[][] positions) {
        int n = positions.length;
        Set<Integer> set = new TreeSet<>();
        Map<Integer, Integer> posToIndMap = new HashMap<>();
        int ind = 0;
        for (int[] pos : positions) {
            set.add(pos[0]);
            set.add(pos[0]+pos[1]-1);
        }
        for (int ele : set) {
            posToIndMap.put(ele, ind++);
        }

        SegmentTree tree = new SegmentTree(ind);
        List<Integer> ans = new ArrayList();
        int best = 0;
        for (int[] pos : positions) {
            int q = tree.query(posToIndMap.get(pos[0]), posToIndMap.get(pos[0]+pos[1]-1));
            tree.update(posToIndMap.get(pos[0]), posToIndMap.get(pos[0]+pos[1]-1), q+pos[1]);
            best = Math.max(best, q+pos[1]);
            ans.add(best);
        }
        return ans;
    }

    static class SegmentTree {

        class Node {
            int max;
            int set;
            boolean mark;

            public Node(int max, int set, boolean mark) {
                this.max = max;
                this.set = set;
                this.mark = mark;
            }
        }

        int[] arr;
        int n;
        Node[] st;

        public SegmentTree(int n) {
            this.n = n;
            this.arr = new int[n];
            int x = (int)Math.ceil(Math.log(n)/Math.log(2));
            int size = (int)(2*Math.pow(2,x)) + 5;
            // int size = 5*n;
            st = new Node[size];
            for (int i = 0; i < size; i++) {
                st[i] = new Node(0,0,false);
            }
        }

        public void update(int l, int r, int set) {
            update(1, 0, n-1, l, r, set);
        }

        private void update(int node, int beg, int end, int l, int r, int set) {
            if (beg > r || end < l) {
                return;
            }
            if (beg >= l && end <= r) {
                st[node] = new Node(set, set, true);
                return;
            }
            push(node);
            int mid = (beg + end) / 2;
            update(2*node, beg, mid, l, r, set);
            update(2*node+1, mid+1, end, l, r, set);
            st[node].max = Math.max(st[2*node].max, st[2*node+1].max);
        }

        public int query( int l, int r) {
            return query(1, 0, n-1, l, r);
        }

        private int query(int node, int beg, int end, int l, int r) {
            if (beg > r || end < l) {
                return 0;
            }
            if (beg >= l && end <= r) {
                return st[node].max;
            }
            push(node);
            int mid = (beg + end) / 2;
            return Math.max(query(2*node, beg, mid, l, r), query(2*node+1, mid+1, end, l, r));
        }

        private void push(int node) {
            if (st[node].mark) {
                st[2*node].max = st[node].set;
                st[2*node].set = st[node].set;
                st[2*node].mark = true;

                st[2*node+1].max = st[node].set;
                st[2*node+1].set = st[node].set;
                st[2*node+1].mark = true;

                st[node].set = 0;
                st[node].mark = false;
            }
        }
    }

}

