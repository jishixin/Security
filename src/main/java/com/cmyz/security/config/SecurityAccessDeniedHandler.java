package com.cmyz.security.config;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.cmyz.security.pojo.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 403权限不足处理器
 * @author ：cmyz
 * @date ：2021/1/10 18:54
 */
@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response
            , AccessDeniedException e) throws IOException, ServletException {
        Result<Object> result = new Result<>(401,"权限不足!");
        //写入JSON
        response.setStatus(HttpStatus.HTTP_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
