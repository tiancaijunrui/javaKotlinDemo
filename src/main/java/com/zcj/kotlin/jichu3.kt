package com.zcj.kotlin

/**
 * @Since2017/7/25 ZhaCongJie@HF
 */
fun main(args: Array<String>) {

}

private  fun test1(){
    loop@ for(j in 1..100){
        print(j.toString() + "")
        if (j == 10) break@loop
    }
}
