package chapter5.section2

import chapter3.section1.OrderedST
import chapter5.section1.Alphabet
import edu.princeton.cs.algs4.Queue

/**
 * 实现有序符号表API的单词查找树
 */
class OrderedTriesST<V : Any>(alphabet: Alphabet) : TrieST<V>(alphabet), OrderedST<String, V> {

    override fun min(): String {
        return min(root, "") ?: throw NoSuchElementException()
    }

    private fun min(node: Node, key: String): String? {
        if (node.value != null) return key
        for (i in node.next.indices) {
            if (node.next[i] != null) {
                return min(node.next[i]!!, key + alphabet.toChar(i))
            }
        }
        return null
    }

    override fun max(): String {
        return max(root, "") ?: throw NoSuchElementException()
    }

    private fun max(node: Node, key: String): String? {
        for (i in node.next.size - 1 downTo 0) {
            if (node.next[i] != null) {
                return max(node.next[i]!!, key + alphabet.toChar(i))
            }
        }
        return if (node.value != null) key else null
    }

    override fun floor(key: String): String {
        return floor(root, key, 0) ?: throw NoSuchElementException()
    }

    private fun floor(node: Node, key: String, d: Int): String? {
        if (d == key.length) {
            return if (node.value == null) null else key
        }
        val index = alphabet.toIndex(key[d])
        val nextNode = node.next[index]
        if (nextNode != null) {
            val result = floor(nextNode, key, d + 1)
            if (result != null) return result
        }
        for (i in index - 1 downTo 0) {
            if (node.next[i] != null) {
                return max(node.next[i]!!, key.substring(0, d) + alphabet.toChar(i))
            }
        }
        return if (node.value == null) null else key.substring(0, d)
    }

    override fun ceiling(key: String): String {
        return ceiling(root, key, 0) ?: throw NoSuchElementException()
    }

    private fun ceiling(node: Node, key: String, d: Int): String? {
        if (d == key.length) {
            return if (node.value == null) null else key
        }
        val index = alphabet.toIndex(key[d])
        val nextNode = node.next[index]
        if (nextNode == null) {
            for (i in index + 1 until node.next.size) {
                if (node.next[i] != null) {
                    return min(node.next[i]!!, key.substring(0, d) + alphabet.toChar(i))
                }
            }
            return null
        } else {
            return ceiling(nextNode, key, d + 1) ?: min(nextNode, key.substring(0, d + 1))
        }
    }

    override fun rank(key: String): Int {
        return rank(root, key, 0)
    }

    private fun rank(node: TrieST<V>.Node, key: String, d: Int): Int {
        if (d == key.length) return 0
        val index = alphabet.toIndex(key[d])
        var count = 0
        for (i in 0 until index) {
            val nextNode = node.next[i]
            if (nextNode != null) {
                count += size(nextNode)
            }
        }
        if (node.value != null) count++
        val nextNode = node.next[index]
        if (nextNode != null) {
            count += rank(nextNode, key, d + 1)
        }
        return count
    }

    private fun size(node: Node): Int {
        var count = 0
        node.next.forEach { nextNode ->
            if (nextNode != null) {
                count += size(nextNode)
            }
        }
        return if (node.value == null) count else count + 1
    }

    override fun select(k: Int): String {
        return select(root, k, "")
    }

    private fun select(node: Node, k: Int, key: String): String {
        var count = if (node.value == null) 0 else 1
        if (k == count - 1) return key
        for (i in node.next.indices) {
            val nextNode = node.next[i]
            if (nextNode != null) {
                val size = size(nextNode)
                if (k < count + size) {
                    return select(nextNode, k - count, key + alphabet.toChar(i))
                }
                count += size
            }
        }
        throw NoSuchElementException()
    }

    override fun deleteMin() {
        delete(min())
    }

    override fun deleteMax() {
        delete(max())
    }

    override fun size(low: String, high: String): Int {
        if (low > high) return 0
        return if (contains(high)) {
            rank(high) - rank(low) + 1
        } else {
            rank(high) - rank(low)
        }
    }

    override fun keys(low: String, high: String): Iterable<String> {
        val queue = Queue<String>()
        if (low > high) return queue
        keys(root, queue, "", low, high, 0)
        return queue
    }

    private fun keys(node: Node, queue: Queue<String>, key: String, low: String, high: String, d: Int) {
        val minLength = minOf(low.length, high.length)
        if (d == minLength) {
            if (low.length > high.length) return
            if (low.length == high.length) {
                if (node.value != null) {
                    queue.enqueue(key)
                }
                return
            }
            val highIndex = alphabet.toIndex(high[d])
            val highNode = node.next[highIndex]
            if (highNode != null) {
                keysLessThan(highNode, queue, key + alphabet.toChar(highIndex), high, d + 1)
            }
            return
        }
        val lowIndex = alphabet.toIndex(low[d])
        val highIndex = alphabet.toIndex(high[d])
        if (lowIndex == highIndex) {
            val nextNode = node.next[lowIndex] ?: return
            keys(nextNode, queue, key + alphabet.toChar(lowIndex), low, high, d + 1)
        } else {
            val lowNode = node.next[lowIndex]
            if (lowNode != null) {
                keysGreaterThan(lowNode, queue, key + alphabet.toChar(lowIndex), low, d + 1)
            }
            for (i in lowIndex + 1 until highIndex) {
                val nextNode = node.next[i]
                if (nextNode != null) {
                    keys(nextNode, queue, key + alphabet.toChar(i))
                }
            }
            val highNode = node.next[highIndex]
            if (highNode != null) {
                keysLessThan(highNode, queue, key + alphabet.toChar(highIndex), high, d + 1)
            }
        }
    }

    private fun keysGreaterThan(node: Node, queue: Queue<String>, key: String, low: String, d: Int) {
        if (d == low.length) {
            keys(node, queue, key)
            return
        }
        val index = alphabet.toIndex(low[d])
        val nextNode = node.next[index]
        if (nextNode != null) {
            keysGreaterThan(nextNode, queue, key + low[d], low, d + 1)
        }
        for (i in index + 1 until node.next.size) {
            val greaterNode = node.next[i]
            if (greaterNode != null) {
                keys(greaterNode, queue, key + alphabet.toChar(i))
            }
        }
    }

    private fun keysLessThan(node: Node, queue: Queue<String>, key: String, high: String, d: Int) {
        if (d == high.length) {
            if (node.value != null) {
                queue.enqueue(key)
            }
            return
        }
        val index = alphabet.toIndex(high[d])
        for (i in 0 until index) {
            val lessNode = node.next[i]
            if (lessNode != null) {
                keys(lessNode, queue, key + alphabet.toChar(i))
            }
        }
        if (node.value != null) queue.enqueue(key)
        val nextNode = node.next[index]
        if (nextNode != null) {
            keysLessThan(nextNode, queue, key + high[d], high, d + 1)
        }
    }
}

fun main() {
    testStringST { OrderedTriesST(Alphabet.EXTENDED_ASCII) }
    testOrderedStringST { OrderedTriesST(Alphabet.EXTENDED_ASCII) }
}