package bsTree

import treeMap.*

/**
 * Node from a binary search tree
 */
class BSTNode<K : Comparable<K>, V>(key: K, value: V, parent: BSTNode<K, V>?) : Node<K, V, BSTNode<K, V>>(key, value, parent)

/**
 * Binary search tree
 */
class BST<K : Comparable<K>, V> : TreeMap<K, V, BSTNode<K, V>>() {
	/**
	 * Contains root node
	 */
	override var root: BSTNode<K, V>? = null

	/**
	 * Finds minimum element in subtree beginning from "node"
	 */
	private fun findMinimum(node: BSTNode<K, V>): BSTNode<K, V> {
		val nextNode = node.leftChild
		return if (nextNode == null) node else findMinimum(nextNode)
	}

	/**
	 * Inserts element in BST using key
	 */
	override fun insert(key: K, value: V) {
		if (this.root == null) {
			this.root = BSTNode(key = key, value = value, parent = null)
			size++
			return
		}

		var current = this.root
		while (current != null) {
			require(key != current.key) { "Node with this key already exists" }
			if (key < current.key) {
				if (current.leftChild != null) {
					current = current.leftChild
				}
				else {
					current.leftChild = BSTNode(key = key, value = value, parent = current)
					size++
					return
				}
			}
			else {
				if (current.rightChild != null) {
					current = current.rightChild
				}
				else {
					current.rightChild = BSTNode(key = key, value = value, parent = current)
					size++
					return
				}
			}
		}
	}

	/**
	 * Finds node using a key
	 */
	private fun findNode(key: K): BSTNode<K, V>? {
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
		return null
	}

	/**
	 * Returns the next element by key
	 */
	private fun next(node: BSTNode<K, V>): BSTNode<K, V> {
		val right = node.rightChild
		if (right != null) return findMinimum(right)

		var current = node
		var par = node.parent
		while (par != null && current == par.rightChild) {
			current = par
			par = par.parent
		}
		requireNotNull(par) { "Next element wasn't found" }
		return par
	}

	/**
	 * Removes element using a key
	 */
	override fun remove(key: K) {
		val node = findNode(key)
		require(node != null) { "Node with this key doesn't exist" }
		val par = node.parent

		//Removing node has no descendants
		if (node.leftChild == null && node.rightChild == null) {
			if (par != null) {
				if (par.leftChild == node) par.leftChild = null
				else par.rightChild = null
			}
			else this.root = null
		}

		//Removing node has one descendant
		else if (node.leftChild == null || node.rightChild == null) {
			val child = node.leftChild ?: node.rightChild
			if (par != null) {
				if (par.leftChild == node) par.leftChild = child
				else par.rightChild = child
				child?.parent = par
			}
			else {
				child?.parent = null
				this.root = child
			}
		}

		//Removing node has two descendants
		else {
			val successor = next(node)
			node.key = successor.key
			node.value = successor.value

			if (successor.parent?.leftChild == successor) {
				successor.parent?.leftChild = successor.rightChild
				if (successor.rightChild != null) successor.rightChild?.parent = successor.parent
			}
			else {
				successor.parent?.rightChild = successor.rightChild
				if (successor.rightChild != null) successor.rightChild?.parent = successor.parent
			}
		}
		size--
	}
}
