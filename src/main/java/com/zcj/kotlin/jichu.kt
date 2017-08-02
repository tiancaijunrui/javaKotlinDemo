package com.zcj.kotlin

import com.sun.xml.internal.fastinfoset.util.StringArray
import org.jetbrains.annotations.TestOnly

/**
 * @Since2017/7/19 ZhaCongJie@HF
 */
fun main(args: Array<String>) {
    val a: Int = 1_000_000
    print(a === a)
    val boxedA: Int? = a
    val antherBoxedA: Int? = a
    print(boxedA === antherBoxedA)
    print(dicimalDigitValue('5'))
    testArray();
    testString();
    testString1();
}

fun dicimalDigitValue(c: Char): Int {
    if (c !in '0'..'9') {
        throw IllegalAccessException("Out of range")
    }
    return c.toInt() - '0'.toInt()
}

fun testArray(): IntArray {
    val asc = Array(5, { i -> (i * i).toString() })
    return intArrayOf(1)
}

fun testString() {
    var text = """
    for(...){
}
"""
    print(text)
}

fun testString1(){
    var text = """
        | select * from t_els_cou...
        | .....
""".trimMargin()
    print(text)
}