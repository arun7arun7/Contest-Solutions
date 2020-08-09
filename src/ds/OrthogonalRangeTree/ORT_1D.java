package ds.OrthogonalRangeTree;


/**
 *  NOTE: Has Some Bugs and needs to be tested
 * @param <E>
 */
public class ORT_1D<E extends Comparable <E>> {

    private class Node
    {
        E key;
        int height;
        int size; // size of a subtree
        int count; // allow duplicates
        Node left , right;

        public Node (E e)
        {
            key = e;
            height = 1;
            size = 1;
            count = 1;
            left = null;
            right = null;
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
            root.count++;
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
            return 1;
        }
        // lca is parent of lower
        else if (upper.key.compareTo(lca.key) == 0) {
            return 1+left(lca.left, lower);
        }
        // lca is parent of upper
        else if (lower.key.compareTo(lca.key) == 0) {
            return 1+right(lca.right, upper);
        }
        else {
            return 1+left(lca.left, lower)+right(lca.right, upper);
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
                result += szOfSubtree(node.right) + 1;
                node = node.left;
            }
            else {
                result += szOfSubtree(node.right) + 1;
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
                result += szOfSubtree(node.left) + 1;
                node = node.right;
            }
            else if (node.key.compareTo(upper.key) > 0){
                node = node.left;
            }
            else {
                result += szOfSubtree(node.left) + 1;
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

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ORT_1D<Integer> tree = new ORT_1D<>();
        tree.insertRec(50);
        tree.insertRec(30);
        tree.insertRec(20);
        tree.insertRec(40);
        tree.insertRec(70);
        tree.insertRec(60);
        tree.insertRec(80);

        System.out.println("\nTree Size: ");
        System.out.println(tree.size());

        System.out.println("Inorder traversal of the given tree");
        tree.inorderRec();

        System.out.println("\nDelete 20");
        tree.deleteRec(20);
        System.out.println("Inorder traversal of the modified tree");
        tree.inorderRec();

        System.out.println("\nTree Size: ");
        System.out.println(tree.size());

        System.out.println("\nDelete 30");
        tree.deleteRec(30);
        System.out.println("Inorder traversal of the modified tree");
        tree.inorderRec();

        System.out.println("\nTree Size: ");
        System.out.println(tree.size());

        System.out.println("\nDelete 50");
        tree.deleteRec(50);
        System.out.println("Inorder traversal of the modified tree");
        tree.inorderRec();

        System.out.println("\nTree Size: ");
        System.out.println(tree.size());

    }

}

