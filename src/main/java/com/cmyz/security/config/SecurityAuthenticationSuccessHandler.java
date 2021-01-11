package com.cmyz.security.config;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cmyz.security.pojo.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理器
 * @author ：cmyz
 * @date ：2021/1/11 20:44
 */
@Component
public class SecurityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        Result<Object> result = new Result<>(200, "登录成功");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
