package com.zcj.service

import com.zcj.domain.User
import javax.annotation.Resource

/**
 * @Since2017/6/28 ZhaCongJie@HF
 */
class UserServiceImpl : UserService {
    @Resource
    protected var baseService: HibernateDao? = null
    override fun save(user: User): String {
        return baseService!!.save(user)
    }
}
