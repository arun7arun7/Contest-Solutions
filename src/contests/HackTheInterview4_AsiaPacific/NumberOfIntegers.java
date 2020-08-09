package contests.HackTheInterview4_AsiaPacific;

import java.io.*;
import java.util.*;

public class NumberOfIntegers {


    static String l,r;
    static int k;
    static long[][][] dp;
    static int con = 1000000007;

    static long mod(long a)
    {
        return (a%con + con) % con;

    }

    static long add(long a, long b) {
        return mod(mod(a) + mod(b));
    }

    static long sub(long a, long b) {
        return mod(mod(a) - mod(b));
    }

    static long func( int i, int k, boolean isLesser) {
        if (k==0){
            return 1;
        }
        if (i==l.length()) {
            return 0;
        }
        if (dp[i][k][isLesser?1:0] != -1) {
            return dp[i][k][isLesser?1:0];
        }
        char maxDigit = isLesser  ? '9' : l.charAt(i);
        int maxDig = Character.getNumericValue(maxDigit);

        long ans = 0;
        for (int j=0;j<=maxDig;j++){
            if (j == maxDig) {
                ans = add(ans, func(i+1,j==0?k:k-1,isLesser));
            }
            else {
                ans = add(ans, func(i+1,j==0?k:k-1,true));
            }
        }
        dp[i][k][isLesser?1:0] = ans;
        return ans;
    }

    static long func1( int i, int k, boolean isLesser) {
        if (k==0){
            return 1;
        }
        if (i==r.length()) {
            return 0;
        }
        if (dp[i][k][isLesser?1:0] != -1) {
            return dp[i][k][isLesser?1:0];
        }
        char maxDigit = isLesser  ? '9' : r.charAt(i);
        int maxDig = Character.getNumericValue(maxDigit);

        long ans = 0;
        for (int j=0;j<=maxDig;j++){
            if (j == maxDig) {
                ans = add(ans, func1(i+1,j==0?k:k-1,isLesser));
            }
            else {
                ans = add(ans, func1(i+1,j==0?k:k-1,true));
            }
        }
        dp[i][k][isLesser?1:0] = ans;
        return ans;
    }


    public static void main (String[] args) {
        InputReader in=new InputReader(System.in);
        OutputWriter out=new OutputWriter(System.out);
        l = in.next();r=in.next();k=in.readInt();
        dp = new long[105][105][2];
        for (int i=0;i<dp.length;i++){
            for (int j=0;j<dp[0].length;j++){
                for (int k=0;k<2;k++){
                    dp[i][j][k]=-1;
                }
            }
        }
        long r1 = func1(0,k,false);
        for (int i=0;i<dp.length;i++){
            for (int j=0;j<dp[0].length;j++){
                for (int k=0;k<2;k++){
                    dp[i][j][k]=-1;
                }
            }
        }
        long l1 = func(0,k,false);
        long ans = sub( r1, l1);
        out.printLine(ans);
        out.close();
    }

    static class InputReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private SpaceCharFilter filter;
        public InputReader(InputStream stream) {
            this.stream = stream;
        }
        public int read() {
            if (numChars == -1)
                throw new InputMismatchException();
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }
        public int readInt() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }
        public String readString() {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }
        public boolean isSpaceChar(int c) {
            if (filter != null)
                return filter.isSpaceChar(c);
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }
        public String next() {
            return readString();
        }
        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);
        }
    }

    static class OutputWriter {
        private final PrintWriter writer;
        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }
        public OutputWriter(Writer writer) {
            this.writer = new PrintWriter(writer);
        }
        public void print(Object...objects) {
            for (int i = 0; i < objects.length; i++) {
                if (i != 0)
                    writer.print(' ');
                writer.print(objects[i]);
            }
        }
        public void printLine(Object...objects) {
            print(objects);
            writer.println();
        }
        public void close() {
            writer.close();
        }
        public void flush() {
            writer.flush();
        }
    }
    static class IOUtils {
        public static int[] readIntArray(InputReader in, int size) {
            int[] array = new int[size];
            for (int i = 0; i < size; i++)
                array[i] = in.readInt();
            return array;
        }
    }
}
