package com.zcj.kotlin

import org.apache.poi.ss.formula.functions.T
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import javax.xml.bind.DatatypeConverter.parseString

/**
 * @Since2017/7/20 ZhaCongJie@HF
 */
fun main(args: Array<String>) {
//    test1()
//    test2()
//    print(test3(6, 2))
//    print(test4("232323"))
    testFor()
}

private fun test1() {
    val i = 10;
    val s = "i = $i"
    print(s)
}

private fun test2() {
    val price = """ ${'$'}9.99"""
    print(price)
}

private fun test3(num1: Int, num2: Int): Int {
    return if (num1 > num2) num1 else num2
}

private fun test4(str: String): String {
    when (str) {
        "1" -> return "1"
        "2" -> return "2"
        else -> {
            return "else"
        }
    }
}
private fun testWhen(str:Any){
    when(str){
        0,1 -> println("0,1")
        else -> println("else")
    }
    when(str){
        parseInt(str.toString()) -> println(str)
        in 1..10 -> println(111)
        !in 10 ..20 -> println(222)
        is String -> str.startsWith("11")
        else -> println("else1")
    }
}
fun testFor(){
    val arr = ArrayList<Int>()
    arr.add("1".toInt())
    arr.add(2)
    arr.add(3)
    for(a in arr){
        print(a.toString() + "")
    }
    for (i in arr.indices){
        println(arr[i])
    }
    for ((index,value) in arr.withIndex()){
        println("$index is $value")
    }
}
