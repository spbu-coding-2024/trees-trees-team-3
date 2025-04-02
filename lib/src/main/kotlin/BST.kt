package BSTree

import TreeMap.*


class BSTNode<K: Comparable<K>, V>(key: K, value: V, parent: BSTNode<K, V>?): Node<K, V, BSTNode<K, V>>(key, value, parent) {
}


class BST<K: Comparable<K>, V>: TreeMap<K, V, BSTNode<K, V>>(){
    override protected var root: BSTNode<K, V>? = null

    override fun insert(key: K, value: V) {
        TODO("Not yet implemented")
    }

    override fun remove(key: K) {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<Pair<K, V>> {
        TODO("Not yet implemented")
    }
}