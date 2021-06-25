package chapter2.exericise1_1

import chapter2.showSortingProcess
import extensions.delayExit

fun ex1(array: Array<Char>, delay: Long) {
    val doubleArray = Array(array.size) { array[it].toDouble() }
    showSortingProcess(doubleArray, ::selectSort, delay)
    delayExit()
}

fun main() {
    ex1(arrayOf('E', 'A', 'S', 'Y', 'Q', 'U', 'E', 'S', 'T', 'I', 'O', 'N'), 1000)
}