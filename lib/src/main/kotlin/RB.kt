package main.kotlin

class RBNode<K: Comparable<K>, V>(key: K, value: V, parent: RBNode<K, V>?): Node<K, V, RBNode<K, V>>(key, value, parent){

}


class RBTree<K: Comparable<K>, V>: TreeMap<K, V, RBNode<K, V>>() {
    override protected var size: Int = 0
    override internal var root: RBNode<K, V>? = null

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
