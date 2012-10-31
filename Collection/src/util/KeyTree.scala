package util

/**
 * An Arbor is a tree of key-value pairs. All nodes of the tree have a unique key associated with them.
 */
trait KeyTree[A, B] {

    /**
     * Adds a root node to an empty tree
     * @param key
     * @param value
     * @requires this.isEmpty
     */
    def addRoot(kv: (A, B))

    /**
     * Adds a node with the key-value pair as the last child of the parent.
     * @param parent
     * @param key
     * @param value
     * @requires this.contains(parent)
     * @requires !this.contains(key)
     */
    def addChild(parent: A, kv: (A, B))

    /**
     * Adds a node with the key-value pair as the n-th child of the parent.
     * @param parent
     * @param n
     * @param key
     * @param value
     * @requires this.contains(parent)
     * @requires !this.contains(key)
     */
    def addChild(parent: A, n: Int, kv: (A, B))

    /**
     * Returns the value associated with the key.
     * @param key
     * @return
     * @requires this.contains(key)
     */
    def getValue(key: A): B

    /**
     * Replaces the original value of the node with the new value and returns the original.
     * @param key
     * @param value
     * @return
     * @requires this.contains(key)
     */
    def replace(key: A, value: B): B

    /**
     * Removes the leaf node and returns its value.
     * @param key
     * @return
     * @requires this.contains(key)
     * @requires this.hasLeaf(key)
     */
    def removeLeaf(key: A): B

    /**
     * Moves the subtree rooted at key as the last child of the parent.
     * @param key
     * @param parent
     * @requires this.contains(key)
     * @requires this.contains(parent)
     * @requires !this.hasDescendingPath(key, parent)
     */
    def moveSubtree(key: A, parent: A)

    /**
     * Returns true if the first node is an ancestor of second or the nodes are equal
     * @param first
     * @param second
     * @return
     * @requires this.contains(first)
     * @requires this.contains(second)
     */
    def hasDescendingPath(first: A, second: A): Boolean

    /**
     * Returns the parent of the node.
     * @param key
     * @return
     * @requires this.contains(key)
     * @requires !this.hasRoot(key)
     */
    def parent(key: A): A

    /**
     * Returns the first child of the node
     * @param key
     * @return
     * @requires this.contains(key)
     * @requires !this.hasLeaf(key)
     */
    def firstChild(key: A): A

    /**
     * Returns true if the node is the root
     * @param key
     * @return
     * @requires this.contains(key)
     */
    def hasRoot(key: A): Boolean

    /**
     * Returns true if the node is a leaf
     * @param node
     * @return
     * @requires this.contains(key)
     */
    def hasLeaf(key: A): Boolean

    /**
     * Returns true if the node is a last child
     * @param key
     * @return
     * @requires this.contains(key)
     */
    def hasNext(key: A): Boolean

    /**
     * Returns true if the node is a first child
     * @param key
     * @return
     * @requires this.contains(key)
     */
    def hasPrev(key: A): Boolean

    /**
     * Returns true if the tree contains the node.
     * @param node
     * @return
     */
    def contains(key: A): Boolean

    /**
     * Returns a list of child nodes of the parent
     * @param parent
     * @return
     * @requires this.contains(parent)
     */
    def childNodes(parent: A): List[A]

    /**
     * Returns the number of nodes in the tree.
     * @return
     */
    def size(): Integer

    /**
     * Returns true if the tree is empty.
     * @return
     */
    def isEmpty(): Boolean
}