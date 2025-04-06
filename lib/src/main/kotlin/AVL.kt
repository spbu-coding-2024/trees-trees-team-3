import TreeMap.Node
import TreeMap.TreeMap
import kotlin.math.max

const val RIGHT_HEAVY = 2
const val LEFT_HEAVY = -2
const val LEFT_RIGHT_HEAVY = -1
const val RIGHT_LEFT_HEAVY = 1

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
    override var size: Int = 0
    override var root: AVLNode<K, V>? = null
    // нахождение преемника вершины
    private fun findMin(nod: AVLNode<K, V>): AVLNode<K, V> {
        if (nod.leftChild == null) {
            return nod
        }
        return (findMin(nod.leftChild!!))
    }
    // поиск вершины по ключу
    private fun finder(key: K): AVLNode<K, V> {
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
        val rootVal = this.root
        if (rootVal == null) {
            this.root = AVLNode(key = key, value = value, null)
            size++
            return
        }
        else {
            var currentNode = rootVal
            var parentNode : AVLNode<K, V>? = null
            while (true) {
                when {
                    currentNode == null -> {
                        currentNode = AVLNode(key, value, parentNode!!)
                        if (parentNode.key < key) {
                            parentNode.rightChild = currentNode
                        }
                        else parentNode.leftChild = currentNode
                        rebalanced(parentNode)
                        size++
                        return
                    }
                    currentNode.key < key -> {
                        parentNode = currentNode
                        currentNode = currentNode.rightChild
                    }
                    currentNode.key > key -> {
                        parentNode = currentNode
                        currentNode = currentNode.leftChild
                    }
                    else -> {
                        currentNode.value = value
                        return
                    }
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
            if (parentNode != null) {
                if (parentNode.leftChild == node) {
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
            val singleChild = node.leftChild ?: node.rightChild
            if (parentNode != null) {
                if (parentNode.leftChild == node) {
                    parentNode.leftChild = singleChild
                }
                else {
                    parentNode.rightChild = singleChild
                }
                singleChild!!.parent = parentNode
                rebalanced(parentNode)
            }
            else {
                singleChild!!.parent = null
                this.root = singleChild
            }

        }
        // удаляемый узел имеет 2 детей
        else {
            val successor = findMin(node.rightChild!!)
            val newKey = successor.key
            node.value = successor.value
            remove(newKey)
            node.key = newKey
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
        val diff : Int = node.diff
        return when (diff) {
            LEFT_HEAVY -> {
                val rightNode = node.rightChild
                if (rightNode!!.diff == RIGHT_LEFT_HEAVY) bigRotateLeft(node)
                else rotateLeft(node)
            }
            RIGHT_HEAVY -> {
                val leftNode = node.leftChild
                if (leftNode!!.diff == LEFT_RIGHT_HEAVY) bigRotateRight(node)
                else rotateRight(node)
            }
            else -> node
        }
    }
}