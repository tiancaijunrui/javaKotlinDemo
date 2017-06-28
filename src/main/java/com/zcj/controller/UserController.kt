package com.zcj.controller

import com.zcj.domain.User
import com.zcj.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import sun.security.provider.MD5

import javax.annotation.Resource
import java.util.UUID

/**
 * @Since2017/6/26 ZhaCongJie@HF
 */
@Controller
class UserController {
    @Resource
    private val userService: UserService? = null

    @ResponseBody
    @RequestMapping("uc/toLogin")
    fun toLogin(): String {
        val user = User()
        user.id = UUID.randomUUID().toString().replace("-".toRegex(), "")
        user.loginName = "tiancai"
        user.passWord = "2222"
        userService!!.save(user)
        return "hello"
    }

}
