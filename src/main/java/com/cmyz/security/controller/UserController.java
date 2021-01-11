package com.cmyz.security.controller;

import cn.hutool.core.util.StrUtil;
import com.cmyz.security.pojo.Result;
import com.cmyz.security.pojo.User;
import com.cmyz.security.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ：cmyz
 * @date ：2021/1/10 12:02
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/add")
//    @Secured("ROLE_admin")
    @PreAuthorize("hasAuthority('/user/add')")
    public Result<Object> addUser(User user){
        if (!StrUtil.isBlank(user.getUsername())&&!StrUtil.isBlank(user.getPassword())){
            boolean save = userService.save(user);
            if (save){
                return new Result<>(200,"用户新增成功!");
            }else {
                return new Result<>(500,"用户新增失败!");
            }
        }else{
            return new Result<>(500,"用户账号或者密码不能为空!");
        }
    }

    @GetMapping("/{id}")
    public Result<Object> getUser(@PathVariable Integer id){
        if (id!=null&&id>0){
            User user = userService.getById(id);
            if (user!=null){
                return new Result<>(200,"success",user);
            }else{
                return new Result<>(500,"获取失败!");
            }
        }else{
            return new Result<>(500,"输入有误!");
        }
    }
}
