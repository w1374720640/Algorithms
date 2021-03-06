package chapter2.section3

import chapter2.*
import chapter2.section1.cornerCases
import chapter2.section1.doublingTest
import extensions.*

/**
 * 快速排序的3路算法
 * 用于排序只有有限主键的数组时，时间复杂度为O(N)
 *
 * 具体实现：以数组第一个值A为基准，将数组从左到右分为四部分
 * 第一部分所有元素都小于A，初始大小为0
 * 第二部分所有元素都等于A，初始大小为1
 * 第三部分为未对比元素，初始大小为size-1，最终会缩小为0
 * 第四部分所有元素都大于A，初始大小为0
 * 从第二个元素开始向右遍历，
 * 如果元素小于A，则与第二部分的第一个值交换，第一部分向右扩展一位，大小加一，第二部分左侧减少一位右侧多一位，大小不变
 * 如果元素等于A，则第二部分向右扩展一位
 * 如果元素大于A，则与第三部分的最后一个值交换，第四部分向左扩展一位，大小加一
 * 不断循环，直到第三部分大小减为0，递归排序第一和第四部分
 */
fun <T : Comparable<T>> quickSort3Way(array: Array<T>) {
    quickSort3Way(array, 0, array.size - 1)
}

fun <T : Comparable<T>> quickSort3Way(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    var lowerThan = start
    var greaterThan = end
    var i = start + 1
    val value = array[start]
    while (i <= greaterThan) {
        val result = array.compare(i, value)
        when {
            result < 0 -> {
                if (lowerThan != i) {
                    array.swap(lowerThan, i)
                }
                lowerThan++
                i++
            }
            result > 0 -> {
                if (greaterThan != i) {
                    array.swap(greaterThan, i)
                }
                greaterThan--
            }
            else -> i++
        }
    }
    quickSort3Way(array, start, lowerThan - 1)
    quickSort3Way(array, greaterThan + 1, end)
}

private fun drawSortingProcess() {
    inputPrompt()
    val size = readInt("size: ")
    val delay = readLong("delay: ")
    val array = Array(size) { random(5).toDouble() }
    showSortingProcess(array, ::quickSort3Way, delay, true)
    delayExit()
}

//时间复杂度为O(N)
private fun growthTest() {
    doublingTest(1000_0000, ::quickSort3Way, createArray = {
        getDoubleArray(it, ArrayInitialState.REPEAT)
    }, O = { N -> N.toDouble() })
}

fun main() {
    cornerCases(::quickSort3Way)
    drawSortingProcess()
//    performanceTesting("quickSort3Way", ::quickSort3Way)
//    growthTest()
}