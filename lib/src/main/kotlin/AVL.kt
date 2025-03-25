package main.kotlin
import kotlin.math.*
import java.util.*
import java.io.*
class AVLNode<K: Comparable<K>, V>(key: K, value: V, parent: AVLNode<K, V>?):
    Node<K, V, AVLNode<K, V>>(key, value, parent = null){
        private var height: Int = 1
    val diff: Int
        get() = (leftChild?.height ?: 0) - (rightChild?.height ?: 0)
    fun updateHeight() {
        height = max(leftChild?.height ?: 0, rightChild?.height ?: 0) + 1
    }
}


class AVL<K: Comparable<K>, V>: TreeMap<K, V, AVLNode<K, V>>() {
    protected override var size: Long = 0
    protected override var root: AVLNode<K, V>? = null
    private fun updater(stack: Stack<AVLNode<K, V>>) {
        if (stack.empty()) {
            return
        }
        val node = stack.pop()
        node.updateHeight()
        when (node.diff) {
            0, 1, -1 -> {stack.clear(); return}
            2, -2 -> balance(node)
        }
        updater(stack)
    }
    private fun findMin(nod: AVLNode<K, V>): AVLNode<K, V> {
        if (nod.leftChild == null) {
            return nod
        }
        return(findMin(nod.leftChild!!))
    }
    private fun findMax(nod: AVLNode<K, V>): AVLNode<K, V> {
        if (nod.rightChild == null) {
            return nod
        }
        return (findMax(nod.rightChild!!))
    }
    override fun insert(key: K, value: V): Boolean {
        if (this.root == null) {
            this.root = AVLNode(key, value, null)
            size++
            return true
        }
        var currentNode = this.root
        val stack = Stack<AVLNode<K, V>>()
        stack.push(currentNode)
        while (currentNode != null) {
            if (currentNode.key == key) {
                return false
            }
            else if (currentNode.key < key ) {
                if (currentNode.rightChild == null) {
                    currentNode.rightChild = AVLNode(key, value, null)
                    stack.push(currentNode.rightChild)
                    updater(stack)
                    size++
                    return true
                }
                else {
                    stack.push(currentNode)
                    currentNode = currentNode.rightChild
                }
            }
            else if (currentNode.key > key) {
                if (currentNode.leftChild == null) {
                    currentNode.leftChild = AVLNode(key, value, null)
                    stack.push(currentNode.leftChild)
                    updater(stack)
                    size++
                    return true
                }
                else {
                    stack.push(currentNode)
                    currentNode = currentNode.leftChild
                }
            }

        }

        return false
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
    private fun delete(root: AVLNode<K, V>?, z: K, stack: Stack<AVLNode<K, V>>): AVLNode<K, V>? {
        if (root == null) return null
        if (z < root.key) {
            stack.push(root)
            root.leftChild = delete(root.leftChild, z, stack)
        }
        else if (z > root.key) {
            stack.push(root)
            root.rightChild = delete(root.rightChild, z, stack)
        }
        else if (root.leftChild != null && root.rightChild != null) {
            val rootMin = findMin(root.rightChild ?: return null)
            val newNode = AVLNode(key = rootMin.key, value = rootMin.value, parent = null)
            newNode.leftChild = root.leftChild
            newNode.rightChild = delete(root.rightChild, rootMin.key, stack)
            stack.push(newNode)
            return newNode
        }
        else {
            if (root.leftChild != null) {
                val rootLeft = root.leftChild!!
                val newNode = AVLNode(key = rootLeft.key, value = rootLeft.value, parent = null)
                newNode.leftChild = rootLeft.leftChild
                newNode.rightChild = rootLeft.rightChild
                stack.push(newNode)
                return newNode
            }
            else if (root.rightChild != null) {
                val rootRight = root.rightChild!!
                val newNode = AVLNode(key = rootRight.key, value = rootRight.value, parent = null)
                newNode.leftChild = rootRight.leftChild
                newNode.rightChild = rootRight.rightChild
                stack.push(newNode)
                return newNode
            }
            else {
                val newNode = null
                return newNode
            }
        }
        return root
    }
    override fun remove(key: K): Boolean {
        val stack = Stack<AVLNode<K, V>>()
        delete(this.root, key, stack)
        updater(stack)
        return true
    }
    private fun rotateLeft(a: AVLNode<K, V>) {
        val b = a.rightChild ?: return
        a.rightChild = b.leftChild

        b.leftChild = a
        a.updateHeight()
        b.updateHeight()
    }
    private fun rotateRight(a: AVLNode<K, V>) {
        val b = a.leftChild ?: return
        a.leftChild = b.rightChild
        b.rightChild = a
        a.updateHeight()
        b.updateHeight()
    }
    private fun bigRotateLeft(a: AVLNode<K, V>) {
        rotateRight(a.rightChild ?: return)
        rotateLeft(a)
    }
    private fun bigRotateRight(a: AVLNode<K, V>) {
        rotateLeft(a.leftChild ?: return)
        rotateRight(a)
    }
    private fun balance(a: AVLNode<K, V>) {
        val b = a.rightChild
        val br = a.leftChild
        if (a.diff == -2) {
            when (b?.diff) {
                -1, 0 -> rotateLeft(a)
                1 -> bigRotateRight(a)
            }
        }
        else if (a.diff == 2) {
            when (br?.diff) {
                1, 0 -> rotateRight(a)
                -1 -> bigRotateLeft(a)
            }
        }
    }
}