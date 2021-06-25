package chapter2.exericise1_1

import chapter1.exercise1_3.*
import extensions.random

/**
 * 对扑克可操作的行为进行封装
 */
class Poker {
    val SIZE = 54
    private val list = DoublyLinkedList<Int>()
    //获取一副随机打乱的扑克牌
    init {
        val initList = DoublyLinkedList<Int>()
        repeat(SIZE) {
            initList.addTail(it)
        }
        while (!initList.isEmpty()) {
            val pick = random(0, initList.size())
            list.addTail(initList.delete(pick))
        }
    }

    fun look(): Pair<Int, Int> {
        return list.get(0) to list.get(1)
    }

    fun swap() {
        val second = list.delete(1)
        list.addHeader(second)
    }

    fun reverse() {
        val first = list.deleteHeader()
        list.addTail(first)
    }

    fun print() {
        println(list.joinToString())
    }
}

/**
 * 使用冒泡排序的思想排序
 * 比较头部两张牌的大小，保留大的值，小的放入牌底，一次循环后，最大的值在牌顶
 * 下次循环开始时，将已排序的值依次放入牌底，剩余的牌中再选出最大值，不断循环
 */
fun ex14(poker: Poker) {
    poker.print()
    for (i in poker.SIZE - 1 downTo 0) {
        repeat(poker.SIZE - 1 - i) {
            poker.reverse()
        }
        for (j in 0 until i) {
            val pair = poker.look()
            if (pair.first > pair.second) {
                poker.swap()
            }
            poker.reverse()
        }
    }
    poker.print()
}

fun main() {
    ex14(Poker())
}