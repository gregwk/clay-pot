package util

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

class MapTree[A, B] extends KeyTree[A, B] with TreePrinter[A] {

    // value and parent can change. key cannot change and neither can list,
    // but the contents of the (mutable) list can change
    class Node(val key: A, var value: B, var parent: Option[Node], val children: ListBuffer[Node])
    val map: Map[A, Node] = Map.empty
    var root: Option[Node] = None

    /**
     * Adds a root node to an empty tree
     * @param key
     * @param value
     * @requires this.isEmpty
     */
    def addRoot(kv: (A, B)) {
        assert(isEmpty)
        val (key, value) = kv
        val node = new Node(key, value, None, ListBuffer.empty)
        root = Some(node)
        map += (key -> node)
    }

    /**
     * Adds a node with the key-value pair as the last child of the parent.
     * @param parent
     * @param key
     * @param value
     * @requires this.contains(parent)
     * @requires !this.contains(key)
     */
    def addChild(parent: A, kv: (A, B)) {
        assert(contains(parent), "No object corresponding to key: " + parent)
        assert(!contains(kv._1))
        val (key, value) = kv
        val node = new Node(key, value, Some(map(parent)), ListBuffer.empty)
        map(parent).children += node
        map += (key -> node)
    }

    /**
     * Adds a node with the key-value pair as the n-th child of the parent.
     * @param parent
     * @param n
     * @param key
     * @param value
     * @requires this.contains(parent)
     * @requires !this.contains(key)
     */
    def addChild(parent: A, n: Int, kv: (A, B)) {
        assert(contains(parent), "No object corresponding to key: " + parent)
        assert(!contains(kv._1))
        val (key, value) = kv
        val node = new Node(key, value, Some(map(parent)), ListBuffer.empty)
        map(parent).children.insert(n, node)
        map += (key -> node)
    }

    /**
     * Returns the value associated with the key.
     * @param key
     * @return
     * @requires this.contains(key)
     */
    def getValue(key: A): B = {
        assert(contains(key), "No object corresponding to key: " + key)
        map(key).value
    }

    /**
     * Replaces the original value of the node with the new value and returns the original.
     * @param key
     * @param value
     * @return
     * @requires this.contains(key)
     */
    def replace(key: A, value: B): B = {
        assert(contains(key), "No object corresponding to key: " + key)
        val node = map(key)
        val oldValue = node.value
        node.value = value
        oldValue
    }

    /**
     * Removes the leaf node and returns its value.
     * @param key
     * @return
     * @requires this.contains(key)
     * @requires this.hasLeaf(key)
     */
    def removeLeaf(key: A): B = {
        //assert(contains(key)) checked in hasLeaf
        assert(hasLeaf(key))
        map.remove(key) match {
            case None => throw new RuntimeException // should never happen
            case Some(node) =>
                node.parent match {
                    case None => root = None
                    case Some(parent) => parent.children -= node
                }
                node.value
        }
    }

    /**
     * Moves the subtree rooted at key as the last child of the parent.
     * @param key
     * @param parent
     * @requires this.contains(key)
     * @requires this.contains(parent)
     * @requires !this.hasDescendingPath(key, parent)
     */
    def moveSubtree(key: A, parent: A) {
        //assert(contains(key)) checked in hasDescendingPath
        //assert(contains(parent)) checked in hasDescendingPath
        assert(!hasDescendingPath(key, parent))
        val movingNode = map(key)
        val newParentNode = map(parent)
        movingNode.parent match {
            case None => throw new RuntimeException // should never happen
            case Some(p) => p.children -= movingNode
        }
        newParentNode.children += movingNode
        movingNode.parent = Some(newParentNode)
    }

    /**
     * Returns true if the first node is an ancestor of second or the nodes are equal
     * @param first
     * @param second
     * @return
     * @requires this.contains(first)
     * @requires this.contains(second)
     */
    def hasDescendingPath(first: A, second: A): Boolean = {
        assert(contains(first), "No object corresponding to key: " + first)
        assert(contains(second), "No object corresponding to key: " + second)
        if (first == second) {
            true
        } else {
            map(second).parent match {
                case None => false
                case Some(p) => hasDescendingPath(first, p.key)
            }
        }
    }

    /**
     * Returns the parent of the node.
     * @param key
     * @return
     * @requires this.contains(key)
     * @requires !this.hasRoot(key)
     */
    def parent(key: A): A = {
        //assert(contains(key)) checked in hasRoot
        assert(!hasRoot(key))
        map(key).parent match {
            case None => throw new RuntimeException // should never happen
            case Some(node) => node.key
        }
    }

    /**
     * Returns the first child of the node
     * @param key
     * @return
     * @requires this.contains(key)
     * @requires !this.hasLeaf(key)
     */
    def firstChild(key: A): A = {
        //assert(contains(key)) checked in hasLeaf
        assert(!hasLeaf(key))
        map(key).children.head.key
    }

    /**
     * Returns true if the node is the root
     * @param key
     * @return
     * @requires this.contains(key)
     */
    def hasRoot(key: A): Boolean = {
        assert(contains(key), "No object corresponding to key: " + key)
        map(key).parent match {
            case Some(_) => false
            case None => true
        }
    }

    /**
     * Returns true if the node is a leaf
     * @param node
     * @return
     * @requires this.contains(key)
     */
    def hasLeaf(key: A): Boolean = {
        assert(contains(key), "No object corresponding to key: " + key)
        map(key).children.isEmpty
    }

    /**
     * Returns true if the node is a last child
     * @param key
     * @return
     * @requires this.contains(key)
     */
    def hasNext(key: A): Boolean = {
        assert(contains(key), "No object corresponding to key: " + key)
        val node = map(key)
        node.parent match {
            case Some(p) => node != p.children.last
            case None => false
        }
    }

    /**
     * Returns true if the node is a first child
     * @param key
     * @return
     * @requires this.contains(key)
     */
    def hasPrev(key: A): Boolean = {
        assert(contains(key), "No object corresponding to key: " + key)
        val node = map(key)
        node.parent match {
            case Some(p) => node != p.children.head
            case None => false
        }
    }

    /**
     * Returns true if the tree contains the node.
     * @param node
     * @return
     */
    def contains(key: A): Boolean = {
        map.contains(key)
    }

    /**
     * Returns a list of child nodes of the parent
     * @param parent
     * @return
     * @requires this.contains(parent)
     */
    def childNodes(parent: A): List[A] = {
        assert(contains(parent), "No object corresponding to key: " + parent)
        val kids = map(parent).children
        kids.map(_.key).toList
    }

    /**
     * Returns the number of nodes in the tree.
     * @return
     */
    def size(): Integer = {
        map.size
    }

    /**
     * Returns true if the tree is empty.
     * @return
     */
    def isEmpty(): Boolean = {
        map.isEmpty
    }

    // def equals(that: Any): Boolean

    // def hashCode(): Integer

    override def toString(): String = {
        def printSubtree(n: Node): String = {
            val subtrees = n.children.map(printSubtree(_))
            "[" + n.key + "->" + n.value + "]" + subtrees.mkString("(", ",", ")")
        }
        root match {
            case None => "[]()"
            case Some(node) => printSubtree(node)
        }
    }

    def prettyPrint(): String = {
        def printSubtree(n: Node, indent: Int): String = {
            val subtrees = n.children.map(printSubtree(_, indent + 2))
            (" " * indent) + "[" + n.key + "->" + n.value + "]\n" + subtrees.mkString("")
        }
        root match {
            case None => "[]\n"
            case Some(node) => printSubtree(node, 0)
        }
    }

    def prettyPrintKeys(key: A, indent: Int): String = {
    	val node = map(key)
        val subtrees = node.children.map(n => prettyPrintKeys(n.key, indent + 2))
        (" " * indent) + "[" + node.key + "]\n" + subtrees.mkString("")
    }

    def prettyPrintKeys(): String = {
        root match {
            case None => "[]\n"
            case Some(node) => prettyPrintKeys(node.key, 0)
        }
    }
}