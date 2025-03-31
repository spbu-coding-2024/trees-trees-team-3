import main.kotlin.AVL
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

internal class TreeMapTest {
  private var tree = AVL<Int, Int>()
 private fun()
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

}