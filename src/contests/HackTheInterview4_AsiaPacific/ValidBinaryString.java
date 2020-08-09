package contests.HackTheInterview4_AsiaPacific;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;


public class ValidBinaryString {

    public static void main (String[] args) {
        InputReader in=new InputReader(System.in);
        OutputWriter out=new OutputWriter(System.out);
        String s=in.next();
        int d=in.readInt();
        List<Integer> list=new ArrayList<>();
        int coont=0;
        for (int i=0;i<s.length();i++){
            if (s.charAt(i)=='0'){
                coont++;
            }
            else {
                if (coont > 0) {
                    list.add(coont);
                }
                coont=0;
            }
        }
        if (coont > 0) {
            list.add(coont);
        }
        int ans=0;
        for (int i=0;i<list.size();i++){
            ans+=(int)Math.floor((double)list.get(i)/(double)d);
        }
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

