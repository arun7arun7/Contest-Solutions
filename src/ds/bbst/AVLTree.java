package ds.bbst;

/**
 * AVL Tree
 *
 * Duplicates Allowed
 * Working Insert, Delete, Select
 *
 * To be Tested
 * Rank, successors and predecessors
 */

public class AVLTree {

    class Node {
        int key, height, dupCnt, subTreeSize;
        Node left, right;

        Node(int d) {
            key = d;
            height = 1;
            dupCnt = 1;
            subTreeSize = 1;
        }
    }

    Node root;

    // A utility function to get height of the tree
    int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }

    int szOfSubtree(Node node) {
        if (node == null) {
            return 0;
        }
        return node.subTreeSize;
    }

    // A utility function to get maximum of two integers
    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // A utility function to right rotate subtree rooted with y
    // See the diagram given above.
    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        y.subTreeSize = szOfSubtree(y.left) + szOfSubtree(y.right) + y.dupCnt;
        x.subTreeSize = szOfSubtree(x.left) + szOfSubtree(x.right) + x.dupCnt;

        // Return new root
        return x;
    }

    // A utility function to left rotate subtree rooted with x
    // See the diagram given above.
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        x.subTreeSize = szOfSubtree(x.left) + szOfSubtree(x.right) + x.dupCnt;
        y.subTreeSize = szOfSubtree(y.left) + szOfSubtree(y.right) + y.dupCnt;


        // Return new root
        return y;
    }

    // Get Balance factor of node N
    int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    public int size() {
        return szOfSubtree(root);
    }

    /**
     *
     * @param key
     * @return rank of key stating from zero. Ex: rank of minimum element will be zero. Returns -1 if key is not found in the tree
     */
    public int rank(int key) {
        return rank(root, key);
    }

    private int rank(Node root, int key) {
        if (root == null) {
            return -1;
        }
        else if (root.key == (key)) {
            return szOfSubtree(root.left);
        }
        else if (root.key < (key)) {
            int rank = rank(root.right, key);
            if (rank != -1) {
                return szOfSubtree(root.left) + rank + root.dupCnt;
            }
            return rank;
        }
        else {
            return rank(root.left, key);
        }
    }

    /**
     *
     * @param i
     * @return key element whose rank(i) = key. Ex : for i == 0 , return minimum key. If i == tree.size(), return null.
     */
    public Integer select(int i) {
        Node node = root;
        while (node != null) {
            int rank = szOfSubtree(node.left);
            if (i >= rank && i <= (rank + node.dupCnt - 1)) {
                return node.key;
            }
            else if (i < rank) {
                node = node.left;
            }
            else {
                i -= (rank + node.dupCnt);
                node = node.right;
            }
        }
        return null;
    }

    void insert(int key) {
        root = insert(root, key);
    }

    Node insert(Node node, int key) {
        /* 1. Perform the normal BST rotation */
        if (node == null)
            return (new Node(key));

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else {
            node.dupCnt++;
        }

        /* 2. Update height of this ancestor node */
        node.height = 1 + max(height(node.left),
                height(node.right));

        node.subTreeSize = szOfSubtree(node.left) + szOfSubtree(node.right) + node.dupCnt;

		/* 3. Get the balance factor of this ancestor
		node to check whether this node became
		Wunbalanced */
        int balance = getBalance(node);

        // If this node becomes unbalanced, then
        // there are 4 cases Left Left Case
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        /* return the (unchanged) node pointer */
        return node;
    }

    /* Given a non-empty binary search tree, return the
    node with minimum key value found in that tree.
    Note that the entire tree does not need to be
    searched. */
    Node minValueNode(Node node) {
        Node current = node;

        /* loop down to find the leftmost leaf */
        while (current.left != null)
            current = current.left;

        return current;
    }

    void delete(int key) {
        root = deleteNode(root, key, false);
    }

    Node deleteNode(Node root, int key, boolean deleteCompletely) {
        // STEP 1: PERFORM STANDARD BST DELETE
        if (root == null)
            return root;

        // If the key to be deleted is smaller than
        // the root's key, then it lies in left subtree
        if (key < root.key)
            root.left = deleteNode(root.left, key, deleteCompletely);

            // If the key to be deleted is greater than the
            // root's key, then it lies in right subtree
        else if (key > root.key)
            root.right = deleteNode(root.right, key, deleteCompletely);

            // if key is same as root's key, then this is the node
            // to be deleted
        else {

            if (root.dupCnt > 1 && !deleteCompletely) {
                root.dupCnt--;
            }

            else {
                // node with only one child or no child
                if ((root.left == null) || (root.right == null)) {
                    Node temp = null;
                    if (temp == root.left)
                        temp = root.right;
                    else
                        temp = root.left;

                    // No child case
                    if (temp == null) {
                        temp = root;
                        root = null;
                    } else // One child case
                        root = temp; // Copy the contents of
                    // the non-empty child
                } else {

                    // node with two children: Get the inorder
                    // successor (smallest in the right subtree)
                    Node temp = minValueNode(root.right);

                    // Copy the inorder successor's data to this node
                    root.key = temp.key;
                    root.dupCnt = temp.dupCnt;

                    // Delete the inorder successor
                    root.right = deleteNode(root.right, temp.key, true);
                }
            }
        }

        // If the tree had only one node then return
        if (root == null)
            return root;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        root.height = max(height(root.left), height(root.right)) + 1;
        root.subTreeSize = szOfSubtree(root.left) + szOfSubtree(root.right) + root.dupCnt;

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        // this node became unbalanced)
        int balance = getBalance(root);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    // A utility function to print preorder traversal of
    // the tree. The function also prints height of every
    // node
    void preOrder(Node node) {
        if (node != null) {
            System.out.print(node.key + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    /**
     * @param root can't be null
     * @return
     */
    private Node findMinimum(Node root) {
        while (root.left != null) {
            root = root.left;
        }

        return root;
    }


    /**
     * @param root can't be null
     * @return
     */
    private Node findMaximum(Node root) {
        while (root.right != null) {
            root = root.right;
        }

        return root;
    }


    /**
     * returns least element greater than key if present otherwise returns null
     *
     * @param key
     * @return
     */
    public Integer findSuccessor(int key) {
        Node successor = findSuccessor(root, key);
        if (successor == null) {
            return null;
        }
        return successor.key;
    }

    // Iterative function to find inorder successor for given key in a BST
    private Node findSuccessor(Node root, int key) {
        Node succ = null;

        if (root == null) {
            return null;
        }

        while (true) {
            // if given key is less than the root node, visit left subtree
            if (key < (root.key)) // key < root.key
            {
                // update successor to current node before visiting
                // left subtree
                succ = root;
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key > (root.key)) // key > root.key
            {
                root = root.right;
            }

            // if node with key's value is found, the successor is minimum
            // value node in its right subtree (if any)
            else {
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
     * returns least element greater than or Equal to key if present otherwise returns null
     *
     * @param key
     * @return
     */
    public Integer findSuccessorWithEqualityConsideration(int key) {
        Node successorWithEqualityConsideration = findSuccessorWithEqualityConsideration(root, key);
        if (successorWithEqualityConsideration == null) {
            return null;
        }
        return successorWithEqualityConsideration.key;
    }

    private Node findSuccessorWithEqualityConsideration(Node root, int key) {
        Node succ = null;

        if (root == null) {
            return null;
        }

        while (true) {
            // if given key is less than the root node, visit left subtree
            if (key < (root.key)) // key < root.key
            {
                // update successor to current node before visiting
                // left subtree
                succ = root;
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key > (root.key)) // key > root.key
            {
                root = root.right;
            } else {
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
     * returns greatest element lesser than key if present otherwise returns null
     *
     * @param key
     * @return
     */
    public Integer findPredecessor(int key) {
        Node predecessor = findPredecessor(root, key);
        if (predecessor == null) {
            return null;
        }
        return predecessor.key;
    }

    private Node findPredecessor(Node root, int key) {
        Node predecessor = null;

        if (root == null) {
            return null;
        }

        while (true) {
            // if given key is less than the root node, visit left subtree
            if (key < (root.key)) // key < root.key
            {
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key > (root.key)) // key > root.key
            {
                // update predecessor to current node before visiting
                // right subtree
                predecessor = root;
                root = root.right;
            }

            // if node with key's value is found, the predecessor is maximum
            // value node in its left subtree (if any)
            else {
                if (root.left != null) {
                    predecessor = findMaximum(root.left);
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
     * returns greatest element lesser than or Equal to key if present otherwise returns null
     *
     * @param key
     * @return
     */
    public Integer findPredecessorWithEqualityConsideration(int key) {
        Node predecessorWithEqualityConsideration = findPredecessorWithEqualityConsideration(root, key);
        if (predecessorWithEqualityConsideration == null) {
            return null;
        }
        return predecessorWithEqualityConsideration.key;
    }

    private Node findPredecessorWithEqualityConsideration(Node root, int key) {
        Node predecessor = null;

        if (root == null) {
            return null;
        }

        while (true) {
            // if given key is less than the root node, visit left subtree
            if (key < (root.key)) // key < root.key
            {
                root = root.left;
            }

            // if given key is more than the root node, visit right subtree
            else if (key > (root.key)) // key > root.key
            {
                // update predecessor to current node before visiting
                // right subtree
                predecessor = root;
                root = root.right;
            }

            // if node with key's value is found, the predecessor is found
            else {
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

}