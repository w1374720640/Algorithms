package chapter3.section5

/**
 * 无序集合
 */
interface SET<K : Any> : Iterable<K> {
    fun add(key: K)

    fun delete(key: K)

    fun contains(key: K): Boolean

    fun isEmpty(): Boolean

    fun size(): Int
}