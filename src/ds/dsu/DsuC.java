package ds.dsu;

import java.util.Arrays;

/**
 * static DSU
 * n sets numbered from 0 to n-1
 */
class DsuC {

    private int[] parent;
    private int[] size;
    private int n;

    public DsuC(int n) {
        this.n = n;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        Arrays.fill(size, 1);
    }

    public int findSet(int v) {
        if (parent[v] == v) {
            return v;
        }
        int representative = findSet(parent[v]);
        parent[v] = representative;
        return representative;
    }

    public void union(int v1, int v2) {
        v1 = findSet(v1);
        v2 = findSet(v2);
        if (v1 != v2) {
            if (size[v1] < size[v2]) {
                int temp = v1;
                v1 = v2;
                v2 = temp;
            }
            parent[v2] = v1;
            size[v1] = size[v1] + size[v2];
        }
    }

    public boolean isSameSet(int v1, int v2) {
        return findSet(v1) == findSet(v2);
    }
}


