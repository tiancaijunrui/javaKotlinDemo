package com.zcj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Since2017/6/26 ZhaCongJie@HF
 */
@Controller
public class UserController {
    @RequestMapping("uc/toLogin")
    public String toLogin(){
        return "hello";
    }
}
