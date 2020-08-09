package contests.codejam2020_QualificationRound;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Vestigium {

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for(int z = 1; z <= t; z++) {
            int n = sc.nextInt();
            int[][] arr = new int[n][n];
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    arr[i][j] = sc.nextInt();
                }
            }
            int k = 0, r = 0, c = 0;
            Set<Integer>[] rowSum = new HashSet[n];
            Set<Integer>[] colSum = new HashSet[n];
            for(int i = 0; i < n; i++) {
                rowSum[i] = new HashSet<>();
                colSum[i] = new HashSet<>();
            }
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(i == j) {
                        k += arr[i][j];
                    }
                    rowSum[i].add(arr[i][j]);
                    colSum[j].add(arr[i][j]);
                }
            }
            // int excepted = (n * (n + 1)) / 2;
            for(int i = 0; i < n; i++) {
                if(rowSum[i].size() != n) {
                    r++;
                }
                if(colSum[i].size() != n) {
                    c++;
                }
            }
            System.out.println("Case #" + z + ": " + k + " " + r + " " + c);
        }
    }
}
