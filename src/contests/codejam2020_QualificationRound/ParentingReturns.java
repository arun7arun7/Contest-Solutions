package contests.codejam2020_QualificationRound;

import java.util.LinkedList;
import java.util.Scanner;

public class ParentingReturns {

    static class Bipartite {

        private int V ;
        private int[][] G;
        private int[] colorArr;
        public Bipartite(int[][] G) {
            V = G.length;
            this.G = G;
        }

        private  boolean isBipartiteUtil(int src)
        {
            colorArr[src] = 1;

            LinkedList<Integer> q = new LinkedList<Integer>();
            q.add(src);

            while (!q.isEmpty())
            {
                int u = q.getFirst();
                q.pop();

                if (G[u][u] == 1)
                    return false;

                for (int v = 0; v < V; ++v)
                {
                    if (G[u][v] ==1 && colorArr[v] == -1)
                    {
                        colorArr[v] = 1 - colorArr[u];
                        q.push(v);
                    }

                    else if (G[u][v] ==1 && colorArr[v] ==
                            colorArr[u])
                        return false;
                }
            }

            return true;
        }

        public  boolean isBipartite()
        {
            colorArr = new int[V];
            for (int i = 0; i < V; ++i)
                colorArr[i] = -1;

            for (int i = 0; i < V; i++)
                if (colorArr[i] == -1)
                    if (isBipartiteUtil(i) == false)
                        return false;

            return true;
        }

        public int[] getColorArr() {
            return colorArr;
        }

    }


    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for(int z = 1; z <= t; z++) {
            String ans;
            int n = sc.nextInt();
            int[][] arr = new int[n][2];
            for(int i = 0; i < n; i++) {
                arr[i][0] = sc.nextInt();
                arr[i][1] = sc.nextInt();
            }
            int[][] g = new int[n][n];
            for(int i = 0; i < n; i++) {
                for(int j = i; j < n; j++) {
                    if(i != j && arr[i][1] > arr[j][0] && arr[j][1] > arr[i][0]) {
                        g[i][j] = 1;g[j][i] = 1;
                    }
                }
            }
            Bipartite bipartite = new Bipartite(g);
            boolean isBipartite = bipartite.isBipartite();
            if(!isBipartite) {
                ans = "IMPOSSIBLE";
            }
            else {
                ans = "";
                int[] color = bipartite.getColorArr();
                for(int i = 0; i < color.length; i++) {
                    if(color[i] == 1) {
                        ans += "J";
                    }
                    else {
                        ans += "C";
                    }
                }
            }
            System.out.println("Case #" + z + ": " + ans);
        }
    }
}
