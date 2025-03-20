package main.kotlin

class AVLNode<K: Comparable<K>, V>(key: K, value: V, parent: AVLNode<K, V>?): Node<K, V, AVLNode<K, V>>(key, value, parent){

}


class AVL<K: Comparable<K>, V>: TreeMap<K, V, AVLNode<K, V>>() {
    override protected var size: Long = 0
    override protected var root: AVLNode<K, V>? = null

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