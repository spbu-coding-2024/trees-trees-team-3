package main.kotlin

enum class Color {
    RED, BLACK
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
        }
    }

    override fun remove(key: K) {
        TODO("Not yet implemented")
    }
}
