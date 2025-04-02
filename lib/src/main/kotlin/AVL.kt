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
    override protected var root: AVLNode<K, V>? = null
    override var size: Long = 0
    override var root: AVLNode<K, V>? = null
    // нахождение преемника вершины
    private fun findMin(nod: AVLNode<K, V>): AVLNode<K, V> {
        if (nod.leftChild == null) {
            return nod
        }
        return (findMin(nod.leftChild!!))
    }
    // поиск вершины по ключу
    private fun finder(key: K): AVLNode<K,V> {
        var currentNode = this.root
        while (currentNode != null) {
            if (currentNode.key == key) {
                return currentNode
            }
            else if (currentNode.key < key ) {
                currentNode = currentNode.rightChild
            }
            else {
                currentNode = currentNode.leftChild
            }
        }
        throw IllegalArgumentException("There is no such node.")
    }
    // вернуть корень
    internal fun getRoot() = this.root
    // сбалансировать все дерево
    private fun rebalanced(node: AVLNode<K, V>?) {
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
        else {
            var currentNode = this.root!!
            while (true) {
                require(currentNode.key != key) {
                    "It is not possible to insert a node " +
                            "as such a node already exists."
                }
                if (currentNode.key < key) {
                    if (currentNode.rightChild == null) {
                        val newNode = AVLNode(key, value, currentNode)
                        currentNode.rightChild = newNode
                        rebalanced(currentNode)
                        size++
                        return
                    } else currentNode = currentNode.rightChild!!
                } else {
                    if (currentNode.leftChild == null) {
                        val newNode = AVLNode(key, value, currentNode)
                        currentNode.leftChild = newNode
                        rebalanced(currentNode)
                        size++
                        return
                    } else currentNode = currentNode.leftChild!!
                }
            }
        }
    }
    override fun remove(key: K) {
        val node = finder(key)
        val parentNode = node.parent
        /*
        если удаляемый узел лист, то сначала проверка на то что
        есть родитель, дальше проверка на то, что удаляемый лист левый/правый ребенок
         */
        if (node.leftChild == null && node.rightChild == null) {
            if (node.parent != null) {
                if (parentNode!!.leftChild == node) {
                    parentNode.leftChild = null
                } else {
                    parentNode.rightChild = null
                }
                rebalanced(parentNode)
            }
            else {
                this.root = null
            }
        }
        // если удаляемый узел имеет лишь 1 ребенка
        else if (node.leftChild == null || node.rightChild == null) {
            if (node.leftChild == null) {
                if (parentNode != null) {
                    if (parentNode.leftChild == node) {
                        parentNode.leftChild = node.rightChild
                    }
                    else {
                        parentNode.rightChild = node.rightChild
                    }
                    node.rightChild!!.parent = parentNode
                    rebalanced(parentNode)
                }
                else {
                    node.rightChild!!.parent = null
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
                    node.leftChild!!.parent = parentNode
                    rebalanced(parentNode)
                }
                else {
                    node.leftChild!!.parent = null
                    this.root = node.leftChild
                }
            }
        }
        // удаляемый узел имеет 2 детей
        else {
            val successor = findMin(node.rightChild!!)
            node.key = successor.key
            node.value = successor.value
            val successorParent = successor.parent!!
            // преемник не является правым ребенком удаляемого узла
            if (successorParent.leftChild == successor) {
                successorParent.leftChild = successor.rightChild
                if (successor.rightChild != null) {
                    successor.rightChild!!.parent = successor.parent
                }
                rebalanced(successorParent)
            }
            // преемник является правым ребенком удаляемого узла
            else {
                successorParent.rightChild = successor.rightChild
                if (successor.rightChild != null) {
                    successor.rightChild!!.parent = successor.parent
                }
                rebalanced(successorParent)
            }
        }
        size--
    }
    private fun rotateLeft(node: AVLNode<K, V>): AVLNode<K, V> {
        val rightNode = node.rightChild
        val parentNode = node.parent
        node.rightChild = rightNode!!.leftChild
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
        val parentNode = node.parent
        node.leftChild = leftNode!!.rightChild
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
        rotateRight(rightNode!!)
        return rotateLeft(node)
    }
    private fun bigRotateRight(node: AVLNode<K, V>): AVLNode<K, V> {
        val leftNode = node.leftChild
        rotateLeft(leftNode!!)
        return rotateRight(node)
    }
    private fun balance(node: AVLNode<K, V>): AVLNode<K, V> {
        node.updateHeight()
        val diff = node.diff
        return when {
            diff < -1 -> {
                val rightNode = node.rightChild
                if (rightNode!!.diff == 1) bigRotateLeft(node)
                else rotateLeft(node)
            }
            diff > 1 -> {
                val leftNode = node.leftChild
                if (leftNode!!.diff == -1) bigRotateRight(node)
                else rotateRight(node)
            }
            else -> node
        }
    }
}