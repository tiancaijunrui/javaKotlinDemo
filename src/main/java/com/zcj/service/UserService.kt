package com.zcj.service

import com.zcj.domain.User
import org.springframework.stereotype.Service

/**
 * @Since2017/6/28 ZhaCongJie@HF
 */
@Service
interface UserService {
    fun save(user: User): String
}
