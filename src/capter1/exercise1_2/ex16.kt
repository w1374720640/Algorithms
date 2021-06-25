package capter1.exercise1_2

import kotlin.math.abs

//使用欧几里得算法计算最大公约数
//因为需要支持所有理数，所以添加负数支持
//正常情况下只讨论自然数的最大公约数，这里最大公约数一直为正
//参考练习1.1.24
fun commonDivisor(a: Long, b: Long): Long {
    val absA = abs(a)
    val absB = abs(b)
    //两个整数的最大公约数等于其中较小的那个数和两数相除余数的最大公约数
    val large = if (absA > absB) absA else absB
    val small = if (absA > absB) absB else absA
    //余数
    val remainder = large % small
    if (remainder == 0L) return small
    return commonDivisor(small, remainder)
}


/**
 * 实现一个有理数
 */
class Rational {
    //分子（为防止溢出，用Long表示）
    val numerator: Long

    //分母（为防止溢出，用Long表示）
    val denominator: Long

    constructor(numerator: Int, denominator: Int) : this(numerator.toLong(), denominator.toLong())

    private constructor(numerator: Long, denominator: Long) {
        val commonDivisor = commonDivisor(numerator, denominator)
        if (commonDivisor == 1L) {
            this.numerator = numerator
            this.denominator = denominator
        } else {
            this.numerator = numerator / commonDivisor
            this.denominator = denominator / commonDivisor
        }
    }

    //加
    fun plus(that: Rational): Rational {
        val numerator = this.numerator * that.denominator + that.numerator * this.denominator
        val denominator = this.denominator * that.denominator
        return Rational(numerator, denominator)
    }

    //减
    fun minus(that: Rational): Rational {
        val numerator = this.numerator * that.denominator - that.numerator * this.denominator
        val denominator = this.denominator * that.denominator
        return Rational(numerator, denominator)
    }

    //乘
    fun times(that: Rational): Rational {
        val numerator = this.numerator * that.numerator
        val denominator = this.denominator * that.denominator
        return Rational(numerator, denominator)
    }

    //除
    fun divides(that: Rational): Rational {
        val numerator = this.numerator * that.denominator
        val denominator = this.denominator * that.numerator
        return Rational(numerator, denominator)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other === null) return false
        if (other !is Rational) return false
        return this.numerator == other.numerator && this.denominator == other.denominator
    }


    override fun toString(): String = "[numerator=${numerator} denominator=${denominator}]"

    private fun sameSign(a: Long, b: Long) =  (a >= 0L && b >= 0L) || (a <= 0L && b <= 0L)
}

fun main() {
    //常规测试
    println(Rational(84, 196))
    println(Rational(433, 4).plus(Rational(33, 5)))
    println(Rational(289, 8).minus(Rational(1, 8)))
    println(Rational(2, 3).times(Rational(3, 2)))
    println(Rational(2, 3).divides(Rational(2, 1)))
    println(Rational(100, 300) == Rational(1, 3))

    //负数测试
    println(Rational(-84, -196))
    println(Rational(-2, 3).times(Rational(3, 2)))

    //溢出测试
    val a = Rational(Int.MAX_VALUE, Int.MAX_VALUE - 1)
    println(a)
    println(a.times(a).times(a))

    //TODO 1.2.17 使用断言来防止溢出
}