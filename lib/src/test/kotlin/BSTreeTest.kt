import bsTree.BST
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class BSTreeTest {
    private lateinit var tree: BST<Int, Int>

    @BeforeEach
    fun begin() {
        tree = BST()
    }

    @Test
    fun `insert in empty tree`() {
        tree.insert(1, 1)
        assertEquals(1, tree.find(1))
        assertEquals(1, tree.size)
    }

    @Test
    fun `insert right and left children`() {
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.insert(3, 3)
        assertEquals(3, tree.size)
        for (i in 1..3) {
            assertEquals(i, tree.find(i))
        }
    }

    @Test
    fun `insert number with key that already exists`() {
        tree.insert(1, 1)
        assertThrows<IllegalArgumentException> {
            tree.insert(1, 2)
        }
    }

    @Test
    fun `remove root`() {
        tree.insert(1, 1)
        tree.remove(1)
        assertNull(tree.find(1))
        assertEquals(0, tree.size)
    }

    @Test
    fun `remove root with one child`() {
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.remove(2)
        assertNull(tree.find(2))
        assertEquals(1, tree.size)
        assertEquals(1, tree.find(1))
    }

    @Test
    fun `remove root with two children`() {
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.insert(3, 3)
        tree.remove(2)
        assertNull(tree.find(2))
        assertEquals(2, tree.size)
        assertEquals(3, tree.find(3))
        assertEquals(1, tree.find(1))
    }

    @Test
    fun `remove left element without children`() {
        tree.insert(2, 2)
        tree.insert(1, 1)
        tree.remove(1)
        assertNull(tree.find(1))
        assertEquals(1, tree.size)
        assertEquals(2, tree.find(2))
    }

    @Test
    fun `remove right element without children`() {
        tree.insert(1, 1)
        tree.insert(2, 2)
        tree.remove(2)
        assertNull(tree.find(2))
        assertEquals(1, tree.size)
        assertEquals(1, tree.find(1))
    }

    @Test
    fun `remove element with left child`() {
        tree.insert(1, 1)
        tree.insert(3, 3)
        tree.insert(2, 2)
        tree.remove(3)
        assertNull(tree.find(3))
        assertEquals(2, tree.size)
        assertEquals(2, tree.find(2))
        assertEquals(1, tree.find(1))
    }

    @Test
    fun `remove element with right child`() {
        tree.insert(1, 1)
        tree.insert(2, 2)
        tree.insert(3, 3)
        tree.remove(2)
        assertNull(tree.find(2))
        assertEquals(2, tree.size)
        assertEquals(3, tree.find(3))
        assertEquals(1, tree.find(1))
    }

    @Test
    fun `remove element with two children`() {
        tree.insert(1, 1)
        tree.insert(3, 3)
        tree.insert(2, 2)
        tree.insert(4, 4)
        tree.remove(3)
        assertNull(tree.find(3))
        assertEquals(3, tree.size)
        assertEquals(1, tree.find(1))
        assertEquals(2, tree.find(2))
        assertEquals(4, tree.find(4))
    }

    @Test
    fun `remove element with two children and one has right child`() {
        tree.insert(1, 1)
        tree.insert(3, 3)
        tree.insert(2, 2)
        tree.insert(4, 4)
        tree.insert(5, 5)
        tree.remove(3)
        assertNull(tree.find(3))
        assertEquals(4, tree.size)
        assertEquals(1, tree.find(1))
        assertEquals(2, tree.find(2))
        assertEquals(4, tree.find(4))
        assertEquals(5, tree.find(5))
    }

    @Test
    fun `remove element with two children and one has left child`() {
        tree.insert(1, 1)
        tree.insert(3, 3)
        tree.insert(2, 2)
        tree.insert(5, 5)
        tree.insert(4, 4)
        tree.remove(3)
        assertNull(tree.find(3))
        assertEquals(4, tree.size)
        assertEquals(1, tree.find(1))
        assertEquals(2, tree.find(2))
        assertEquals(4, tree.find(4))
        assertEquals(5, tree.find(5))
    }

    @Test
    fun `remove element with two children and one has two children`() {
        tree.insert(1, 1)
        tree.insert(3, 3)
        tree.insert(2, 2)
        tree.insert(5, 5)
        tree.insert(4, 4)
        tree.insert(6, 6)
        tree.remove(3)
        assertNull(tree.find(3))
        assertEquals(5, tree.size)
        assertEquals(1, tree.find(1))
        assertEquals(2, tree.find(2))
        assertEquals(4, tree.find(4))
        assertEquals(5, tree.find(5))
        assertEquals(6, tree.find(6))
    }

    @Test
    fun `remove element with two children and two have right child`() {
        tree.insert(1, 1)
        tree.insert(4, 4)
        tree.insert(2, 2)
        tree.insert(3, 3)
        tree.insert(5, 5)
        tree.insert(6, 6)
        tree.remove(4)
        assertNull(tree.find(4))
        assertEquals(5, tree.size)
        assertEquals(1, tree.find(1))
        assertEquals(2, tree.find(2))
        assertEquals(3, tree.find(3))
        assertEquals(5, tree.find(5))
        assertEquals(6, tree.find(6))
    }

    @Test
    fun `remove element with two children and two have left child`() {
        tree.insert(1, 1)
        tree.insert(4, 4)
        tree.insert(3, 3)
        tree.insert(2, 2)
        tree.insert(6, 6)
        tree.insert(5, 5)
        tree.remove(4)
        assertNull(tree.find(4))
        assertEquals(5, tree.size)
        assertEquals(1, tree.find(1))
        assertEquals(2, tree.find(2))
        assertEquals(3, tree.find(3))
        assertEquals(5, tree.find(5))
        assertEquals(6, tree.find(6))
    }

    @Test
    fun `remove element with two children and two have two children`() {
        tree.insert(1, 1)
        tree.insert(5, 5)
        tree.insert(3, 3)
        tree.insert(2, 2)
        tree.insert(4, 4)
        tree.insert(7, 7)
        tree.insert(6, 6)
        tree.remove(5)
        assertNull(tree.find(5))
        assertEquals(6, tree.size)
        assertEquals(1, tree.find(1))
        assertEquals(2, tree.find(2))
        assertEquals(3, tree.find(3))
        assertEquals(4, tree.find(4))
        assertEquals(6, tree.find(6))
        assertEquals(7, tree.find(7))
    }

    @Test
    fun `remove element with key that does not exist`() {
        tree.insert(1, 1)
        assertThrows<IllegalArgumentException> {
            tree.remove(2)
        }
    }

    @RepeatedTest(3)
    fun `insert and remove 1000 random numbers`() {
        var treeSize = 0
        for (i in (1..1000).shuffled()) {
            tree.insert(i, i)
            treeSize++
            assertEquals(i, tree.find(i))
            assertEquals(treeSize, tree.size)
        }
        assertEquals(1000, tree.size)

        for (i in (1..1000).shuffled()) {
            tree.remove(i)
            treeSize--
            assertNull(tree.find(i))
            assertEquals(treeSize, tree.size)
        }
        assertEquals(0, tree.size)
    }
}
