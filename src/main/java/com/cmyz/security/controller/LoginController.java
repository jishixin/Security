package com.cmyz.security.controller;

import com.cmyz.security.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录相关逻辑接口
 * @author ：cmyz
 * @date ：2021/1/10 13:18
 */
@RestController
public class LoginController {

    @GetMapping("/login")
    public Result<Object> login(){
        return new Result<>(401,"请登录!");
    }

}
