package treeMapTest
import avlTree.AVL
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

internal class TreeMapTest {
 private var tree = AVL<Int, Int>()
 private fun sorted(iterator: Iterator<Pair<Int, Int>>): Boolean {
  if (!iterator.hasNext()) {
   return true
  }
  var previousKey = iterator.next().first
  while (iterator.hasNext()) {
   val currentKey = iterator.next().first
   if (previousKey >= currentKey) return false
   previousKey = currentKey
  }
  return true
 }
@BeforeEach
 fun setUp() {
 tree = AVL()
 }
@Test
fun `node is not exist`(){
 val res = tree.find(5)
 assertThat(res).isNull()
}
@Test
fun `find right node`(){
 tree.insert(5, 1005)
 tree.insert(15, 1015)
 val res = tree.find(15)
 assertThat(res).isEqualTo(1015)
}
@Test
fun `find left node`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 val res = tree.find(3)
 assertThat(res).isEqualTo(1003)
}
@Test
fun `iteration null-tree`(){
 val pairsFromIterator = tree.iterator()
 assertThat(sorted(pairsFromIterator)).isTrue()
}
@Test
fun `iteration tree with one node`() {
 tree.insert(5, 1005)
 val pairsFromIterator = tree.iterator()
 assertThat(sorted(pairsFromIterator)).isTrue()
}
@Test
fun `iteration tree with 3 nodes`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 tree.insert(7, 1007)
 val pairsFromIterator = tree.iterator()
 assertThat(sorted(pairsFromIterator)).isTrue()
}
@Test
fun `iteration tree with many nodes`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 tree.insert(7, 1007)
 tree.insert(1, 1001)
 tree.insert(20, 1020)
 tree.insert(2, 1002)
 tree.insert(15, 1015)
 tree.insert(60, 1060)
 tree.insert(4, 1004)
 tree.insert(6, 1006)
 val pairsFromIterator = tree.iterator()
 assertThat(sorted(pairsFromIterator)).isTrue()
}
}