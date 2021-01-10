package com.cmyz.security.config;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.cmyz.security.pojo.Result;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 由于SpringSecurity的登录失败是以抛出异常的形式
 * 所以需要收到获取异常判断状态
 * @author ：cmyz
 * @date ：2021/1/10 14:53
 */
@Component
public class SecurityAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException exception) throws IOException, ServletException {
        Result<Object> result = new Result<>();
        result.setCode(401);
        if (exception instanceof UsernameNotFoundException) {
            result.setMsg("用户不存在!");
        } else if (exception instanceof BadCredentialsException) {
            result.setMsg("用户名或密码错误！");
        } else if (exception instanceof LockedException) {
            result.setMsg("用户已被锁定！");
        } else if (exception instanceof DisabledException) {
            result.setMsg("用户不可用！");
        } else if (exception instanceof AccountExpiredException) {
            result.setMsg("账户已过期！");
        } else if (exception instanceof CredentialsExpiredException) {
            result.setMsg("用户密码已过期！");
        }else {
            result.setMsg("认证失败，请联系网站管理员！");
        }
        //写入JSON
        response.setStatus(HttpStatus.HTTP_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }

}
