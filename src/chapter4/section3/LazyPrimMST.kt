package chapter4.section3

import chapter2.section4.HeapMinPriorityQueue

/**
 * 最小生成树的Prim算法的延时实现
 */
class LazyPrimMST(graph: EWG) : MST() {
    private val marked = BooleanArray(graph.V)
    private val minPQ = HeapMinPriorityQueue<Edge>()

    init {
        visit(graph, 0)
        while (!minPQ.isEmpty()) {
            val edge = minPQ.delMin()
            val v = edge.either()
            val w = edge.other(v)
            if (marked[v] && marked[w]) continue
            queue.enqueue(edge)
            if (!marked[v]) visit(graph, v)
            if (!marked[w]) visit(graph, w)
            weight += edge.weight
        }
        check(queue.size() == graph.V - 1) { "All vertices should be connected." }
    }

    private fun visit(graph: EWG, v: Int) {
        marked[v] = true
        graph.adj(v).forEach {
            if (!marked[it.other(v)]) {
                minPQ.insert(it)
            }
        }
    }
}

fun main() {
    val graph = getTinyWeightedGraph()
    val lazyPrimMST = LazyPrimMST(graph)
    println(lazyPrimMST.toString())

    drawRandomEWG { LazyPrimMST(it) }
}