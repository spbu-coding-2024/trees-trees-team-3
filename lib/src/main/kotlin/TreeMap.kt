package main.kotlin

abstract class Node<K: Comparable<K>, V, N>(key: K, value: V, parent: N?){
    internal var key: K = key
    internal var value: V = value
    internal var parent: N? = parent

    internal var leftChild: N? = null

    internal var rightChild: N? = null
}


abstract class TreeMap<K: Comparable<K>, V, N: Node<K, V, N>>: Iterable<Pair<K, V>> {
    protected abstract var size: Long
    protected abstract var root: N?

    fun find(key: K): V? {
        var currentNode = this.root
        while (currentNode != null) {
            if (currentNode.key == key) {
                return currentNode.value
            }
            else if (currentNode.key < key ) {
                currentNode = currentNode.rightChild
            }
            else {
                currentNode = currentNode.leftChild
            }
        }
        return null
    }

    abstract fun insert(key: K, value: V)
    abstract fun remove(key: K)

    override fun iterator(): Iterator<Pair<K, V>> {
        val allNodes = ArrayList<Pair<K, V>>()
        fun dfs(curNode: N?) {
            if (curNode != null) {
                if (curNode.leftChild != null) {
                    dfs(curNode.leftChild)
                }
                allNodes.add(Pair(curNode.key, curNode.value))
                if (curNode.rightChild != null) {
                    dfs(curNode.rightChild)
                }
            }
        }

        dfs(this.root)
        return allNodes.iterator()
    }
}
