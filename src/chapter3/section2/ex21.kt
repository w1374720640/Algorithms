package chapter3.section2

import extensions.random
import extensions.shuffle

/**
 * 为二叉查找树添加一个randomKey()方法来再和树高成正比的时间内从符号表中随机返回一个键
 */
fun <K: Comparable<K>, V: Any> BinarySearchTree<K, V>.randomKey(): K {
    return select(random(size()))
}

fun main() {
    val size = 1_0000
    val times = 100_0000

    val bst = BinarySearchTree<Int, Int>()
    val array = Array(size) {it}
    array.shuffle()
    array.forEach {
        bst.put(it, 0)
    }

    val result = IntArray(size)
    repeat(times) {
        result[bst.randomKey()]++
    }
    println("expect=${times / size}")
    //打印每个索引出现的次数
    println(result.joinToString(limit = 100))
}