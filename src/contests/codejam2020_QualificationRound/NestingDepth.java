package contests.codejam2020_QualificationRound;

import java.util.Scanner;

public class NestingDepth {

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for(int z = 1; z <= t; z++) {
            String ans = "";
            String s = sc.next();
            int val = 0;
            for(int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                val = (ch - 48) - val;
                if(val > 0) {
                    for(int j = 0; j < val; j++) {
                        ans += "(";
                    }
                }
                else {
                    int val1 = Math.abs(val);
                    for(int j = 0; j < val1; j++) {
                        ans += ")";
                    }
                }
                ans += ch;
                val = (ch - 48);
            }
            for(int i = 0; i < val; i++) {
                ans += ")";
            }
            System.out.println("Case #" + z + ": " + ans);
        }
    }

}
