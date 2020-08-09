package ds.OrthogonalRangeTree;

import java.util.*;

/**
 *  NOTE: Has Some Bugs and needs to be tested
 * @param <E>
 */
class ORT_1d<E extends Comparable <E>> {

    private class Node
    {
        E key;
        int height;
        int size; // size of a subtree
        // int count; // allow duplicates
        Collection<E> duplicates;
        Node left , right;

        public Node (E e)
        {
            key = e;
            height = 1;
            size = 1;
            // count = 1;
            left = null;
            right = null;
            duplicates = new HashSet<>(Arrays.asList(e));
        }
    }

    Node root = null;
    // int sz = 0;

    private int szOfSubtree(Node k) {
        if (k == null) {
            return 0;
        }
        return k.size;
    }

    private int getHeight(Node k)
    {
        if(k == null)
            return 0;
        return k.height;
    }

    private Node rightRotate(Node y)
    {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        x.right.left = t2;

        x.right.height = Math.max(getHeight(x.right.left), getHeight(x.right.right));
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        x.right.size = szOfSubtree(x.right.left) + szOfSubtree(x.right.right) + 1;
        x.height = szOfSubtree(x.left) + szOfSubtree(x.right) + 1;

        return x;
    }

    private Node leftRotate(Node x)
    {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        y.left.right = t2;

        y.left.height = Math.max(getHeight(y.left.left), getHeight(y.left.right));
        y.height = Math.max(getHeight(y.left), getHeight(y.right));

        x.left.size = szOfSubtree(y.left.left) + szOfSubtree(y.left.right) + 1;
        y.size = szOfSubtree(y.left) + szOfSubtree(y.right) + 1;

        return y;
    }

    public void insertRec(E key)
    {
        root = insert(root, key);
    }

    private Node insert(Node root, E key)
    {
        if(root == null)
        {
            // sz++;
            return new Node(key);
        }
        else if (root.key.compareTo(key) == 0) {
            root.duplicates.add(key);
            // root.count++;
            root.size++;
        }
        else if(root.key.compareTo(key) > 0)  // root.ele > key
        {
            root.left = insert(root.left, key);
            root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
            root.size = szOfSubtree(root.left) + szOfSubtree(root.right) + 1;
            int balance = Math.abs(getHeight(root.left) - getHeight(root.right));

            if(balance > 1 && root.left.key.compareTo(key) > 0) // root.left.ele > key
            {
                // Single rotation
                root = rightRotate(root);
            }

            if(balance > 1 && root.left.key.compareTo(key) < 0) // root.left.ele < key
            {
                //Double Rotation
                root.left = leftRotate(root.left);
                root = rightRotate(root);
            }

        }
        else if(root.key.compareTo(key) < 0) // root.ele < key
        {
            root.right = insert(root.right, key);
            root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
            root.size = szOfSubtree(root.left) + szOfSubtree(root.right) + 1;
            int balance = Math.abs(getHeight(root.left) - getHeight(root.right));

            if(balance > 1 && root.right.key.compareTo(key) < 0) // root.right.ele < key
            {
                // Single Rotation
                root = leftRotate(root);
            }

            if(balance > 1 && root.right.key.compareTo(key) > 0) //root.right.ele > key
            {
                root.right = rightRotate(root.right);
                root = leftRotate(root);
            }

        }

        return root;
    }

    public void deleteRec(E key)
    {
        root = delete(root,key);
    }

    private Node delete(Node root, E key)
    {
        if(root == null)
            return null;
        else if(root.key.compareTo(key) == 0) // root.ele == key
        {
            if(root.left == null && root.right == null)
            {//sz--;
                return null;}
            else if(root.left == null)
            {//sz--;
                return root.right;}
            else if(root.right == null)
            {// sz--;
                return root.left;}
            else
            {
                root.key = minValue(root.right);
                root.right = delete(root.right , root.key);
            }
        }
        else if(root.key.compareTo(key) > 0) // root.ele > key
        {
            root.left = delete(root.left,key);
            root.height = Math.max(getHeight(root.left), getHeight(root.right));
            root.size = szOfSubtree(root.left) + szOfSubtree(root.right) + 1;
            int balance = Math.abs(getHeight(root.left) - getHeight(root.right));

            if(balance > 1 && (getHeight(root.right.right) >= getHeight(root.right.left)))
            {
                //Single Rotation
                root = leftRotate(root);
            }

            else if(balance > 1 && (getHeight(root.right.right) < getHeight(root.right.left)) )
            {
                //Double Rotation
                root.right = rightRotate(root.right);
                root = leftRotate(root);
            }
        }
        else
        {
            root.right = delete(root.right,key);
            root.height = Math.max(getHeight(root.left), getHeight(root.right));
            root.size = szOfSubtree(root.left) + szOfSubtree(root.right) + 1;
            int balance = Math.abs(getHeight(root.left) - getHeight(root.right));

            if(balance > 1 && (getHeight(root.left.left) >= getHeight(root.left.right)) )
            {
                // Single Rotation
                root = rightRotate(root);

            }

            else if(balance > 1 && (getHeight(root.left.left) < getHeight(root.left.right)))
            {
                // Double Rotation
                root.left = leftRotate(root.left);
                root = rightRotate(root);
            }
        }

        return root;
    }

    /**
     *
     * @param root can't be null
     * @return
     */
    private E minValue(Node root)
    {
        while(root.left != null)
            root = root.left;
        return root.key;
    }

    /**
     *
     * @param root can't be null
     * @return
     */
    private Node findMinimum(Node root)
    {
        while (root.left != null) {
            root = root.left;
        }

        return root;
    }


    /**
     *
     * @param root can't be null
     * @return
     */
    private Node findMaximum(Node root)
    {
        while (root.right != null) {
            root = root.right;
        }

        return root;
    }

    public void inorderRec()
    {
        inorder(root);
    }

    public void inorder(Node root)
    {
        if(root.left != null)
            inorder(root.left);
        System.out.println(root.key);
        if(root.right != null)
            inorder(root.right);
    }

    public int size() {
        return szOfSubtree(root);
    }

    public boolean isEmpty() {
        return size() == 0;
    }


    /**
     *  returns least element greater than key if present otherwise returns null
     * @param key
     * @return
     */
    public E findSuccessor(E key) {
        Node successor = findSuccessor(root, key);
        if (successor == null) {
            return null;
        }
        return successor.key;
    }

    // Iterative function to find inorder successor for given key in a BST
    private Node findSuccessor(Node root, E key)
    {
        Node succ = null;

        if (root == null) {
            return null;
        }

        while (true)
        {
            // if given key is less than the root node, visit left subtree
            if (key.compareTo(root.key) < 0) // key < root.key
            {
                // update successor to current node before visiting
                // left subtree
                succ = root;
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key.compareTo(root.key) > 0) // key > root.key
            {
                root = root.right;
            }

            // if node with key's value is found, the successor is minimum
            // value node in its right subtree (if any)
            else
            {
                if (root.right != null) {
                    succ = findMinimum(root.right);
                }
                break;
            }

            // if reaches leaf node
            if (root == null) {
                break;
            }

        }

        return succ;
    }

    /**
     *  returns least element greater than or Equal to key if present otherwise returns null
     * @param key
     * @return
     */
    public E findSuccessorWithEqualityConsideration(E key) {
        Node successor = findSuccessorWithEqualityConsideration(root, key);
        if (successor == null) {
            return null;
        }
        return successor.key;
    }

    private Node findSuccessorWithEqualityConsideration(Node root, E key)
    {
        Node succ = null;

        if (root == null) {
            return null;
        }

        while (true)
        {
            // if given key is less than the root node, visit left subtree
            if (key.compareTo(root.key) < 0) // key < root.key
            {
                // update successor to current node before visiting
                // left subtree
                succ = root;
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key.compareTo(root.key) > 0) // key > root.key
            {
                root = root.right;
            }

            else
            {
                succ = root;
                break;
            }

            // if reaches leaf node
            if (root == null) {
                break;
            }

        }

        return succ;
    }

    /**
     *  returns greatest element lesser than key if present otherwise returns null
     * @param key
     * @return
     */
    public E findPredecessor(E key) {
        Node successor = findSuccessor(root, key);
        if (successor == null) {
            return null;
        }
        return successor.key;
    }

    private Node findPredecessor(Node root, E key)
    {
        Node predecessor = null;

        if (root == null) {
            return null;
        }

        while (true)
        {
            // if given key is less than the root node, visit left subtree
            if (key.compareTo(root.key) < 0) // key < root.key
            {
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key.compareTo(root.key) > 0) // key > root.key
            {
                // update predecessor to current node before visiting
                // right subtree
                predecessor = root;
                root = root.right;
            }

            // if node with key's value is found, the predecessor is maximum
            // value node in its left subtree (if any)
            else
            {
                if (root.right != null) {
                    predecessor = findMaximum(root.right);
                }
                break;
            }

            // if reaches leaf node
            if (root == null) {
                break;
            }

        }

        return predecessor;
    }

    /**
     *  returns greatest element lesser than or Equal to key if present otherwise returns null
     * @param key
     * @return
     */
    public E findPredecessorWithEqualityConsideration(E key) {
        Node successor = findPredecessorWithEqualityConsideration(root, key);
        if (successor == null) {
            return null;
        }
        return successor.key;
    }

    private Node findPredecessorWithEqualityConsideration(Node root, E key)
    {
        Node predecessor = null;

        if (root == null) {
            return null;
        }

        while (true)
        {
            // if given key is less than the root node, visit left subtree
            if (key.compareTo(root.key) < 0) // key < root.key
            {
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key.compareTo(root.key) > 0) // key > root.key
            {
                // update predecessor to current node before visiting
                // right subtree
                predecessor = root;
                root = root.right;
            }

            // if node with key's value is found, the predecessor is found
            else
            {
                predecessor = root;
                break;
            }

            // if reaches leaf node
            if (root == null) {
                break;
            }

        }

        return predecessor;
    }

    /**
     * lower <= upper
     * @param lower
     * @param upper
     * @param isLowerInclusive true if closed interval
     * @param isUpperInclusive true if closed interval
     * @return
     */
    public long noOfElements(E lower, E upper, boolean isLowerInclusive, boolean isUpperInclusive) {
        Node lowerNode = isLowerInclusive ? findSuccessorWithEqualityConsideration(root, lower) : findSuccessor(root, lower);
        Node upperNode = isUpperInclusive ? findPredecessorWithEqualityConsideration(root, upper) : findPredecessor(root, upper);
        if (lowerNode == null || upperNode == null) {
            return 0;
        }
        return sweep(lowerNode, upperNode);
    }

    /**
     *
     * @param lower
     * @param upper
     * @return no of elements(or nodes) from lower.key to upper.key both inclusive
     */
    private long sweep(Node lower, Node upper) {
        Node lca = lca(lower.key, upper.key);

        // both lca is same as lower and upper
        if (lower.key.compareTo(upper.key) == 0) {
            return lca.duplicates.size();
        }
        // lca is parent of lower
        else if (upper.key.compareTo(lca.key) == 0) {
            return lca.duplicates.size() + left(lca.left, lower);
        }
        // lca is parent of upper
        else if (lower.key.compareTo(lca.key) == 0) {
            return lca.duplicates.size()+right(lca.right, upper);
        }
        else {
            return lca.duplicates.size()+left(lca.left, lower)+right(lca.right, upper);
        }
    }

    /**
     * node is ancestor of or equal to lower
     * @param node should not be null
     * @param lower should not be null
     * @return no of elements greater than or equal to lower.key from lower node to ancestor node(both inclusive) travelling up the tree
     */
    private long left(Node node, Node lower) {
        long result = 0;
        do {
            if (node.key.compareTo(lower.key) < 0) {
                node = node.right;
            }
            else if (node.key.compareTo(lower.key) > 0){
                result += szOfSubtree(node.right) + node.duplicates.size();
                node = node.left;
            }
            else {
                result += szOfSubtree(node.right) + node.duplicates.size();
            }
        }
        while (node.key.compareTo(lower.key) != 0);
        return result;
    }


    /**
     * node is ancestor of or equal to upper
     * @param node should not be null
     * @param upper should not be null
     * @return no of elements lesser than or equal to upper.key from upper node to ancestor node(both inclusive) travelling up the tree
     */
    private long right(Node node, Node upper) {
        long result = 0;
        do {
            if (node.key.compareTo(upper.key) < 0) {
                result += szOfSubtree(node.left) + node.duplicates.size();
                node = node.right;
            }
            else if (node.key.compareTo(upper.key) > 0){
                node = node.left;
            }
            else {
                result += szOfSubtree(node.left) + node.duplicates.size();
            }
        }
        while (node.key.compareTo(upper.key) != 0);
        return result;
    }

    /**
     * Assumed both key1 and key2 are present in the bst and key1 <= key2
     * @param key1
     * @param key2
     * @return
     */
    private Node lca(E key1, E key2) {
        Node node = root;
        while (node != null) {
            if (node.key.compareTo(key1) > 0 && node.key.compareTo(key2) > 0) {
                node = node.left;
            }
            else if (node.key.compareTo(key1) < 0 && node.key.compareTo(key2) < 0) {
                node = node.right;
            }
            else {
                break;
            }
        }
        return node;
    }

    /**
     *
     * @param key
     * @return rank of key stating from zero. Ex: rank of minimum element will be zero. Returns -1 if key is not found in the tree
     */
    public int rank(E key) {
        return rank(root, key);
    }

    private int rank(Node root, E key) {
        if (root == null) {
            return -1;
        }
        else if (root.key.compareTo(key) == 0) {
            return szOfSubtree(root.left);
        }
        else if (root.key.compareTo(key) < 0) {
            int rank = rank(root.right, key);
            if (rank != -1) {
                return szOfSubtree(root.left) + rank + root.duplicates.size();
            }
        }
        return -1;
    }

    /**
     *
     * @param i
     * @return key element whose rank(i) = key. Ex : for i == 0 , return minimum key. If i == tree.size(), return null.
     */
    public E select(int i) {
        Node node = root;
        while (node != null) {
            int rank = szOfSubtree(node.left);
            if (i == rank) {
                return node.key;
            }
            else if (i < rank) {
                node = node.left;
            }
            else {
                node = node.right;
                i -= (rank+node.duplicates.size());
                if (i < 0) {
                    return node.key;
                }
            }
        }
        return null;
    }

}

class Pair<E1 extends Comparable<E1>, E2 extends Comparable<E2>> implements Comparable<Pair<E1,E2>> {
    E1 firstCoordinate;
    E2 secondCoordinate;

    public Pair(E1 firstCoordinate, E2 secondCoordinate) {
        this.firstCoordinate = firstCoordinate;
        this.secondCoordinate = secondCoordinate;
    }

    @Override
    public int compareTo(Pair<E1,E2> o) {
        return this.secondCoordinate.compareTo(o.secondCoordinate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return firstCoordinate.equals(pair.firstCoordinate) &&
                secondCoordinate.equals(pair.secondCoordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCoordinate, secondCoordinate);
    }

}

/**
 *  Allows only static points
 *
 * @param <E1>  first or x coordinate
 * @param <E2>  second or y coordinate
 */
public class ORT_2D<E1 extends Comparable<E1>, E2 extends Comparable<E2>> {

    private class Node  {
        E1 key;
        int height;
        int size;
        Node left, right;
        ORT_1d<E2> pairsWithSameKey; // contains only the points with same firstCoordinate
        ORT_1d<Pair<E1,E2>> ortBySecondCoordinate; // contains ort of all points in that subtree including this node

        public Node(E1 key) {
            this.key = key;
            height = 1;
            size = 0; // no of points in the subtree
            left = null;
            right = null;
            ortBySecondCoordinate = new ORT_1d<>();
            pairsWithSameKey = new ORT_1d<>();
        }
    }

    /**
     *  call this constructor with array of points
     * @param pairs
     */
    public ORT_2D(Pair<E1,E2>[] pairs) {
        Set<E1> firstCoordinates = new HashSet<>();
        // construct 1D range tree by firsrCoordinate
        for (Pair<E1,E2> pair : pairs) {
            if (!firstCoordinates.contains(pair.firstCoordinate)) {
                firstCoordinates.add(pair.firstCoordinate);
                insertRec(pair.firstCoordinate);
            }
        }
        // insert all points in constructed 1D range tree
        for (Pair<E1,E2> pair : pairs) {
            insertRec(root, pair.firstCoordinate, pair.secondCoordinate);
        }
    }

    Node root = null;

    private int szOfSubtree(Node k) {
        if (k == null) {
            return 0;
        }
        return k.size;
    }

    private int getHeight(Node k)
    {
        if(k == null)
            return 0;
        return k.height;
    }

    private Node rightRotate(Node y)
    {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        x.right.left = t2;

        x.right.height = Math.max(getHeight(x.right.left), getHeight(x.right.right));
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        x.right.size = szOfSubtree(x.right.left) + szOfSubtree(x.right.right) + 1;
        x.height = szOfSubtree(x.left) + szOfSubtree(x.right) + 1;

        return x;
    }

    private Node leftRotate(Node x)
    {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        y.left.right = t2;

        y.left.height = Math.max(getHeight(y.left.left), getHeight(y.left.right));
        y.height = Math.max(getHeight(y.left), getHeight(y.right));

        x.left.size = szOfSubtree(y.left.left) + szOfSubtree(y.left.right) + 1;
        y.size = szOfSubtree(y.left) + szOfSubtree(y.right) + 1;

        return y;
    }


    public void insertRec(E1 firstCoordinate)
    {
        root = insert(root, firstCoordinate);
    }

    private Node insert(Node root, E1 firstCoordinate)
    {
        if(root == null)
        {
            return new Node(firstCoordinate);
        }
        else if (root.key.compareTo(firstCoordinate) == 0) {
            root.size++;
        }
        else if(root.key.compareTo(firstCoordinate) > 0)  // root.ele > firstCoordinate
        {
            root.left = insert(root.left, firstCoordinate);
            root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
            root.size = szOfSubtree(root.left) + szOfSubtree(root.right) + 1;
            int balance = Math.abs(getHeight(root.left) - getHeight(root.right));

            if(balance > 1 && root.left.key.compareTo(firstCoordinate) > 0) // root.left.ele > firstCoordinate
            {
                // Single rotation
                root = rightRotate(root);
            }

            if(balance > 1 && root.left.key.compareTo(firstCoordinate) < 0) // root.left.ele < firstCoordinate
            {
                //Double Rotation
                root.left = leftRotate(root.left);
                root = rightRotate(root);
            }

        }
        else if(root.key.compareTo(firstCoordinate) < 0) // root.ele < firstCoordinate
        {
            root.right = insert(root.right, firstCoordinate);
            root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
            root.size = szOfSubtree(root.left) + szOfSubtree(root.right) + 1;
            int balance = Math.abs(getHeight(root.left) - getHeight(root.right));

            if(balance > 1 && root.right.key.compareTo(firstCoordinate) < 0) // root.right.ele < firstCoordinate
            {
                // Single Rotation
                root = leftRotate(root);
            }

            if(balance > 1 && root.right.key.compareTo(firstCoordinate) > 0) //root.right.ele > firstCoordinate
            {
                root.right = rightRotate(root.right);
                root = leftRotate(root);
            }

        }

        return root;
    }

    public void insertRec(Node root, E1 firstCoordinate, E2 secondCoordinate) {
        if (root.key.compareTo(firstCoordinate) == 0) {
            root.pairsWithSameKey.insertRec(secondCoordinate);
            root.ortBySecondCoordinate.insertRec(new Pair<>(firstCoordinate, secondCoordinate));
        }
        else if (root.key.compareTo(firstCoordinate) > 0) {
            // left subtree
            root.ortBySecondCoordinate.insertRec(new Pair<>(firstCoordinate, secondCoordinate));
            insertRec(root.left, firstCoordinate, secondCoordinate);
        }
        else {
            // right subtree
            root.ortBySecondCoordinate.insertRec(new Pair<>(firstCoordinate, secondCoordinate));
            insertRec(root.right, firstCoordinate, secondCoordinate);
        }
        root.size++;
    }

    /**
     *
     * @param root can't be null
     * @return
     */
    private Node findMinimum(Node root)
    {
        while (root.left != null) {
            root = root.left;
        }

        return root;
    }


    /**
     *
     * @param root can't be null
     * @return
     */
    private Node findMaximum(Node root)
    {
        while (root.right != null) {
            root = root.right;
        }

        return root;
    }


    /**
     *  returns least element greater than key if present otherwise returns null
     * @param key
     * @return
     */
    public E1 findSuccessor(E1 key) {
        Node successor = findSuccessor(root, key);
        if (successor == null) {
            return null;
        }
        return successor.key;
    }

    // Iterative function to find inorder successor for given key in a BST
    private Node findSuccessor(Node root, E1 key)
    {
        Node succ = null;

        if (root == null) {
            return null;
        }

        while (true)
        {
            // if given key is less than the root node, visit left subtree
            if (key.compareTo(root.key) < 0) // key < root.key
            {
                // update successor to current node before visiting
                // left subtree
                succ = root;
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key.compareTo(root.key) > 0) // key > root.key
            {
                root = root.right;
            }

            // if node with key's value is found, the successor is minimum
            // value node in its right subtree (if any)
            else
            {
                if (root.right != null) {
                    succ = findMinimum(root.right);
                }
                break;
            }

            // if reaches leaf node
            if (root == null) {
                break;
            }

        }

        return succ;
    }

    /**
     *  returns least element greater than or Equal to key if present otherwise returns null
     * @param key
     * @return
     */
    public E1 findSuccessorWithEqualityConsideration(E1 key) {
        Node successor = findSuccessorWithEqualityConsideration(root, key);
        if (successor == null) {
            return null;
        }
        return successor.key;
    }

    private Node findSuccessorWithEqualityConsideration(Node root, E1 key)
    {
        Node succ = null;

        if (root == null) {
            return null;
        }

        while (true)
        {
            // if given key is less than the root node, visit left subtree
            if (key.compareTo(root.key) < 0) // key < root.key
            {
                // update successor to current node before visiting
                // left subtree
                succ = root;
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key.compareTo(root.key) > 0) // key > root.key
            {
                root = root.right;
            }

            else
            {
                succ = root;
                break;
            }

            // if reaches leaf node
            if (root == null) {
                break;
            }

        }

        return succ;
    }

    /**
     *  returns greatest element lesser than key if present otherwise returns null
     * @param key
     * @return
     */
    public E1 findPredecessor(E1 key) {
        Node successor = findSuccessor(root, key);
        if (successor == null) {
            return null;
        }
        return successor.key;
    }

    private Node findPredecessor(Node root, E1 key)
    {
        Node predecessor = null;

        if (root == null) {
            return null;
        }

        while (true)
        {
            // if given key is less than the root node, visit left subtree
            if (key.compareTo(root.key) < 0) // key < root.key
            {
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key.compareTo(root.key) > 0) // key > root.key
            {
                // update predecessor to current node before visiting
                // right subtree
                predecessor = root;
                root = root.right;
            }

            // if node with key's value is found, the predecessor is maximum
            // value node in its left subtree (if any)
            else
            {
                if (root.right != null) {
                    predecessor = findMaximum(root.right);
                }
                break;
            }

            // if reaches leaf node
            if (root == null) {
                break;
            }

        }

        return predecessor;
    }

    /**
     *  returns greatest element lesser than or Equal to key if present otherwise returns null
     * @param key
     * @return
     */
    public E1 findPredecessorWithEqualityConsideration(E1 key) {
        Node successor = findPredecessorWithEqualityConsideration(root, key);
        if (successor == null) {
            return null;
        }
        return successor.key;
    }

    private Node findPredecessorWithEqualityConsideration(Node root, E1 key)
    {
        Node predecessor = null;

        if (root == null) {
            return null;
        }

        while (true)
        {
            // if given key is less than the root node, visit left subtree
            if (key.compareTo(root.key) < 0) // key < root.key
            {
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key.compareTo(root.key) > 0) // key > root.key
            {
                // update predecessor to current node before visiting
                // right subtree
                predecessor = root;
                root = root.right;
            }

            // if node with key's value is found, the predecessor is found
            else
            {
                predecessor = root;
                break;
            }

            // if reaches leaf node
            if (root == null) {
                break;
            }

        }

        return predecessor;
    }

    /**
     * lowerByFirstCoordinate <= upperByFirstCoordinate and lowerBySecondCoordinate <= upperBySecondCoordinate
     * @param lowerByFirstCoordinate
     * @param upperByFirstCoordinate
     * @param lowerBySecondCoordinate
     * @param upperBySecondCoordinate
     * @param islowerByFirstCoordinateInclusive
     * @param isupperByFirstCoordinateInclusive
     * @param islowerBySecondCoordinateInclusive
     * @param isupperBySecondCoordinateInclusive
     * @return  return no of elements inside the box
     */
    public long noOfElements(E1 lowerByFirstCoordinate, E1 upperByFirstCoordinate, E2 lowerBySecondCoordinate, E2 upperBySecondCoordinate, boolean islowerByFirstCoordinateInclusive, boolean isupperByFirstCoordinateInclusive, boolean islowerBySecondCoordinateInclusive, boolean isupperBySecondCoordinateInclusive) {
        Node lowerNodeByFirstCoordinate = islowerByFirstCoordinateInclusive ? findSuccessorWithEqualityConsideration(root, lowerByFirstCoordinate) : findSuccessor(root, lowerByFirstCoordinate);
        Node upperNodeByFirstCoordinate = isupperByFirstCoordinateInclusive ? findPredecessorWithEqualityConsideration(root, upperByFirstCoordinate) : findPredecessor(root, upperByFirstCoordinate);
        if (lowerNodeByFirstCoordinate == null || upperNodeByFirstCoordinate == null) {
            return 0;
        }
        return sweep(lowerNodeByFirstCoordinate, upperNodeByFirstCoordinate, lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
    }

    /**
     *
     * @param lower
     * @param upper
     * @param lowerBySecondCoordinate
     * @param upperBySecondCoordinate
     * @param islowerBySecondCoordinateInclusive
     * @param isupperBySecondCoordinateInclusive
     * @return no of elements from lower.key to upper.key both inclusive satisfying second coordinates also
     */
    private long sweep(Node lower, Node upper, E2 lowerBySecondCoordinate, E2 upperBySecondCoordinate, boolean islowerBySecondCoordinateInclusive, boolean isupperBySecondCoordinateInclusive) {
        Node lca = lca(lower.key, upper.key);

        // both lca is same as lower and upper
        if (lower.key.compareTo(upper.key) == 0) {
            return lca.pairsWithSameKey.noOfElements(lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
        }
        // lca is parent of lower
        else if (upper.key.compareTo(lca.key) == 0) {
            return lca.pairsWithSameKey.noOfElements(lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive)
                    + left(lca.left, lower, lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
        }
        // lca is parent of upper
        else if (lower.key.compareTo(lca.key) == 0) {
            return lca.pairsWithSameKey.noOfElements(lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive)
                    + right(lca.right, upper, lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
        }
        else {
            return lca.pairsWithSameKey.noOfElements(lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive)
                    + left(lca.left, lower, lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive)
                    + right(lca.right, upper, lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
        }
    }

    /**
     * node is ancestor of or equal to lower
     * @param node should not be null
     * @param lower should not be null
     * @return no of elements greater than or equal to lower.key from lower node to ancestor node(both inclusive) travelling up the tree satisfying second coordinates also
     */
    private long left(Node node, Node lower, E2 lowerBySecondCoordinate, E2 upperBySecondCoordinate, boolean islowerBySecondCoordinateInclusive, boolean isupperBySecondCoordinateInclusive) {
        long result = 0;
        do {
            if (node.key.compareTo(lower.key) < 0) {
                node = node.right;
            }
            else if (node.key.compareTo(lower.key) > 0){
                // result += szOfSubtree(node.right) + 1;
                result += node.pairsWithSameKey.noOfElements(lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
                result += node.right.ortBySecondCoordinate.noOfElements(new Pair<>(node.key, lowerBySecondCoordinate), new Pair<>(node.key, upperBySecondCoordinate), islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
                node = node.left;
            }
            else {
                // result += szOfSubtree(node.right) + 1;
                result += node.pairsWithSameKey.noOfElements(lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
                result += node.right.ortBySecondCoordinate.noOfElements(new Pair<>(node.key, lowerBySecondCoordinate), new Pair<>(node.key, upperBySecondCoordinate), islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
            }
        }
        while (node.key.compareTo(lower.key) != 0);
        return result;
    }


    /**
     * node is ancestor of or equal to upper
     * @param node should not be null
     * @param upper should not be null
     * @return no of elements lesser than or equal to upper.key from upper node to ancestor node(both inclusive) travelling up the tree satisfying second coordinates also
     */
    private long right(Node node, Node upper, E2 lowerBySecondCoordinate, E2 upperBySecondCoordinate, boolean islowerBySecondCoordinateInclusive, boolean isupperBySecondCoordinateInclusive) {
        long result = 0;
        do {
            if (node.key.compareTo(upper.key) < 0) {
                // result += szOfSubtree(node.left) + 1;
                result += node.pairsWithSameKey.noOfElements(lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
                result += node.left.ortBySecondCoordinate.noOfElements(new Pair<>(node.key, lowerBySecondCoordinate), new Pair<>(node.key, upperBySecondCoordinate), islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
                node = node.right;
            }
            else if (node.key.compareTo(upper.key) > 0){
                node = node.left;
            }
            else {
                // result += szOfSubtree(node.left) + 1;
                result += node.pairsWithSameKey.noOfElements(lowerBySecondCoordinate, upperBySecondCoordinate, islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
                result += node.left.ortBySecondCoordinate.noOfElements(new Pair<>(node.key, lowerBySecondCoordinate), new Pair<>(node.key, upperBySecondCoordinate), islowerBySecondCoordinateInclusive, isupperBySecondCoordinateInclusive);
            }
        }
        while (node.key.compareTo(upper.key) != 0);
        return result;
    }

    /**
     * Assumed both key1 and key2 are present in the bst and key1 <= key2
     * @param key1
     * @param key2
     * @return
     */
    private Node lca(E1 key1, E1 key2) {
        Node node = root;
        while (node != null) {
            if (node.key.compareTo(key1) > 0 && node.key.compareTo(key2) > 0) {
                node = node.left;
            }
            else if (node.key.compareTo(key1) < 0 && node.key.compareTo(key2) < 0) {
                node = node.right;
            }
            else {
                break;
            }
        }
        return node;
    }

    long ans = Long.MAX_VALUE;
    public long compute(int n, int h, int v) {
        ans = Integer.MAX_VALUE;
        compute(root, n, h, v);
        if (ans != Long.MAX_VALUE) {
            return ans;
        }
        return -1;
    }

    private void compute(Node root, int n, int h, int v) {
        if (root == null) {
            return;
        }

        int pt1 = root.pairsWithSameKey.size();
        int pt2 = 0;
        if (root.left != null) {
            pt2 = root.left.size;
        }

        if ((pt1 + pt2) <= h) {
            if ((n - (pt1 + pt2)) <= v) {
                long maxSecondCoordinateInRightSubtree = 0;
                if (root.right != null) {
                    maxSecondCoordinateInRightSubtree = (Long) root.right.ortBySecondCoordinate.select(root.right.ortBySecondCoordinate.size()-1).secondCoordinate;
                }
                long maxFirstCoordinateInLeftSubtree = (Long) root.key;
                ans = Math.min(ans, maxFirstCoordinateInLeftSubtree+maxSecondCoordinateInRightSubtree);
            }
        }
        else if (pt2 < h && (pt1+pt2) > h) {
            if ((n-h) <= v) {
                long maxSecondCoordinateInRightSubtree = 0;
                if (root.right != null) {
                    maxSecondCoordinateInRightSubtree = (Long) root.right.ortBySecondCoordinate.select(root.right.ortBySecondCoordinate.size()-1).secondCoordinate;
                }
                long maxVertical = Math.max(maxSecondCoordinateInRightSubtree, (Long) root.pairsWithSameKey.select(root.pairsWithSameKey.size() - (h-pt2+1)));
                long maxHorizontal = (Long) root.key;
                ans = Math.min(ans, maxHorizontal+maxVertical);
            }
        }
        compute(root.left, n, h, v);
        compute(root.right, n, h, v);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for(int z = 1; z <= t; z++) {
            long ans;
            int n = sc.nextInt(), h = sc.nextInt(), v = sc.nextInt();
            long x1 = sc.nextLong(), x2 = sc.nextLong(), ax = sc.nextLong(), bx = sc.nextLong(), cx = sc.nextLong(), dx = sc.nextLong();
            long y1 = sc.nextLong(), y2 = sc.nextLong(), ay = sc.nextLong(), by = sc.nextLong(), cy = sc.nextLong(), dy = sc.nextLong();
            Pair<Long, Long>[] pairs = new Pair[n];
            pairs[0] = new Pair<>(x1, y1);
            pairs[1] = new Pair<>(x2, y2);
            for (int i = 2; i < n; i++) {
                long k1 = (ax*pairs[i-2].firstCoordinate) % dx;
                long k2 = (bx*pairs[i-1].secondCoordinate) % dx;
                long xi = ((k1 + k2 + cx) % dx) + 1;

                long k3 = (ay*pairs[i-2].secondCoordinate) % dy;
                long k4 = (by*pairs[i-1].secondCoordinate) % dy;
                long yi = ((k3 + k4 + cy) % dy) + 1;

                pairs[i] = new Pair<>(xi, yi);
            }
            ORT_2D<Long, Long> ort_2D = new ORT_2D<>(pairs);
            ans = ort_2D.compute(n,h,v);
            System.out.println("Case #" + z + ": " + ans);
        }
    }

}

