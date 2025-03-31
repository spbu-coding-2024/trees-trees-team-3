import main.kotlin.AVL
import main.kotlin.AVLNode
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit
import kotlin.random.Random
internal class AVLTest {
 private var tree = AVL<Int, Int>()
 private fun isBalanced(tree: AVL<Int, Int>): Boolean {
  if (tree.getRoot() == null) return true
  return balanceCheck(tree.getRoot())
 }
 private fun balanceCheck(node: AVLNode<Int, Int>?): Boolean {
  if (node == null) return true
  return if (node.diff !in -1..1) {
   false
  } else balanceCheck(node.leftChild) && balanceCheck(node.rightChild)
 }
@BeforeEach
fun sutUp(){
 tree = AVL()
}
@Test
fun `insert root`() {
 tree.insert(5, 1005)
 assertThat(tree.getRoot()?.key).isEqualTo(5)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `iterator of null`(){
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly()
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `insert node that already exist`(){
 tree.insert(5, 1005)
 val exception = assertThrows(IllegalArgumentException::class.java) {
  tree.insert(5, 1005)
 }
 assertThat(exception.message).contains("It is not possible to insert a node " +
         "as such a node already exists.")
}
@Test
fun `insert left and right node`() {
 tree.insert(5, 1005)
 tree.insert(4, 1004)
 tree.insert(40, 1040)
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly(4, 5, 40)
 assertThat(tree.getRoot()?.key).isEqualTo(5)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `small left rotate`() {
 tree.insert(5, 1005)
 tree.insert(4, 1004)
 tree.insert(40, 1040)
 tree.insert(70, 1070)
 tree.insert(80, 1080)
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
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
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly(2, 3, 5, 7, 10, 15)
 assertThat(tree.getRoot()?.key).isEqualTo(5)
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
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly(4, 5, 40, 45, 55, 70)
 assertThat(tree.getRoot()?.key).isEqualTo(40)
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
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly(3, 5, 6, 7, 10, 15)
 assertThat(tree.getRoot()?.key).isEqualTo(7)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `root delete`(){
 tree.insert(1, 1001)
 tree.remove(1)
 assertThat(tree.getRoot()).isNull()
}
@Test
fun `left node remove`(){
 tree.insert(5, 1005)
 tree.insert(4, 1004)
 tree.remove(4)
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
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
fun `deleting node have right child and is left child`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 tree.insert(6, 1006)
 tree.insert(4, 1004)
 tree.remove(3)
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly(4, 5, 6)
 assertThat(tree.getRoot()?.key).isEqualTo(5)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `deleting node have left child and is left child`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 tree.insert(6, 1006)
 tree.insert(2, 1002)
 tree.remove(3)
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly(2, 5, 6)
 assertThat(tree.getRoot()?.key).isEqualTo(5)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `deleting node that minimum is right and have right`(){
 tree.insert(10, 1010)
 tree.insert(5, 1005)
 tree.insert(15, 1015)
 tree.insert(20, 1020)
 tree.remove(10)
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly(5, 15, 20)
 assertThat(tree.getRoot()?.key).isEqualTo(15)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
fun `deleting node that have two childes and minimum have right`(){
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
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
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
  assertThat(tree.getRoot()?.key).isEqualTo(4)
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
  assertThat(tree.getRoot()?.key).isEqualTo(6)
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
  assertThat(tree.getRoot()?.key).isEqualTo(6)
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
  assertThat(tree.getRoot()?.rightChild?.key).isEqualTo(7)
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
  assertThat(tree.getRoot()?.rightChild?.key).isEqualTo(6)
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
  assertThat(tree.getRoot()?.rightChild?.key).isEqualTo(8)
  assertThat(isBalanced(tree)).isTrue()
 }
@Test
fun `node remove from two child right`(){
 tree.insert(5, 1005)
 tree.insert(3, 1003)
 tree.insert(20, 1020)
 tree.insert(17, 1017)
 tree.insert(23, 1023)
 tree.remove(20)
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly(3, 5, 17, 23)
 assertThat(tree.getRoot()?.rightChild?.key).isEqualTo(23)
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
fun `remove node with two left childes`() {
 tree.insert(10, 1010)
 tree.insert(9, 1009)
 tree.insert(17, 1017)
 tree.insert(8, 1008)
 tree.insert(25, 1025)
 tree.insert(16, 1016)
 tree.insert(15, 1015)
 tree.remove(10)
 assertThat(tree.getRoot()?.key).isEqualTo(15)
 val pairsFromIterator = tree.iterator()
 val keysFromIterator = mutableListOf<Int>()
 for (pair in pairsFromIterator) {
  keysFromIterator.add(pair.first)
 }
 assertThat(keysFromIterator).containsExactly(8, 9, 15, 16, 17, 25)
 assertThat(isBalanced(tree)).isTrue()
}
@Test
@Timeout(1, unit = TimeUnit.SECONDS)
fun `stress test`(){
 val insertAttempts = 100_000
 val removeAttempts = 10_000
 repeat(insertAttempts) {
  val key = Random.nextInt(0, 999)
  val value = Random.nextInt(0, 999)
  try {
   tree.insert(key, value)
  }
  catch (e: IllegalArgumentException){}
  catch (e: Exception) {
   fail("failed")
  }
 }
 repeat(removeAttempts) {
  val key = Random.nextInt(0, 999)
  try {
   tree.remove(key)
  }
  catch (e: IllegalArgumentException){}
  catch (e: Exception) {
   fail("failed")
  }
 }
 assertThat(isBalanced(tree)).isTrue()
}
}
