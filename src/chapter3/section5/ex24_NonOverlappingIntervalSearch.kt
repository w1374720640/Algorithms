package chapter3.section5

/**
 * 不重叠的区间查找
 * 给定对象的一组互不重叠的区间，编写一个函数接受一个对象作为参数并判断它是否存在于其中任何一个区间内
 * 例如，如果对象是整数而区间为1643-2033，5532-7643，8998-10332，5666653-5669321
 * 那么查询9122的结果为在第三个区间，而8122的结果不在任何区间
 *
 * 解：Kotlin中有自带的区间类[kotlin.ranges.IntRange]，先对区间数组按起始位置排序，再对数组二分查找
 */
fun ex24_NonOverlappingIntervalSearch(rangeArray: Array<IntRange>, key: Int): IntRange? {
    rangeArray.sortBy { it.first }
    val index = searchInRangeArray(rangeArray, key)
    return if (index == -1) null else rangeArray[index]
}

private fun searchInRangeArray(rangeArray: Array<IntRange>, key: Int): Int {
    var low = 0
    var high = rangeArray.size - 1
    while (low <= high) {
        val mid = (low + high) / 2
        val midRange = rangeArray[mid]
        when {
            key in midRange -> return mid
            key < midRange.first -> high = mid - 1
            else -> low = mid + 1
        }
    }
    return -1
}

fun main() {
    val rangeArray = arrayOf(1643..2033, 5532..7643, 8998..10332, 5666653..5669321)
    println(ex24_NonOverlappingIntervalSearch(rangeArray, 9122))
    println(ex24_NonOverlappingIntervalSearch(rangeArray, 8122))
}