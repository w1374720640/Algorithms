package capter1.exercise1_3

import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 接受一条链表的首结点作为参数，破坏性地将链表反转并返回结果链表的首结点
 * 原链表只剩一个头结点，其他节点丢失
 * 每次操作保存三个结点，将中间结点的next指针指向第一个结点，
 * 然后将三个结点同步后移一位，依次循环直到最后一个结点
 */
fun <T> reverse(node: Node<T>): Node<T> {
    var first = node
    var reverse = node.next
    first.next = null
    while (reverse != null) {
        val last = reverse.next
        reverse.next = first
        first = reverse
        reverse = last
    }
    return first
}


fun main() {
    inputPrompt()
    val array = readAllStrings()
    val list = SinglyLinkedList<String>()
    list.addAll(array.iterator())
    println("origin list = ${list.joinToSting()}")
    if (list.first == null) {
        println("list is empty")
    } else {
        val node = reverse(list.first!!)
        println("reverse end list = ${list.joinToSting()}")
        println("new list first node = ${node.item}")
        println("new list = ${SinglyLinkedList<String>().run {
            first = node
            joinToSting()
        }}")
    }
}