package contests.HackTheInterview4_AsiaPacific;


import java.io.*;
import java.util.*;

public class OptimalNetworkRouting {

    static class MinHeap{

        class HeapNode{
            int vertex; // for unique identification of a node
            int key; // this is key and duplicate key is allowed in minHeap

            public HeapNode(int vertex, int key) {
                this.vertex = vertex;
                this.key = key;
            }
        }

        int capacity;
        int currentSize;
        HeapNode[] mH;
        int [] indexes; //will be used to decrease the key


        public MinHeap(int capacity) {
            this.capacity = capacity;
            mH = new HeapNode[capacity + 1];
            indexes = new int[capacity];
            mH[0] = new HeapNode(-1,Integer.MIN_VALUE);
            currentSize = 0;
        }

        public void display() {
            for (int i = 0; i <=currentSize; i++) {
                System.out.println(" " + mH[i].vertex + "   key   " + mH[i].key);
            }
            System.out.println("________________________");
        }

        public void insert(int vertex, int distance) {
            currentSize++;
            int idx = currentSize;
            mH[idx] = new HeapNode(vertex, distance);
            indexes[vertex] = idx;
            bubbleUp(idx);
        }

        public void decreaseKey(int vertex, int newDisance) {
            mH[indexes[vertex]].key = newDisance;
            bubbleUp(indexes[vertex]);
        }

        private void bubbleUp(int pos) {
            int parentIdx = pos/2;
            int currentIdx = pos;
            while (currentIdx > 0 && mH[parentIdx].key > mH[currentIdx].key) {
                HeapNode currentNode = mH[currentIdx];
                HeapNode parentNode = mH[parentIdx];

                //swap the positions
                indexes[currentNode.vertex] = parentIdx;
                indexes[parentNode.vertex] = currentIdx;
                swap(currentIdx,parentIdx);
                currentIdx = parentIdx;
                parentIdx = parentIdx/2;
            }
        }

        public HeapNode extractMin() {
            HeapNode min = mH[1];
            HeapNode lastNode = mH[currentSize];
//            update the indexes[] and move the last node to the top
            indexes[lastNode.vertex] = 1;
            mH[1] = lastNode;
            mH[currentSize] = null;
            sinkDown(1);
            currentSize--;
            return min;
        }

        private void sinkDown(int k) {
            int smallest = k;
            int leftChildIdx = 2 * k;
            int rightChildIdx = 2 * k+1;
            if (leftChildIdx < heapSize() && mH[smallest].key > mH[leftChildIdx].key) {
                smallest = leftChildIdx;
            }
            if (rightChildIdx < heapSize() && mH[smallest].key > mH[rightChildIdx].key) {
                smallest = rightChildIdx;
            }
            if (smallest != k) {

                HeapNode smallestNode = mH[smallest];
                HeapNode kNode = mH[k];

                //swap the positions
                indexes[smallestNode.vertex] = k;
                indexes[kNode.vertex] = smallest;
                swap(k, smallest);
                sinkDown(smallest);
            }
        }

        private void swap(int a, int b) {
            HeapNode temp = mH[a];
            mH[a] = mH[b];
            mH[b] = temp;
        }

        public boolean isEmpty() {
            return currentSize == 0;
        }

        public int heapSize(){
            return currentSize;
        }
    }

    static class Graph {

        static class Edge {
            int dest;
            int weightt;

            public Edge(int dest, int weightt) {
                this.dest = dest;
                this.weightt = weightt;
            }
        }

        private int v;
        private LinkedList<Edge>[] edges;
        private int[] effort;

        Graph(int v) {
            this.v = v;
            edges = new LinkedList[v];
            for (int i=0;i<v;i++){
                edges[i]=new LinkedList<>();
            }
        }

        public void addEdge(int v1, int v2, int weight) {
            edges[v1].add(new Edge(v2,weight));
            edges[v2].add(new Edge(v1,weight));
        }

        public int computeAns(int dest) {
            return effort[dest];
        }

        public void modified_dijKstra(int src) {
            effort = new int[v];
            boolean[] SPT = new boolean[v];
            for (int i=0; i < v;i++) {
                effort[i] = Integer.MAX_VALUE;
            }
            effort[src] = 0;
            MinHeap minHeap = new MinHeap(v+5);
            for (int i = 0; i < v; i++) {
                minHeap.insert(i, effort[i]);
            }
            while (!minHeap.isEmpty()) {
                MinHeap.HeapNode heapNode = minHeap.extractMin();
                effort[heapNode.vertex] = heapNode.key;
                SPT[heapNode.vertex] = true;
                for (Edge edge : edges[heapNode.vertex]) {
                    if (!SPT[edge.dest]) {
                        if (minHeap.mH[minHeap.indexes[edge.dest]].key > Math.max(heapNode.key, edge.weightt)) {
                            minHeap.decreaseKey(edge.dest, Math.max(heapNode.key, edge.weightt));
                        }
                    }
                }
            }
        }
    }

    static int n,m;

    public static void main (String[] args) {
        InputReader in=new InputReader(System.in);
        OutputWriter out=new OutputWriter(System.out);
        n=in.readInt();m=in.readInt();
        int[][] arr=new int[n][m];
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                arr[i][j]=in.readInt();
            }
        }

        Graph graph = new Graph(n*m);
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                if (isInside(i+1,j)) {
                    graph.addEdge(convert(i,j), convert(i+1,j), Math.abs(arr[i][j] - arr[i+1][j]));
                }
                if (isInside(i,j+1)) {
                    graph.addEdge(convert(i,j), convert(i,j+1), Math.abs(arr[i][j]-arr[i][j+1]));
                }
            }
        }
        graph.modified_dijKstra(0);
        int ans = graph.computeAns(convert(n-1,m-1));
        out.printLine(ans);
        out.close();
    }

    static int convert(int i, int j) {
        return (i*m)+j;
    }

    static boolean isInside(int i, int j) {
        if (i >= 0 && i < n && j >= 0 && j < m) {
            return true;
        }
        return false;
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

