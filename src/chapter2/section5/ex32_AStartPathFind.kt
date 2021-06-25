package chapter2.section5

import chapter1.section3.*
import chapter2.section4.HeapMinPriorityQueue
import chapter2.sleep
import edu.princeton.cs.algs4.StdDraw
import java.awt.Color
import kotlin.math.abs

/**
 * A*寻路算法
 * 参考文章：https://hogwartsrico.github.io/2016/03/11/AStarPathFinding/index.html
 * 在线演示：http://qiao.github.io/PathFinding.js/visual/
 * A*寻路算法用于在有障碍的地图中找到两点之间的最短路径，每次移动可以向四周8个方向中的一个方向前进一步
 *
 * 算法原理直接参考上面的参考文章，这里直接给出Kotlin实现的代码，并用StdDraw绘制寻路过程
 */
class AStartPathFind(startPoint: Point, endPoint: Point, val canWalkDiagonally: Boolean = true, val drawingProcess: Boolean = false) {
    companion object {
        const val mapSize = 20
        const val side = 1.0 / mapSize
        const val dashWidth = side / 50
        const val lineWidth = dashWidth * 4
    }

    class Point(val x: Int, val y: Int, var parent: Point? = null) : Comparable<Point> {
        init {
            check(x in 0 until mapSize && y in 0 until mapSize)
        }

        enum class State {
            DEFAULT, START, END, OPEN, CLOSE, BARRIER
        }

        var state = State.DEFAULT

        var G = -1 //从起始点移动到该点的成本
            private set
        var H = -1 //从该点移动到终点的估算成本
            private set
        val F: Int //总的成本
            get() = G + H

        /**
         * 根据父结点计算G的值，根据曼哈顿距离计算H的值
         * 曼哈顿距离等于x坐标差值的绝对值和y坐标差值的绝对值之和
         * 表示在只能水平移动或垂直移动时从一个点移动到另一个点需要移动的次数（本题中可以沿对角线移动）
         * 也可以用欧几里得距离计算（两点之间的直线距离）
         */
        fun calculateDistance(endPoint: Point) {
            //parent为空时，G=0，parent不为空时，G=parent.G + 1
            G = (parent?.G ?: -1) + 1
            H = abs(endPoint.x - x) + abs(endPoint.y - y)
        }

        //比较大小时用总成本比较
        override fun compareTo(other: Point): Int {
            require(G >= 0 && H >= 0 && other.G >= 0 && H >= 0) { "Not initialized properly" }
            return F.compareTo(other.F)
        }

        //判断点是否相等时用坐标判断，和compareTo方法的结果不一致
        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (this === other) return true
            if (other !is Point) return false
            return x == other.x && y == other.y
        }

        override fun hashCode(): Int {
            return x + 31 * y
        }

        fun draw() {
            val color = Color(when (state) {
                State.DEFAULT -> 0xffffff
                State.START -> 0x00dd00
                State.END -> 0xee4400
                State.OPEN -> 0x98fb98
                State.CLOSE -> 0xafeeee
                State.BARRIER -> 0x808080
            })
            StdDraw.setPenColor(color)
            StdDraw.filledRectangle((x + 0.5) * side, (y + 0.5) * side, (side - dashWidth) / 2, (side - dashWidth) / 2)
        }

        fun drawTo(other: Point) {
            StdDraw.setPenColor(Color(0xffff00))
            StdDraw.setPenRadius(lineWidth)
            StdDraw.line((x + 0.5) * side, (y + 0.5) * side, (other.x + 0.5) * side, (other.y + 0.5) * side)
        }
    }

    //因为地图不大，直接生成所有的点
    private val allPoints = Array(mapSize) { y ->
        Array(mapSize) { x ->
            Point(x, y)
        }
    }
    private val startPoint = getPoint(startPoint.x, startPoint.y)
    private val endPoint = getPoint(endPoint.x, endPoint.y)
    var drawDelayTime = 100L

    init {
        this.startPoint.state = Point.State.START
        this.endPoint.state = Point.State.END
        if (drawingProcess) {
            drawBackground()
            this.startPoint.draw()
            this.endPoint.draw()
        }
    }

    //用于存放已经计算过总成本的点，可以快速获取当前总成本最小的点
    private val openPQ = HeapMinPriorityQueue<Point>()

    private fun drawBackground() {
        if (!drawingProcess) return
        StdDraw.setPenColor(Color(0xcecece))
        StdDraw.setPenRadius(dashWidth)
        for (i in 1..19) {
            StdDraw.line(0.0, i * side, 1.0, i * side)
        }
        for (i in 1..19) {
            StdDraw.line(i * side, 0.0, i * side, 1.0)
        }
    }

    fun getPoint(x: Int, y: Int): Point {
        require(x in 0 until mapSize && y in 0 until mapSize)
        return allPoints[y][x]
    }

    fun setBarriers(barriers: Array<Point>) {
        barriers.forEach {
            val barrier = getPoint(it.x, it.y)
            barrier.state = Point.State.BARRIER
            if (drawingProcess) {
                barrier.draw()
            }
        }
    }

    /**
     * 开始查找，返回途径的每个点
     * 不包括起始点，包括结束点，所以列表长度等于需要走的步数
     */
    fun find(): DoublyLinkedList<Point> {
        val list = DoublyLinkedList<Point>()
        if (startPoint == endPoint) return list
        startPoint.calculateDistance(endPoint)
        openPQ.insert(startPoint)
        while (!openPQ.isEmpty()) {
            //获取Open列表中总成本最小的点
            var point = openPQ.delMin()
            //如果列表中包含结束点，则结束点的总成本最小
            if (point == endPoint) {
                list.addHeader(point)
                var parent = point.parent
                while (parent != null) {
                    if (drawingProcess) {
                        point.drawTo(parent)
                    }
                    list.addHeader(parent)
                    point = parent
                    parent = parent.parent
                }
                return list
            }

            val childList = getPointsAround(point)
            childList.forEach {
                openPQ.insert(it)
            }
            if (point != startPoint) {
                point.state = Point.State.CLOSE
            }
            if (drawingProcess) {
                point.draw()
                sleep(drawDelayTime)
            }
        }
        return list
    }

    /**
     * 获取一个点周围能直接到达的点
     */
    fun getPointsAround(point: Point): SinglyLinkedList<Point> {
        val list = SinglyLinkedList<Point>()
        //上下左右四个点
        if (point.x > 0) checkPointValid(getPoint(point.x - 1, point.y), point, list)
        if (point.y > 0) checkPointValid(getPoint(point.x, point.y - 1), point, list)
        if (point.x < mapSize - 1) checkPointValid(getPoint(point.x + 1, point.y), point, list)
        if (point.y < mapSize - 1) checkPointValid(getPoint(point.x, point.y + 1), point, list)

        if (canWalkDiagonally) {
            //如果左侧和下方有障碍，则无法直接到达左下角
            //左下角
            if (point.x > 0 && point.y > 0
                    && (getPoint(point.x - 1, point.y).state != Point.State.BARRIER
                            || getPoint(point.x, point.y - 1).state != Point.State.BARRIER)) {
                checkPointValid(getPoint(point.x - 1, point.y - 1), point, list)
            }
            //右下角
            if (point.x < mapSize - 1 && point.y > 0
                    && (getPoint(point.x, point.y - 1).state != Point.State.BARRIER
                            || getPoint(point.x + 1, point.y).state != Point.State.BARRIER)) {
                checkPointValid(getPoint(point.x + 1, point.y - 1), point, list)
            }
            //右上角
            if (point.x < mapSize - 1 && point.y < mapSize - 1
                    && (getPoint(point.x + 1, point.y).state != Point.State.BARRIER
                            || getPoint(point.x, point.y + 1).state != Point.State.BARRIER)) {
                checkPointValid(getPoint(point.x + 1, point.y + 1), point, list)
            }
            //左上角
            if (point.x > 0 && point.y < mapSize - 1
                    && (getPoint(point.x - 1, point.y).state != Point.State.BARRIER
                            || getPoint(point.x, point.y + 1).state != Point.State.BARRIER)) {
                checkPointValid(getPoint(point.x - 1, point.y + 1), point, list)
            }
        }
        return list
    }

    /**
     * 检查子节点是否有效
     */
    private fun checkPointValid(child: Point, parent: Point, list: SinglyLinkedList<Point>) {
        when (child.state) {
            Point.State.START, Point.State.OPEN, Point.State.CLOSE, Point.State.BARRIER -> return
            Point.State.DEFAULT -> {
                child.state = Point.State.OPEN
                if (drawingProcess) {
                    child.draw()
                }
            }
            Point.State.END -> {
            }
        }
        child.parent = parent
        child.calculateDistance(endPoint)
        list.add(child)
    }
}

fun main() {
    val startPoint = AStartPathFind.Point(5, 10)
    val endPoint = AStartPathFind.Point(15, 10)
    val find = AStartPathFind(startPoint, endPoint, canWalkDiagonally = true, drawingProcess = true)
    //通过设置不同类型的障碍来观察寻路过程
    find.setBarriers(getBarriers(1))
    val list = find.find()
    if (list.isEmpty()) {
        if (startPoint == endPoint) {
            println("No need to move")
        } else {
            println("Cannot move to the end point")
        }
    } else {
        println("Need to move ${list.size()} times")
        list.forwardIterator().forEach {
            print("(${it.x},${it.y}) ")
        }
    }
}

/**
 * 获取不同类型的障碍
 */
fun getBarriers(which: Int): Array<AStartPathFind.Point> {
    return when (which) {
        //和y轴的平行的障碍
        1 -> arrayOf(
                AStartPathFind.Point(10, 8),
                AStartPathFind.Point(10, 9),
                AStartPathFind.Point(10, 10),
                AStartPathFind.Point(10, 11),
                AStartPathFind.Point(10, 12)
        )
        //半包围起始点的障碍
        2 -> arrayOf(
                AStartPathFind.Point(4, 7),
                AStartPathFind.Point(5, 7),
                AStartPathFind.Point(6, 7),
                AStartPathFind.Point(7, 7),
                AStartPathFind.Point(8, 7),
                AStartPathFind.Point(8, 8),
                AStartPathFind.Point(8, 9),
                AStartPathFind.Point(8, 10),
                AStartPathFind.Point(8, 11),
                AStartPathFind.Point(8, 12),
                AStartPathFind.Point(8, 13),
                AStartPathFind.Point(7, 13),
                AStartPathFind.Point(6, 13),
                AStartPathFind.Point(5, 13),
                AStartPathFind.Point(4, 13)
        )
        //两条平行的障碍
        3 -> arrayOf(
                AStartPathFind.Point(8, 8),
                AStartPathFind.Point(8, 9),
                AStartPathFind.Point(8, 10),
                AStartPathFind.Point(8, 11),
                AStartPathFind.Point(8, 12),
                AStartPathFind.Point(8, 13),
                AStartPathFind.Point(8, 14),
                AStartPathFind.Point(8, 15),
                AStartPathFind.Point(12, 13),
                AStartPathFind.Point(12, 12),
                AStartPathFind.Point(12, 11),
                AStartPathFind.Point(12, 10),
                AStartPathFind.Point(12, 9),
                AStartPathFind.Point(12, 8),
                AStartPathFind.Point(12, 7),
                AStartPathFind.Point(12, 6),
                AStartPathFind.Point(12, 5),
                AStartPathFind.Point(12, 4),
                AStartPathFind.Point(12, 3)
        )
        //包围住起始点的障碍
        4 -> arrayOf(
                AStartPathFind.Point(3, 11),
                AStartPathFind.Point(3, 10),
                AStartPathFind.Point(3, 9),
                AStartPathFind.Point(4, 8),
                AStartPathFind.Point(5, 8),
                AStartPathFind.Point(6, 8),
                AStartPathFind.Point(7, 9),
                AStartPathFind.Point(7, 10),
                AStartPathFind.Point(7, 11),
                AStartPathFind.Point(6, 12),
                AStartPathFind.Point(5, 12),
                AStartPathFind.Point(4, 12)
        )
        //默认没有障碍
        else -> emptyArray()
    }
}

