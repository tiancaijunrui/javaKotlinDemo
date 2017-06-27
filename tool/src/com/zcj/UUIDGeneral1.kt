package com.zcj

import java.util.UUID

/**
 * @Since2017/6/27 ZhaCongJie@HF
 */

fun main(args: Array<String>) {
    for (i in 1..123){
        println(UUID.randomUUID().toString().replace("-", ""));
    }
}
