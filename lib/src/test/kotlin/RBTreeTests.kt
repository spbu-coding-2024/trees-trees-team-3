import rbTree.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
// Визуализатор: https://www.cs.usfca.edu/~galles/visualization/RedBlack.html


class Insertion {
    private var map: RBTree<Int, Int> = RBTree<Int, Int>()

    fun setup() {
        map = RBTree<Int, Int>()
    }

    @Test
    @DisplayName("Insert in empty tree")
    fun insertRoot() {
        map.insert(1, 1)
        var ans = ArrayDeque<Triple<Int?, Int?, Color?>?>()
        ans.addLast(Triple(1, 1, Color.BLACK))
        ans.addLast(null)
        ans.addLast(null)
        assertEquals(ans, map.printTree())
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Insert root's childen")
    fun insertChildren() {
        map.insert(1, 1)
        map.insert(-1, 1)
        map.insert(2, 1)
        var ans = arrayListOf<Triple<Int?, Int?, Color?>?>(
            Triple(1, 1, Color.BLACK), Triple(-1, 1, Color.RED), Triple(2, 1, Color.RED), null, null, null, null
        )
        assertEquals(ans, map.printTree())
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Insert node with red uncle")
    fun insertWithRedUnc() {
        map.insert(1, 1)
        map.insert(-1, 1)
        map.insert(2, 1)
        map.insert(-2, 1)
        var ans = arrayListOf<Triple<Int?, Int?, Color?>?>(
            Triple(1, 1, Color.BLACK), Triple(-1, 1, Color.BLACK), Triple(2, 1, Color.BLACK), Triple(-2, 1, Color.RED),
            null, null, null, null, null
        )
        assertEquals(ans, map.printTree())
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Insert node with black uncle")
    fun insertWithBlackUnc(){
        map.insert(1, 1)
        map.insert(-1, 1)
        map.insert(2, 1)
        map.insert(-2, 1)
        map.insert(-3, 1)
        var ans = arrayListOf<Triple<Int?, Int?, Color?>?>(
            Triple(1, 1, Color.BLACK), Triple(-1, 1, Color.BLACK), Triple(2, 1, Color.BLACK),
            Triple(-3, 1, Color.RED), Triple(-2, 1, Color.RED), null, null, null, null, null, null
        )
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Insert another node with red uncle")
    fun anotherInsertWithRedUnc() {
        map.insert(1, 1)
        map.insert(-1, 1)
        map.insert(2, 1)
        map.insert(-2, 1)
        map.insert(-3, 1)
        map.insert(0, 1)
        var ans = arrayListOf<Triple<Int?, Int?, Color?>?>(
            Triple(1, 1, Color.BLACK), Triple(-2, 1, Color.RED), Triple(2, 1, Color.BLACK),
            Triple(-3, 1, Color.BLACK), Triple(-1, 1, Color.BLACK), null, null, null, null, null, Triple(0, 1, Color.RED),
            null, null
        )
        assertEquals(ans, map.printTree())
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Stress test for insertion")
    fun stress() {
        repeat (100) {
            map.insert((0..10_000).random(), 0)
        }
        assertEquals(true, map.checkBalance())
    }
}


class Deletion {
    private lateinit var map: RBTree<Int, Int>

    @Test
    @DisplayName("Delete red node")
    fun deleteRed() {
        map = RBTree<Int, Int>()
        for (i in 0..10) {
            map.insert(i, 0)
        }
        map.remove(10)
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Delete red parent, black brother with red child")
    fun delete1() {
        map = RBTree<Int, Int>()
        for (i in 0..10) {
            map.insert(i, 0)
        }
        map.remove(6)
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Delete red parent, black brother with black children")
    fun delete2() {
        map = RBTree<Int, Int>()
        for (i in 0..11) {
            map.insert(i, 0)
        }
        map.remove(4)
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Delete black parent, red brother with right child's black children")
    fun delete3() {
        map = RBTree<Int, Int>()
        map.insert(10, 0)
        map.insert(5, 0)
        map.insert(20, 0)
        map.insert(15, 0)
        map.insert(8, 0)
        map.insert(25, 0)
        map.insert(30, 0)
        map.remove(30)
        map.remove(15)
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Delete black parent, red brother and right child has red child")
    fun delete4() {
        map = RBTree<Int, Int>()
        map.insert(20, 0)
        map.insert(10, 0)
        map.insert(30, 0)
        map.insert(25, 0)
        map.insert(35, 0)
        map.insert(28, 0)
        map.remove(10)
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Delete black parent, black brother with red child")
    fun delete5() {
        map = RBTree<Int, Int>()
        map.insert(2, 0)
        map.insert(3, 0)
        map.insert(1, 0)
        map.insert(0, 0)
        map.remove(3)
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Delete black parent, black brother with black children")
    fun delete6() {
        map = RBTree<Int, Int>()
        map.insert(2, 0)
        map.insert(1, 0)
        map.insert(3, 0)
        map.insert(0, 0)
        map.remove(0)
        map.remove(1)
        assertEquals(true, map.checkBalance())
    }

    @Test
    @DisplayName("Stress test for deletion")
    fun stress() {
        repeat(100) {
            repeat(100) {
                map = RBTree<Int, Int>()
                map.insert((0..100).random(), 0)
            }
            var to_remove = (0..100).random()
            if (map.contains(to_remove)) {
                map.remove(to_remove)
            }
            assertEquals(true, map.checkBalance())
        }

    }
}
