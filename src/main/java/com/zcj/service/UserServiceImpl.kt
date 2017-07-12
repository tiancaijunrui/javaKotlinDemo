package com.zcj.service

import com.zcj.domain.User
import org.springframework.stereotype.Service
import javax.annotation.Resource

/**
 * @Since2017/6/28 ZhaCongJie@HF
 */
@Service("userService")
class UserServiceImpl : UserService {
    @Resource
    protected var baseService: HibernateDao? = null
    override fun save(user: User): String {
        return baseService!!.save(user)
    }
}
