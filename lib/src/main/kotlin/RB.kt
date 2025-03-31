package RBTree

import TreeMap.*

enum class Color {
    RED, BLACK
}


enum class Side {
    left, right
}


class RBNode<K: Comparable<K>, V>(key: K, value: V, parent: RBNode<K, V>?, color: Color?): Node<K, V, RBNode<K, V>>(key, value, parent){
    internal var color: Color? = color
}


class RBTree<K: Comparable<K>, V>: TreeMap<K, V, RBNode<K, V>>() {
    override protected var size: Long = 0
    override protected var root: RBNode<K, V>? = null

    // поворот налево: n - новая "старшая вершина", отец n становится левым ребенком n
    private fun leftRotation(n: RBNode<K, V>?) {
        var updateRoot = false
        if (n?.parent == this.root) {
            updateRoot  = true
        }

        if (n == null) {
            return
        }
        var parent = n.parent
        if (parent == null) {
            return
        }
        parent.rightChild = n.leftChild
        if (parent.rightChild != null) {
            parent.rightChild?.parent = parent
        }
        n.leftChild = parent
        n.parent = parent.parent
        if (parent.parent != null) {
            if (parent == parent.parent?.leftChild) {
                parent.parent?.leftChild = n
            }
            else {
                parent.parent?.rightChild = n
            }
        }
        parent.parent = n

        if (updateRoot) {
            this.root = n
        }
    }

    // аналогичный поворот вправо
    private fun rightRotation(n: RBNode<K, V>?){
        var updateRoot = false
        if (n?.parent == this.root) {
            updateRoot = true
        }

        if (n == null) {
            return
        }
        var parent = n.parent
        if (parent == null) {
            return
        }
        parent.leftChild = n.rightChild
        if (parent.leftChild != null) {
            parent.leftChild?.parent = parent
        }
        n.rightChild = parent
        n.parent = parent.parent
        if (parent.parent != null) {
            if (parent == parent.parent?.leftChild) {
                parent.parent?.leftChild = n
            }
            else {
                parent.parent?.rightChild = n
            }
        }
        parent.parent = n

        if (updateRoot) {
            this.root = n
        }
    }

    override fun insert(key: K, value: V) {
        // если такой ключ уже есть, то ?..
        this.size += 1
        if (this.root == null) {
            this.root = RBNode(key, value, null, Color.BLACK)
            return
        }
        var curNodeParent = this.root
        var curNode = if ((curNodeParent?.key ?: return) > key) curNodeParent.leftChild
                                    else curNodeParent.rightChild
        while (curNode != null) {
            curNodeParent = curNode
            curNode = if (curNodeParent.key > key) curNodeParent.leftChild
                                        else curNodeParent.rightChild
        }
        if (curNodeParent.key > key) {
            curNodeParent.leftChild = RBNode(key, value, curNodeParent, Color.RED)
        }
        else {
            curNodeParent.rightChild = RBNode(key, value, curNodeParent, Color.RED)
        }
        if (curNodeParent.color == Color.BLACK) {
            return
        }
        fixInsertion(curNodeParent.parent, if (curNodeParent.key > key) Side.left else Side.right)
    }

    // A - предок первой из двух подряд красных, side - с какой стороны от А
    private fun fixInsertion(A: RBNode<K, V>?, side: Side) {
        if (side == Side.left) {
            if (A?.rightChild?.color == Color.RED) {
                A?.color = Color.RED
                A?.leftChild?.color = Color.BLACK
                A?.rightChild?.color = Color.BLACK
                if (A == this.root) {
                    A?.color = Color.BLACK
                    return
                }
                if (A?.parent?.color == Color.RED) {
                    fixInsertion(A?.parent?.parent, if (A == A?.parent?.leftChild) Side.left else Side.right)
                }
            }
            else {
              A?.color = Color.RED
              A?.leftChild?.color = Color.BLACK
              rightRotation(A?.leftChild)
            }
        }
        else {
            if (A?.leftChild?.color == Color.RED) {
                A?.color = Color.RED
                A?.leftChild?.color = Color.BLACK
                A?.rightChild?.color = Color.BLACK
                if (A == this.root) {
                    A?.color = Color.BLACK
                    return
                }
                if (A?.parent?.color == Color.RED) {
                    fixInsertion(A?.parent?.parent, if (A == A?.parent?.leftChild) Side.left else Side.right)
                }
            }
            else {
                A?.color = Color.RED
                A?.rightChild?.color = Color.BLACK
                leftRotation(A?.rightChild)
            }
        }
    }

    override fun remove(key: K) {
        this.size -= 1
        var curNode = this.root
        while (curNode != null) {
            if (curNode.key == key) {
                break
            } else if (curNode.key < key) {
                curNode = curNode.rightChild
            } else {
                curNode = curNode.leftChild
            }
        }

        if (curNode == null) {
            throw NoSuchElementException("No such key in map")
        }

        if (curNode == this.root && this.root?.leftChild == null && this.root?.rightChild == null) {
            this.root = null
            return
        }
        deletion(curNode)
    }

    private fun deletion(curNode: RBNode<K, V>) {
        // удаляем вершину
        // красная без детей
        if (curNode.color == Color.RED && (curNode.leftChild == null && curNode.rightChild == null)) {
            if (curNode == curNode.parent?.leftChild) {
                curNode.parent?.leftChild = null
            } else {
                curNode.parent?.rightChild = null
            }
            return
        }
        // красная или черная вершина с двумя детьми
        else if (curNode.leftChild != null && curNode.rightChild != null) {
            var x = curNode.rightChild
            while (x?.leftChild != null) {
                x = x.leftChild
            }
            curNode.key = x?.key ?: return
            curNode.value = x.value
            deletion(x)
            return
        }
        // черная с одним потомком
        else if (curNode.color == Color.BLACK && (curNode.leftChild != null || curNode.rightChild != null)) {
            // ребенком может быть только красная вершина без потомков
            if (curNode.leftChild != null) {
                curNode.key = curNode.leftChild?.key ?: return
                curNode.value = curNode.leftChild?.value ?: return
                curNode.leftChild = null
                return
            } else {
                curNode.key = curNode.rightChild?.key ?: return
                curNode.value = curNode.rightChild?.value ?: return
                curNode.rightChild = null
                return
            }
        }
        // черная с null потомками
        else if (curNode.color == Color.BLACK && curNode.leftChild == null && curNode.rightChild == null) {
            var parent = curNode.parent
            if (curNode == parent?.leftChild) {
                parent.leftChild = null
                fixDeletion(parent, Side.left)
            }
            else {
                parent?.rightChild = null
                fixDeletion(parent, Side.right)
            }
            return
        }
    }

    // балансировка: A - вершина первая, у которой разбаланс поддеревьев, side - у какого поддерева меньше черная высота
    private fun fixDeletion(A: RBNode<K, V>?, side: Side) {
        if (A == null) {
            return
        }

        // удалили вершину справа
        if (side == Side.right) {
            // красный родитель, черный брат с хотя бы одним красным потомком (родство относительно удаленной вершины)
            if (A.color == Color.RED && A.leftChild?.color == Color.BLACK &&
                ((A.leftChild?.leftChild?.color ?: Color.BLACK) == Color.RED ||
                 (A.leftChild?.rightChild?.color ?: Color.BLACK) == Color.RED)) {
                // левый потомок брата красный
                if ((A.leftChild?.leftChild?.color ?: Color.BLACK) == Color.RED) {
                    rightRotation(A.leftChild)
                    A.color = Color.BLACK
                    A.leftChild?.color = Color.RED
                }
                else if ((A.leftChild?.rightChild?.color ?: Color.BLACK) == Color.RED) {
                    leftRotation(A.leftChild?.rightChild)
                    rightRotation(A.leftChild)
                    A.color = Color.BLACK
                }
                return
            }
            // красный родитель, черный брат с черными детьми
            else if (A.color == Color.RED && A.leftChild?.color == Color.BLACK &&
                    ((A.leftChild?.leftChild?.color ?: Color.BLACK) == Color.BLACK &&
                     (A.leftChild?.rightChild?.color ?: Color.BLACK) == Color.BLACK)) {
                A.leftChild?.color = Color.RED
                A.color = Color.BLACK
                return
            }
            // черный родитель, красный брат и у его правого потомка дети черные
            else if (A.color == Color.BLACK && A.leftChild?.color == Color.RED &&
                    ((A.leftChild?.rightChild?.leftChild?.color ?: Color.BLACK) == Color.BLACK) &&
                     (A.leftChild?.rightChild?.rightChild?.color ?: Color.BLACK) == Color.BLACK) {
                var brother = A.leftChild
                rightRotation(A.leftChild)
                if (brother != null) {
                    brother.color = Color.BLACK
                }
                A.leftChild?.color = Color.RED
                return
            }
            // черный родитель, красный брат и у его правого потомка есть красный ребенок
            else if (A.color == Color.BLACK && A.leftChild?.color == Color.RED &&
                    ((A.leftChild?.rightChild?.leftChild?.color ?: Color.BLACK) == Color.RED ||
                     (A.leftChild?.rightChild?.rightChild?.color ?: Color.BLACK) == Color.RED)) {
                if ((A.leftChild?.rightChild?.leftChild?.color ?: Color.BLACK) == Color.RED) {
                    A.leftChild?.rightChild?.leftChild?.color = Color.BLACK
                    leftRotation(A.leftChild?.rightChild)
                    rightRotation(A.rightChild)
                }
                else {
                    A.leftChild?.rightChild?.leftChild?.color = Color.BLACK
                    leftRotation(A.leftChild?.rightChild)
                    rightRotation(A.leftChild)
                }
                return
            }
            // черный родитель, черный брат и какой-то его потомок красный
            else if (A.color == Color.BLACK && A.leftChild?.color == Color.BLACK &&
                    ((A.leftChild?.leftChild?.color ?: Color.BLACK) == Color.RED ||
                     (A.leftChild?.rightChild?.color ?: Color.BLACK) == Color.BLACK)) {
                if (A.leftChild?.rightChild?.color == Color.RED) {
                    A.leftChild?.rightChild?.color = Color.BLACK
                    leftRotation(A.leftChild?.rightChild)
                    rightRotation(A.leftChild)
                }
                else {
                    A.leftChild?.leftChild?.color = Color.BLACK
                    rightRotation(A.leftChild)
                }
                return
            }
            // черный родитель, черный брат с черными потомками
            else if (A.color == Color.BLACK && A.leftChild?.color == Color.BLACK &&
                    ((A.leftChild?.leftChild?.color ?: Color.BLACK) == Color.BLACK &&
                     (A.leftChild?.rightChild?.color ?: Color.BLACK) == Color.BLACK)) {
                A.leftChild?.color = Color.RED
                if (A.parent == null) {
                    return
                }
                else {
                    fixDeletion(A.parent, (if (A == A.parent?.leftChild) Side.left else Side.right))
                }
            }
        }
        // удалили вершину слева, аналогичные случаи
        else {
            // красный родитель, черный брат с красным потомком
            if (A.color == Color.RED && A.rightChild?.color == Color.BLACK &&
               ((A.rightChild?.leftChild?.color ?: Color.BLACK) == Color.RED ||
                (A.rightChild?.rightChild?.color ?: Color.BLACK) == Color.RED)) {
                if (A.rightChild?.leftChild?.color == Color.RED) {
                    A.color = Color.RED
                    rightRotation(A.leftChild?.rightChild)
                    leftRotation(A.rightChild)
                }
                else {
                    A.color = Color.BLACK
                    A.rightChild?.color = Color.RED
                    A.rightChild?.rightChild?.color = Color.BLACK
                    leftRotation(A.rightChild)
                }
                return
            }
            // красный родитель, черный брат с черными потомками
            else if (A.color == Color.RED && A.rightChild?.color == Color.BLACK &&
                    ((A.rightChild?.leftChild?.color ?: Color.BLACK) == Color.BLACK) &&
                     (A.rightChild?.rightChild?.color ?: Color.BLACK) == Color.BLACK) {
                A.color = Color.BLACK
                A.rightChild?.color = Color.RED
                return
            }
            // черный родитель, красный брат, у его левого потомка черные потомки
            else if (A.color == Color.BLACK && A.rightChild?.color == Color.RED &&
                    ((A.rightChild?.leftChild?.leftChild?.color ?: Color.BLACK) == Color.BLACK &&
                     (A.rightChild?.leftChild?.rightChild?.color ?: Color.BLACK) == Color.BLACK)) {
                A.rightChild?.color = Color.BLACK
                A.rightChild?.leftChild?.color = Color.RED
                leftRotation(A.rightChild)
                return
            }
            // черный родитель, красный брат, у его левого потомка есть красный потомок
            else if (A.color == Color.BLACK && A.rightChild?.color == Color.RED &&
                    ((A.rightChild?.leftChild?.leftChild?.color ?: Color.BLACK) == Color.RED ||
                     (A.rightChild?.leftChild?.leftChild?.color ?: Color.BLACK) == Color.RED)) {
                if (A.rightChild?.leftChild?.leftChild?.color == Color.RED) {
                    A.rightChild?.rightChild?.color = Color.RED
                    A.rightChild?.color = Color.BLACK
                    rightRotation(A.rightChild?.leftChild)
                    leftRotation(A.rightChild)
                }
                else {
                    A.rightChild?.leftChild?.rightChild?.color = Color.BLACK
                    rightRotation(A.rightChild?.leftChild)
                    leftRotation(A.rightChild)
                }
                return
            }
            // черный родитель, черный брат с красным потомком
            else if (A.color == Color.BLACK && A.rightChild?.color == Color.BLACK &&
                    ((A.rightChild?.leftChild?.color ?: Color.BLACK) == Color.RED ||
                     (A.rightChild?.rightChild?.color ?: Color.BLACK) == Color.RED)) {
                if (A.rightChild?.rightChild?.color == Color.RED) {
                    A.rightChild?.rightChild?.color = Color.BLACK
                    leftRotation(A.rightChild)
                }
                else {
                    A.rightChild?.leftChild?.color = Color.BLACK
                    rightRotation(A.rightChild?.leftChild)
                    leftRotation(A.rightChild)
                }
                return
            }
            // черный родитель, черный брат с черными потомками
            else if (A.color == Color.BLACK && A.rightChild?.color == Color.BLACK &&
                    (A.rightChild?.leftChild?.color ?: Color.BLACK) == Color.BLACK &&
                    (A.rightChild?.rightChild?.color ?: Color.BLACK) == Color.BLACK) {
                A.rightChild?.color = Color.RED
                if (A != this.root) {
                    fixDeletion(A.parent, if (A == A.parent?.leftChild) Side.left else Side.right)
                }
            }
        }
    }

    public fun printTree(): ArrayList<Triple<K?, V?, Color?>?> {
        var tree = ArrayList<Triple<K?, V?, Color?>?>()

        fun bfs(curNode: RBNode<K, V>?, tree: ArrayList<Triple<K?, V?, Color?>?>) {
            var queue = ArrayDeque<RBNode<K, V>?>()
            if (this.root == null) {
                return
            }
            tree.addLast(Triple(this.root?.key, this.root?.value, this.root?.color))
            queue.addLast(this.root)
            while (queue.isNotEmpty()) {
                var curNode = queue.removeFirst()
                if (curNode?.leftChild == null) {
                    tree.addLast(null)
                }
                else {
                    tree.addLast(Triple(curNode.leftChild?.key, curNode.leftChild?.value, curNode.leftChild?.color))
                    queue.addLast(curNode.leftChild)
                }

                if (curNode?.rightChild == null) {
                    tree.addLast(null)
                }
                else {
                    tree.addLast(Triple(curNode.rightChild?.key, curNode.rightChild?.value, curNode.rightChild?.color))
                    queue.addLast(curNode.rightChild)
                }
            }
        }

        bfs(this.root, tree)
        return tree
    }

    public fun checkBalance(): Boolean {

        fun checkBlackHeight(node: RBNode<K, V>?): Int {
            if (node == null && this.root == null) {
                return 1
            }
            if (node == null) {
                return 0
            }
            var leftH = checkBlackHeight(node.leftChild)
            if (leftH == -1) {
                return -1
            }
            leftH += (if ((node.leftChild?.color ?: Color.BLACK) == Color.BLACK) 1
            else 0)
            var rightH = checkBlackHeight(node.rightChild)
            if (rightH == -1) {
                return -1
            }
            rightH += (if ((node.rightChild?.color ?: Color.BLACK) == Color.BLACK) 1
            else 0)
            if (leftH != rightH) {
                return -1
            } else {
                return leftH
            }
        }

        return if (checkBlackHeight(this.root) != -1) true else false
    }
}
