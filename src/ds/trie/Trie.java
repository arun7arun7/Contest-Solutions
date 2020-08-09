package ds.trie;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;


public class Trie {

    class Key {
        char c;

        public Key(char ch) {
            c = ch;
        }

        @Override
        public boolean equals(Object o) {
            Key key = (Key)o;
            return c == key.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(c);
        }
    }

    class Node {
        Key key;
        Map<Key, Node> children;
        boolean isEndWord;

        public Node(Key key) {
            this.key = key;
            children = new HashMap();
            isEndWord = false;
        }

        public Node findChild(Key k) {
            if (children.containsKey(k)) {
                return children.get(k);
            }
            return null;
        }

        public void addChild(Node node) {
            children.put(node.key, node);
        }

    }

    Node root;

    public Trie() {
        root = new Node(new Key('\0'));
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        root = insert(root, 0, word);
    }

    private Node insert(Node cur, int index, String word) {
        Node newChildNode;
        Node matchingChild = cur.findChild(new Key(word.charAt(index)));
        if (matchingChild != null) {
            if (index == word.length()-1) {
                matchingChild.isEndWord = true;
                cur.addChild(matchingChild);
                return cur;
            }
            newChildNode = insert(matchingChild, index+1, word);
            cur.addChild(newChildNode);
        }
        else {
            newChildNode = new Node(new Key(word.charAt(index)));
            if (index == word.length()-1) {
                newChildNode.isEndWord = true;
                cur.addChild(newChildNode);
                return cur;
            }
            cur.addChild(insert(newChildNode, index+1, word));
        }
        return cur;
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        Node cur = root;
        for (int i = 0; i < word.length(); i++) {
            Node child = cur.findChild(new Key(word.charAt(i)));
            if (child == null) {
                return false;
            }
            cur = child;
        }
        return cur.isEndWord;
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        Node cur = root;
        for (int i = 0; i < prefix.length(); i++) {
            Node child = cur.findChild(new Key(prefix.charAt(i)));
            if (child == null) {
                return false;
            }
            cur = child;
        }
        return true;
    }

    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("app");
        trie.insert("appef");
        trie.insert("beff");
        System.out.println("Search apple = " + trie.search("apple"));
        System.out.println("Search app = " + trie.search("app"));
        System.out.println("Search appef = " + trie.search("appef"));
        System.out.println("startsWith appef = " + trie.startsWith("appef"));
        System.out.println("startsWith appefjk = " + trie.startsWith("appefjk"));
        System.out.println("search appefjk = " + trie.search("appefjk"));
        System.out.println("search a = " + trie.search("a"));
        System.out.println("startsWith a = " + trie.startsWith("a"));
        System.out.println("search beff = " + trie.search("beff"));
        System.out.println("startsWith beff = " + trie.startsWith("beff"));
        System.out.println("startsWith b = " + trie.startsWith("b"));
        System.out.println("search befff = " + trie.search("befff"));
        System.out.println("startsWith befff = " + trie.startsWith("befff"));
    }
}

