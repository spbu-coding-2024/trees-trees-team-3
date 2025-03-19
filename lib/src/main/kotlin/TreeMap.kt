package main.kotlin

abstract class Node<K: Comparable<K>, V, N>(key: K?, value: V?, parent: N?){
    internal val key: K?
    internal val value: V?
    internal var parent: N?

    internal var leftChild: N?

    internal var rightChild: N?

    init {
        this.key = key
        this.value = value
        this.parent = parent
        leftChild = null
        rightChild = null
    }
}


abstract class TreeMap<K: Comparable<K>, V, N: Node<K, V, N>>: Iterable<Pair<K, V>> {
    protected abstract var size: Long
    protected abstract var root: N?

    public fun find(key: K): V? {
        var currentNode = this.root
        while (currentNode != null) {
            if ((currentNode.key ?: return null) == key) {
                return currentNode.value
            }
            else if (currentNode.key < key ) {
                currentNode = currentNode.rightChild
            }
            else if (currentNode.key < key) {
                currentNode = currentNode.leftChild
            }
        }
        return null
    }

    public abstract fun insert(key: K, value: V)
    public abstract fun remove(key: K)
}
