package contests.codejam2020_QualificationRound;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ESAbATAd {

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt(), b = sc.nextInt();
        for(int z = 1; z <= t; z++) {
            byte[] a = new byte[b];
            char ch;
            for(int i = 0; i < b; i++) {
                a[i] = -1;
            }
            for(int i = 0; i < 5; i++) {
                System.out.println(i+1);
                ch = sc.next().charAt(0);
                a[i] = fun(ch);
                System.out.println(b-i);
                ch = sc.next().charAt(0);
                a[b-i-1] = fun(ch);
            }
            int indFront = 5;
            AtomicInteger count = new AtomicInteger(11);
            while (count.intValue() <= 150) {
                if(indFront >= b / 2) {
                    String ans = "";
                    for(int i = 0; i < b; i++) {
                        ans += a[i];
                    }
                    System.out.println(ans);
                    break;
                }
                if(count.intValue() % 10 == 1) {
                    int effectNo = guessEffectNo(a,indFront, sc, count);
                    performOperation(a,indFront,effectNo);
                }
                byte b1 = -2, b2 = -2;
                for(int i = 0; i < 2; i++) {
                    if(count.intValue() % 10 != 1 && i == 0) {
                        System.out.println(indFront+1);
                        ch = sc.next().charAt(0);
                        b1= fun(ch);
                        count.set(count.intValue()+1);
                    }
                    if(count.intValue() % 10 != 1 && i == 1) {
                        System.out.println(b - indFront);
                        ch = sc.next().charAt(0);
                        b2 = fun(ch);
                        count.set(count.intValue()+1);
                    }
                }
                if(b1 != -2 && b2 != -2) {
                    a[indFront] = b1;
                    a[b - indFront - 1] = b2;
                    indFront++;
                }
            }
            ch = sc.next().charAt(0);
            if(ch == 'N') {
                break;
            }
        }
    }

    private static void performOperation(byte[] a, int ind, int effectNo) {
        if(effectNo == 1) {
            for(int i = 0; i < ind; i++) {
                if(a[i] == 1) {
                    a[i] = 0;
                }
                else  {
                    a[i] = 1;
                }

                if(a[a.length-i-1] == 1) {
                    a[a.length-i-1] = 0;
                }
                else {
                    a[a.length-i-1] = 1;
                }
            }
        }
        else if(effectNo == 2) {
            for(int i = 0; i < ind; i++) {
                byte temp = a[i];
                a[i] = a[a.length - i - 1];
                a[a.length - i - 1] = temp;
            }
        }
        else if(effectNo == 3) {
            for(int i = 0; i < ind; i++) {
                byte temp = a[i];
                a[i] = a[a.length - i - 1];
                a[a.length - i - 1] = temp;

                if(a[i] == 1) {
                    a[i] = 0;
                }
                else  {
                    a[i] = 1;
                }

                if(a[a.length-i-1] == 1) {
                    a[a.length-i-1] = 0;
                }
                else {
                    a[a.length-i-1] = 1;
                }
            }
        }
    }

    static byte fun(char ch) {
        if(ch == '1') {
            return 1;
        }
        else if(ch == '0') {
            return 0;
        }
        else {
            return -1;
        }
    }

    static int guessEffectNo(byte[] a, int ind, Scanner sc, AtomicInteger count) {
        int ind1 = -1, ind2 = -1;
        for(int i = 0; i < ind; i++) {
            if(a[i] == a[a.length-i-1]) {
                ind1 = i;
            }
            if((a[i] > a[a.length-i-1] && a[i] - 1 == a[a.length-i-1]) || (a[i] < a[a.length-i-1] && a[i] + 1 == a[a.length-i-1])) {
                ind2 = i;
            }
            if(ind1 >= 0 && ind2 >= 0) {
                break;
            }
        }

        char ch;
        Set<Integer> possibleEffectNo = new HashSet<>();
        Set<Integer> possibleEffectNoByInd2 = new HashSet<>();
        if(ind1 >= 0) {
            System.out.println(ind1+1);
            ch = sc.next().charAt(0);
            byte b = fun(ch);
            count.set(count.intValue()+1);
            if(a[ind1] == 1  && b == 0) {
                possibleEffectNo.add(1);possibleEffectNo.add(3);
            }
            else if(a[ind1] == 1  && b == 1) {
                possibleEffectNo.add(2);possibleEffectNo.add(4);
            }
            else if(a[ind1] == 0  && b == 0) {
                possibleEffectNo.add(2);possibleEffectNo.add(4);
            }
            else if(a[ind1] == 0  && b == 1) {
                possibleEffectNo.add(1);possibleEffectNo.add(3);
            }
        }
        if(ind2 >= 0) {
            System.out.println(ind2+1);
            ch = sc.next().charAt(0);
            byte b = fun(ch);
            count.set(count.intValue()+1);
            if(a[ind2] == 0 && b == 0) {
                possibleEffectNoByInd2.add(3);possibleEffectNoByInd2.add(4);
            }
            else if(a[ind2] == 0 && b == 1) {
                possibleEffectNoByInd2.add(1);possibleEffectNoByInd2.add(2);
            }
            else if(a[ind2] == 1 && b == 0) {
                possibleEffectNoByInd2.add(1);possibleEffectNoByInd2.add(2);
            }
            else if(a[ind2] == 1 && b == 1) {
                possibleEffectNoByInd2.add(3);possibleEffectNoByInd2.add(4);
            }
        }

        if(possibleEffectNo.isEmpty()) {
            return possibleEffectNoByInd2.iterator().next();
        }
        else if(possibleEffectNoByInd2.isEmpty()) {
            return possibleEffectNo.iterator().next();
        }
        else {
            int common = 1;
            for(int element : possibleEffectNo) {
                if(possibleEffectNoByInd2.contains(element)) {
                    common = element;
                    break;
                }
            }
            return common;
        }

    }
}
