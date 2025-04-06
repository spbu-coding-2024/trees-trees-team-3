import main.kotlin.AVL
import main.kotlin.AVLNode
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit
import kotlin.random.Random
internal class AVLTest {
 private var tree = AVL<Int, Int>()
 private fun isBalanced(tree: AVL<Int, Int>): Boolean {
  if (tree.root == null) return true
  return balanceCheck(tree.root)
 }
 private fun balanceCheck(node: AVLNode<Int, Int>?): Boolean {
  if (node == null) return true
  return if (node.diff !in -1..1) {
   false
  } else balanceCheck(node.leftChild) && balanceCheck(node.rightChild)
 }
 private fun iter(tree: AVL<Int, Int>): MutableList<Int> {
  val pairsFromIterator = tree.iterator()
  val keysFromIterator = mutableListOf<Int>()
  for (pair in pairsFromIterator) {
   keysFromIterator.add(pair.first)
  }
  return keysFromIterator
 }
@BeforeEach
fun setUp(){
 tree = AVL()
}
@Test
fun `insert root`() {
 tree.insert(5, 1005)
 assertThat(tree.root?.key).isEqualTo(5)
 assertThat(isBalanced(tree)).isTrue()
}
 @Test
 fun `insert a node that already exist`() {
  tree.insert(5, 1005)
  tree.insert(5, 1010)
  assertThat(tree.root?.key).isEqualTo(5)
  assertThat(tree.root?.value).isEqualTo(1010)
  assertThat(isBalanced(tree)).isTrue()
 }
@Test
fun `iterator of null`(){
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly()
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `insert left and right node`() {
 tree.insert(5, 1005)
 tree.insert(4, 1004)
 tree.insert(40, 1040)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(4, 5, 40)
 assertThat(tree.root?.key).isEqualTo(5)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `small left rotate`() {
 tree.insert(5, 1005)
 tree.insert(4, 1004)
 tree.insert(40, 1040)
 tree.insert(70, 1070)
 tree.insert(80, 1080)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(4, 5, 40, 70, 80)
 assertThat(isBalanced(tree)).isTrue()
 }
@Test
fun `small right rotate`() {
 tree.insert(10, 1010)
 tree.insert(15, 1015)
 tree.insert(5, 1005)
 tree.insert(7, 1007)
 tree.insert(3, 1003)
 tree.insert(2, 1002)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(2, 3, 5, 7, 10, 15)
 assertThat(tree.root?.key).isEqualTo(5)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `big left rotate`() {
 tree.insert(5, 1005)
 tree.insert(4, 1004)
 tree.insert(40, 1040)
 tree.insert(70, 1070)
 tree.insert(55, 1055)
 tree.insert(45, 1045)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(4, 5, 40, 45, 55, 70)
 assertThat(tree.root?.key).isEqualTo(40)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `big right rotate`() {
 tree.insert(10, 1010)
 tree.insert(15, 1015)
 tree.insert(5, 1005)
 tree.insert(7, 1007)
 tree.insert(3, 1003)
 tree.insert(6, 1006)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(3, 5, 6, 7, 10, 15)
 assertThat(tree.root?.key).isEqualTo(7)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `root delete`(){
 tree.insert(1, 1001)
 tree.remove(1)
 assertThat(tree.root).isNull()
}
@Test
fun `left node remove`(){
 tree.insert(5, 1005)
 tree.insert(4, 1004)
 tree.remove(4)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(5)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
 fun `right node remove`(){
  tree.insert(5, 1005)
  tree.insert(6, 1006)
  tree.remove(6)
  val pairsFromIterator = tree.iterator()
  val keysFromIterator = mutableListOf<Int>()
  for (pair in pairsFromIterator) {
   keysFromIterator.add(pair.first)
  }
  assertThat(keysFromIterator).containsExactly(5)
  assertThat(isBalanced(tree)).isTrue()
 }
@Test
fun `removing a node that has a right child and is the left child`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 tree.insert(6, 1006)
 tree.insert(4, 1004)
 tree.remove(3)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(4, 5, 6)
 assertThat(tree.root?.key).isEqualTo(5)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `removing a node that has a left child and is the left child`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 tree.insert(6, 1006)
 tree.insert(2, 1002)
 tree.remove(3)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(2, 5, 6)
 assertThat(tree.root?.key).isEqualTo(5)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `removing a node that minimum is right and have right`(){
 tree.insert(10, 1010)
 tree.insert(5, 1005)
 tree.insert(15, 1015)
 tree.insert(20, 1020)
 tree.remove(10)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(5, 15, 20)
 assertThat(tree.root?.key).isEqualTo(15)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `removing a node that has two children and minimum have right`(){
 tree.insert(10, 1010)
 tree.insert(5, 1005)
 tree.insert(15, 1015)
 tree.insert(20, 1020)
 tree.insert(3, 1003)
 tree.insert(12, 1012)
 tree.insert(17, 1017)
 tree.insert(7, 1007)
 tree.insert(14, 1014)
 tree.insert(8, 1008)
 tree.insert(22, 1022)
 tree.insert(18, 1018)
 tree.remove(15)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(3, 5, 7, 8, 10, 12, 14, 17, 18, 20, 22)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
 fun `root node remove for left`(){
  tree.insert(5, 1005)
  tree.insert(4, 1004)
  tree.remove(5)
  val pairsFromIterator = tree.iterator()
  val keysFromIterator = mutableListOf<Int>()
  for (pair in pairsFromIterator) {
   keysFromIterator.add(pair.first)
  }
  assertThat(keysFromIterator).containsExactly(4)
  assertThat(tree.root?.key).isEqualTo(4)
  assertThat(isBalanced(tree)).isTrue()
 }
 @Test
 fun `root node remove for right`(){
  tree.insert(5, 1005)
  tree.insert(6, 1006)
  tree.remove(5)
  val pairsFromIterator = tree.iterator()
  val keysFromIterator = mutableListOf<Int>()
  for (pair in pairsFromIterator) {
   keysFromIterator.add(pair.first)
  }
  assertThat(keysFromIterator).containsExactly(6)
  assertThat(tree.root?.key).isEqualTo(6)
  assertThat(isBalanced(tree)).isTrue()
 }
 @Test
 fun `root node remove for right and left`(){
  tree.insert(5, 1005)
  tree.insert(6, 1006)
  tree.insert(4, 1004)
  tree.remove(5)
  val pairsFromIterator = tree.iterator()
  val keysFromIterator = mutableListOf<Int>()
  for (pair in pairsFromIterator) {
   keysFromIterator.add(pair.first)
  }
  assertThat(keysFromIterator).containsExactly(4, 6)
  assertThat(tree.root?.key).isEqualTo(6)
  assertThat(isBalanced(tree)).isTrue()
 }
@Test
 fun `root node remove right with child`(){
  tree.insert(5, 1005)
  tree.insert(6, 1006)
  tree.insert(4, 1004)
  tree.insert(7, 1007)
  tree.remove(6)
  val pairsFromIterator = tree.iterator()
  val keysFromIterator = mutableListOf<Int>()
  for (pair in pairsFromIterator) {
   keysFromIterator.add(pair.first)
  }
  assertThat(keysFromIterator).containsExactly(4, 5, 7)
  assertThat(tree.root?.rightChild?.key).isEqualTo(7)
  assertThat(isBalanced(tree)).isTrue()
 }
 @Test
 fun `node remove right with left child`(){
  tree.insert(5, 1005)
  tree.insert(7, 1007)
  tree.insert(4, 1004)
  tree.insert(6, 1006)
  tree.remove(7)
  val pairsFromIterator = tree.iterator()
  val keysFromIterator = mutableListOf<Int>()
  for (pair in pairsFromIterator) {
   keysFromIterator.add(pair.first)
  }
  assertThat(keysFromIterator).containsExactly(4, 5, 6)
  assertThat(tree.root?.rightChild?.key).isEqualTo(6)
  assertThat(isBalanced(tree)).isTrue()
 }
 @Test
 fun `node remove right with unTree`(){
  tree.insert(5, 1005)
  tree.insert(7, 1007)
  tree.insert(6, 1006)
  tree.insert(4, 1004)
  tree.insert(9, 1009)
  tree.insert(3, 1003)
  tree.insert(8, 1008)
  tree.remove(7)
  val pairsFromIterator = tree.iterator()
  val keysFromIterator = mutableListOf<Int>()
  for (pair in pairsFromIterator) {
   keysFromIterator.add(pair.first)
  }
  assertThat(keysFromIterator).containsExactly(3, 4, 5, 6, 8, 9)
  assertThat(tree.root?.rightChild?.key).isEqualTo(8)
  assertThat(isBalanced(tree)).isTrue()
 }
@Test
fun `node remove from two children right`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 tree.insert(20, 1020)
 tree.insert(17, 1017)
 tree.insert(23, 1023)
 tree.remove(20)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(3, 5, 17, 23)
 assertThat(tree.root?.rightChild?.key).isEqualTo(23)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `remove node that not exist`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 tree.insert(7, 1007)
 val exception = assertThrows(IllegalArgumentException::class.java) {
  tree.remove(8)
 }
 assertThat(exception.message).contains("There is no such node.")
}
@Test
fun `remove node with two left children`() {
 tree.insert(10, 1010)
 tree.insert(9, 1009)
 tree.insert(17, 1017)
 tree.insert(8, 1008)
 tree.insert(25, 1025)
 tree.insert(16, 1016)
 tree.insert(15, 1015)
 tree.remove(10)
 assertThat(tree.root?.key).isEqualTo(15)
 val keysFromIterator = iter(tree)
 assertThat(keysFromIterator).containsExactly(8, 9, 15, 16, 17, 25)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
@Timeout(1, unit = TimeUnit.SECONDS)
fun `stress test`(){
 val insertAttempts = 100_000
 val removeAttempts = 10_000
 repeat(insertAttempts) {
  val key = Random.nextInt(0, 1_000_000)
  val value = Random.nextInt(0, 1_000_00)
  try {
   tree.insert(key, value)
  }
  catch (e: Exception) {
   fail("failed: $e")
  }
 }
 repeat(removeAttempts) {
  val key = Random.nextInt(0, 1_000_000)
  try {
   tree.remove(key)
  }
  catch (_: IllegalArgumentException){}
  catch (e: Exception) {
   fail("failed: $e")
  }
 }
 assertThat(isBalanced(tree)).isTrue()
}
}
