package com.cmyz.security.config;

import cn.hutool.core.collection.CollUtil;
import com.cmyz.security.pojo.User;
import com.cmyz.security.service.UserService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户登录验证逻辑重写
 * 用户登录失败时抛出异常
 * @author ：cmyz
 * @date ：2021/1/10 13:29
 */
@Component
public class SecurityUserDetailsService implements UserDetailsService {

    @Resource
    private UserService userService;

    // 用户登录认证方法
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.err.println("登录验证:"+username);
        User user = userService.getUserByUsername(username);
        if (null == user){
            //用户不存在,抛出异常
            throw new UsernameNotFoundException("用户名错误!");
        }
        System.err.println("登录用户:"+user);
        //获取角色和权限
        List<String> roles = userService.getRolesById(user.getId());
        List<String> resources = userService.getResourcesById(user.getId());
        //在SpringSecurity中对角色的命名有严格的要求,要求角色名称的前缀必须是 'ROLE_',一般在此处拼接,不写入数据库
        //在SpringSecurity中角色和权限是平等的,都代表用户的访问权限
        List<String> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add("ROLE_"+role);
        }
        authorities.addAll(resources);
        System.err.println("用户权限为:"+authorities);
        String[] strings = new String[authorities.size()];
        authorities.toArray(strings);
        //返回值使用的是SpringSecurity提供的User对象,用户名,用户正确密码,权限集合
        //AuthorityUtils 可以通过字符串创建集合
        return new org.springframework.security.core.userdetails.User(username,user.getPassword()
                , AuthorityUtils.createAuthorityList(strings));
    }
}
