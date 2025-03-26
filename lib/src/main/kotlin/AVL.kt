package main.kotlin
import kotlin.math.*
class AVLNode<K: Comparable<K>, V>(key: K, value: V, parent: AVLNode<K, V>?):
    Node<K, V, AVLNode<K, V>>(key, value, parent){
        private var height: Int = 1
    val diff: Int
        get() = (leftChild?.height ?: 0) - (rightChild?.height ?: 0)
    internal fun updateHeight() {
        height = max(leftChild?.height ?: 0, rightChild?.height ?: 0) + 1
    }
}


class AVL<K: Comparable<K>, V>: TreeMap<K, V, AVLNode<K, V>>() {
    override var size: Long = 0
    override var root: AVLNode<K, V>? = null
    private fun findMin(nod: AVLNode<K, V>): AVLNode<K, V> {
        if (nod.leftChild == null) {
            return nod
        }
        return(findMin(nod.leftChild!!))
    }
    private fun finder(key: K): AVLNode<K,V>? {
        var currentNode = this.root
        while (currentNode != null) {
            if (currentNode.key == key) {
                return currentNode
            }
            else if (currentNode.key < key ) {
                currentNode = currentNode.rightChild
            }
            else if (currentNode.key > key) {
                currentNode = currentNode.leftChild
            }
        }
        return null
    }
    private fun rebalance(node: AVLNode<K, V>?) {
        var currentNode = node
        while (currentNode != null) {
            val parentNode = currentNode.parent
            currentNode.updateHeight()
            balance(currentNode)
            currentNode = parentNode
        }
    }
    override fun insert(key: K, value: V) {
        if (this.root == null) {
            this.root = AVLNode(key = key, value = value, null)
            size++
            return
        }
        var currentNode = this.root
        while (currentNode != null) {
            require(currentNode.key != key) {"It is not possible to insert a node " +
                    "as such a node already exists."}
            if (currentNode.key < key) {
                if (currentNode.rightChild == null) {
                    val newNode = AVLNode(key, value, currentNode)
                    currentNode.rightChild = newNode
                    rebalance(currentNode)
                    size++
                    return
                }
                else currentNode = currentNode.rightChild
            }
            else {
                if (currentNode.leftChild == null) {
                    val newNode = AVLNode(key, value, currentNode)
                    currentNode.leftChild = newNode
                    rebalance(currentNode)
                    size++
                    return
                }
                else currentNode = currentNode.leftChild
            }
        }
    }
    override fun remove(key: K) {
        val node = finder(key)
        val parentNode = node?.parent
        require(node != null) {"Deletion is not possible: there is no such node."}
        if (node.leftChild == null && node.rightChild == null) {
            if (node.parent != null) {
                if (parentNode?.leftChild == node) {
                    parentNode.leftChild = null
                } else {
                    parentNode?.rightChild = null
                }
                rebalance(parentNode!!)
            }
            else {
                this.root = null
            }
        }
        else if (node.leftChild == null || node.rightChild == null) {
            if (node.leftChild == null) {
                if (parentNode != null) {
                    if (parentNode.leftChild == node) {
                        parentNode.leftChild = node.rightChild
                    }
                    else {
                        parentNode.rightChild = node.rightChild
                    }
                    node.rightChild?.parent = parentNode
                    rebalance(parentNode)
                }
                else {
                    node.rightChild?.parent = null
                    this.root = node.rightChild
                }
            }
            else {
                if (parentNode != null) {
                    if (parentNode.leftChild == node) {
                        parentNode.leftChild = node.leftChild
                    }
                    else {
                        parentNode.rightChild = node.leftChild
                    }
                    node.leftChild?.parent = parentNode
                    rebalance(parentNode)
                }
                else {
                    node.leftChild?.parent = null
                    this.root = node.leftChild
                }
            }
        }
        else {
            val successor = findMin(node.rightChild!!)
            val successorKey = successor.key
            val successorParent = successor.parent
            val leftNode = node.leftChild
            val successorValue = successor.value
            if (successor.parent?.leftChild == successor) {
                successor.parent?.leftChild = successor.rightChild
                successor.rightChild?.parent = successor.parent
                val newNode = AVLNode(key = successorKey, value = successorValue, 
                    node.parent)
                newNode.leftChild = leftNode
                leftNode?.parent = newNode
                newNode.rightChild = node.rightChild
                node.rightChild?.parent = newNode
                if (parentNode != null) {
                    if (parentNode.leftChild == node)
                        parentNode.leftChild = newNode
                    else parentNode.rightChild = newNode
                }
                else this.root = newNode
                rebalance(successorParent!!)
            }
            else {
                val newNode = AVLNode(key = successorKey, value = successorValue,
                    node.parent)
                newNode.leftChild = leftNode
                leftNode?.parent = newNode
                newNode.rightChild = successor.rightChild
                newNode.rightChild?.parent = newNode
                if (parentNode != null) {
                    if (parentNode.leftChild == node)
                        parentNode.leftChild = newNode
                    else parentNode.rightChild = newNode
                }
                else this.root = newNode
                rebalance(newNode)
            }
        }
        size--
    }
    private fun rotateLeft(node: AVLNode<K, V>): AVLNode<K, V> {
        val rightNode = node.rightChild
        require(rightNode != null) {"Left turn is not possible: " +
                "the right child is null"}
        val parentNode = node.parent
        node.rightChild = rightNode.leftChild
        node.rightChild?.parent = node
        rightNode.leftChild = node
        node.parent = rightNode
        if (parentNode == null) {
            this.root = rightNode
            rightNode.parent = null
        }
        else if (parentNode.leftChild == node) {
            parentNode.leftChild = rightNode
            rightNode.parent = parentNode
        }
        else {
            parentNode.rightChild = rightNode
            rightNode.parent = parentNode
        }
        node.updateHeight()
        rightNode.updateHeight()
        return rightNode
    }
    private fun rotateRight(node: AVLNode<K, V>): AVLNode<K, V> {
        val leftNode = node.leftChild
        require(leftNode != null) {"Right turn is not possible: " +
                "the left child is null"}
        val parentNode = node.parent
        node.leftChild = leftNode.rightChild
        node.leftChild?.parent = node
        leftNode.rightChild = node
        node.parent = leftNode
        if (parentNode == null) {
            this.root = leftNode
            leftNode.parent = null
        }
        else if (parentNode.leftChild == node) {
            parentNode.leftChild = leftNode
            leftNode.parent = parentNode
        }
        else {
            parentNode.rightChild = leftNode
            leftNode.parent = parentNode
        }
        node.updateHeight()
        leftNode.updateHeight()
        return leftNode
    }
    private fun bigRotateLeft(node: AVLNode<K, V>): AVLNode<K, V> {
        val rightNode = node.rightChild
        require(rightNode != null) {"Big Right turn is not possible: " +
                "the Right child is null"}
        rotateRight(rightNode)
        return rotateLeft(node)
    }
    private fun bigRotateRight(node: AVLNode<K, V>): AVLNode<K, V> {
        val leftNode = node.leftChild
        require(leftNode != null) {"Big Left turn is not possible: " +
                "the left child is null"}
        rotateLeft(leftNode)
        return rotateRight(node)
    }
    private fun balance(node: AVLNode<K, V>): AVLNode<K, V> {
        node.updateHeight()
        val diff = node.diff
        return when {
            diff < -1 -> {
                val rightNode = node.rightChild
                if ((rightNode?.diff ?: 0) > 0) bigRotateLeft(node)
                else rotateLeft(node)
            }
            diff > 1 -> {
                val leftNode = node.leftChild
                if ((leftNode?.diff ?: 0) < 0) bigRotateRight(node)
                else rotateRight(node)
            }
            else -> node
        }
    }
}