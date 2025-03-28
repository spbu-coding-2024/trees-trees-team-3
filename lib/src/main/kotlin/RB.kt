package main.kotlin

import kotlin.coroutines.Continuation

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

    override fun insert(key: K, value: V) {
        if (this.root == null) {
            this.root = RBNode(key, value, null, Color.BLACK)
            return
        }

        var newNodeParent = this.root
        while (newNodeParent != null) {
            if (newNodeParent.key == key) {
                // поменять/оставить прежнее значение/кинуть исключение?
            }
            else if (newNodeParent.key > key) {
                if (newNodeParent.leftChild == null) {
                    newNodeParent.leftChild = RBNode(key, value, newNodeParent, Color.RED)
                    break
                }
                newNodeParent = newNodeParent.leftChild
            }
            else if (newNodeParent.key < key) {
                if (newNodeParent.rightChild == null) {
                    newNodeParent.rightChild = RBNode(key, value, newNodeParent, Color.RED)
                    break
                }
                newNodeParent = newNodeParent.rightChild
            }
        }

        if (newNodeParent != null && newNodeParent.color == Color.RED) {
            var curNode = if (newNodeParent.leftChild != null) newNodeParent.leftChild else newNodeParent.rightChild
            var parent = newNodeParent
            var grandParent = newNodeParent.parent
            var uncle: RBNode<K, V>?
            if (parent == grandParent?.leftChild) {
                uncle = grandParent.rightChild
            }
            else {
                uncle = grandParent?.leftChild
            }

            // балансировка дерева после вставки
            TODO("посмотреть, во что уходит вершина текущая при балансировке - дед или отец, кто отец, кто дядя")
            while (parent?.color == Color.RED) {
                if (uncle != null && uncle.color == Color.RED) {
                    parent.color = Color.BLACK
                    uncle.color = Color.BLACK
                    grandParent?.color = Color.RED
                    curNode = grandParent
                    parent = curNode?.parent
                    grandParent = parent?.parent
                    if (parent == grandParent?.leftChild) {
                        uncle = grandParent?.rightChild
                    }
                    else {
                        uncle = grandParent?.leftChild
                    }
                }
                else {
                    // рассматриваем левого сына
                    if (curNode == parent.leftChild) {
                        // левый сын левого отца
                        if (parent == grandParent?.leftChild) {
                            grandParent.color = Color.RED
                            grandParent.leftChild = parent.rightChild

                            parent.color = Color.BLACK
                            parent.rightChild = grandParent
                            parent.parent = grandParent.parent

                            grandParent.parent = parent
                        }
                        // левый сын правого отца
                        else {
                            // поворот
                            curNode?.parent = grandParent
                            parent.parent = curNode
                            parent.leftChild = curNode?.rightChild
                            curNode?.rightChild = parent
                            // свапаем, получаем случай правый сын правого отца
                            var tmp = curNode
                            curNode = parent
                            parent = tmp

                            parent?.color = Color.BLACK
                            grandParent?.color = Color.RED

                            parent?.parent = grandParent?.parent
                            grandParent?.rightChild = parent?.leftChild
                            parent?.leftChild = grandParent

                            grandParent?.parent = parent
                        }
                    }
                    // рассматриваем правого сына
                    else {
                        // правый сын левого отца
                        if (parent == grandParent?.leftChild) {
                            parent.rightChild = curNode?.leftChild
                            parent.parent = curNode
                            curNode?.leftChild = parent
                            curNode?.parent = grandParent
                            // получаем случай с левым сыном левым отцом
                            var tmp = curNode
                            curNode = parent
                            parent = tmp

                            grandParent.color = Color.RED
                            grandParent.leftChild = parent?.rightChild

                            parent?.color = Color.BLACK
                            parent?.rightChild = grandParent
                            parent?.parent = grandParent.parent

                            grandParent.parent = parent
                        }
                        // правый сын правого отца
                        else {
                            parent.color = Color.BLACK
                            grandParent?.color = Color.RED

                            parent.parent = grandParent?.parent
                            grandParent?.rightChild = parent.leftChild
                            parent.leftChild = grandParent

                            grandParent?.parent = parent
                        }
                    }
                    curNode = parent
                    parent = curNode?.parent
                    grandParent = parent?.parent
                    if (parent == grandParent?.leftChild) {
                        uncle = grandParent?.rightChild
                    }
                    else {
                        uncle = grandParent?.leftChild
                    }
                    // отец дядя дед ???
                }
            }
            TODO("покрасить корень в черный")
        }
    }

    // поворот налево: n - новая "старшая вершина", отец n становится левым ребенком n
    private fun leftRotation(n: RBNode<K, V>?) {
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
    }

    // аналогичный поворот вправо
    private  fun rightRotation(n: RBNode<K, V>?){
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
    }

    override fun remove(key: K) {
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

        if (curNode == this.root) {
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
            TODO("на подумать")
            return
        }

        // удалили вершину справа
        if (side == Side.right) {
            TODO("проверить условия случаев - где-то вместо ссылки на черного ребенка фактически эквивалентный null")
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
}
