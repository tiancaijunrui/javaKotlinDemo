package com.zcj.service

import com.zcj.domain.User

/**
 * @Since2017/6/28 ZhaCongJie@HF
 */
interface UserService {
    fun save(user: User): String
}
