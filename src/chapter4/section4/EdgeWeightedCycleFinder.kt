package chapter4.section4

import edu.princeton.cs.algs4.Stack

/**
 * 加权有向环的API
 * 参考[chapter4.section2.DirectedCycle]
 */
class EdgeWeightedCycleFinder(digraph: EdgeWeightedDigraph) {
    private val marked = BooleanArray(digraph.V)
    private val edgeTo = arrayOfNulls<DirectedEdge>(digraph.V)
    private val onStack = BooleanArray(digraph.V)
    private var stack: Stack<DirectedEdge>? = null

    init {
        for (v in 0 until digraph.V) {
            if (!marked[v]) {
                dfs(digraph, v)
            }
        }
    }

    private fun dfs(digraph: EdgeWeightedDigraph, v: Int) {
        marked[v] = true
        onStack[v] = true
        digraph.adj(v).forEach { edge ->
            if (hasCycle()) return
            val w = edge.to()
            if (!marked[w]) {
                edgeTo[w] = edge
                dfs(digraph, w)
            } else if (onStack[w]) {
                // 如果w点被当前方法调用栈访问过，说明当前路径上存在环
                stack = Stack<DirectedEdge>()
                stack!!.push(edge)
                var s = v
                while (s != w) {
                    stack!!.push(edgeTo[s])
                    s = edgeTo[s]!!.from()
                }
            }
        }
        // 方法退出时，表明v点未连接任何环结构，重置onStack对应位置的值，而不重置marked对应位置的值
        // 当通过其他路径再次遍历到v点时，既不用遍历v点连接的点，也不用加入到环的路径中
        onStack[v] = false
    }

    /**
     * 是否含有有向环
     */
    fun hasCycle(): Boolean {
        return stack != null
    }

    /**
     * 有向环中的所有顶点（如果存在的话）
     */
    fun cycle(): Iterable<DirectedEdge>? {
        return stack
    }
}

fun main() {
    val digraph = getTinyEWD()
    val cycle = EdgeWeightedCycleFinder(digraph)
    if (cycle.hasCycle()) {
        println("has cycle")
        println(cycle.cycle()!!.joinToString())
    } else {
        println("no cycle")
    }
}