package com.cmyz.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ：cmyz
 * @date ：2021/1/11 21:02
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //配置支持跨域的路径
                .allowedOrigins("*") //配置允许的源
                .allowedHeaders("*") //配置允许的自定义请求头, 用于 预检请求
                .allowCredentials(true) //配置是否允许发送Cookie, 用于 凭证请求
                .allowedMethods("*") //配置支持跨域请求的方法,如：GET、POST，一次性返回
                .exposedHeaders(HttpHeaders.SET_COOKIE) //配置响应的头信息,在其中可以设置其他的头信息
                .maxAge(3600); //配置预检请求的有效时间
    }
}
