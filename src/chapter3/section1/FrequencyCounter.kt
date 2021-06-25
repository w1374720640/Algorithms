package chapter3.section1

import edu.princeton.cs.algs4.In
import extensions.inputPrompt
import extensions.readInt
import extensions.readString

/**
 * 统计输入流中出现频率最高的单词，可以设置统计的最短单词长度
 */
fun frequencyCounter(input: In, minLength: Int, st: ST<String, Int>): Pair<String, Int> {
    while (!input.isEmpty) {
        val word = input.readString()
        if (word.length >= minLength) {
            val count = st.get(word)
            if (count == null) {
                st.put(word, 1)
            } else {
                st.put(word, count + 1)
            }
        }
    }
    if (st.isEmpty()) {
        throw NoSuchElementException("ST is empty!")
    } else {
        var word = ""
        var maxCount = 0
        st.keys().forEach { key ->
            val count = st.get(key)!!
            if (count > maxCount) {
                word = key
                maxCount = count
            }
        }
        return word to maxCount
    }
}

fun frequencyCounter(input: In, minLength: Int, orderedST: OrderedST<String, Int>): Pair<String, Int> {
    while (!input.isEmpty) {
        val word = input.readString()
        if (word.length >= minLength) {
            val count = orderedST.get(word)
            if (count == null) {
                orderedST.put(word, 1)
            } else {
                orderedST.put(word, count + 1)
            }
        }
    }
    if (orderedST.isEmpty()) {
        throw NoSuchElementException("OrderedST is Empty!")
    } else {
        var word = ""
        var maxCount = 0
        orderedST.keys().forEach {key ->
            val count = orderedST.get(key)!!
            if (count > maxCount) {
                word = key
                maxCount = count
            }
        }
        return word to maxCount
    }
}

fun main() {
    inputPrompt()
    val minLength = readInt("minLength=")
    //  ./data/tinyTale.txt  ./data/tale.txt
    val path = readString("path=")
    val counter1 = frequencyCounter(In(path), minLength, LinkedListST())
    println("LinkedListST: ${counter1.first} ${counter1.second}")
    val counter2 = frequencyCounter(In(path), minLength, ArrayOrderedST())
    println("ArrayOrderedST: ${counter2.first} ${counter2.second}")
}