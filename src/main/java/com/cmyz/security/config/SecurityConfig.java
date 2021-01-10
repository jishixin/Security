package com.cmyz.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * Spring Security 重写适配器
 * @author ：cmyz
 * @date ：2021/1/10 11:48
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true) //开启注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private SecurityAuthenticationFailureHandler securityAuthenticationFailureHandler;

    @Resource
    private SecurityAccessDeniedHandler securityAccessDeniedHandler;

    //注入密码加密器
    @Bean
    public PasswordEncoder passwordEncoder(){
        // return new BCryptPasswordEncoder(); 默认加密器
        return new SecurityPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //排除资源
        web.ignoring().antMatchers("/druid/**","/user/**","/favicon.ico","/css/**","/img/**","/js/**","/login.html");
    }

    //一旦重写了此类,SpringSecurity的默认流程将失效
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // csrf 安全访问协议,默认开启,前后端分离不知道能不能开
        http.csrf().disable();
        //form表单形式提交
        http.formLogin()
                //自定义登录的用户名和密码名称
                .usernameParameter("username")
                .passwordParameter("password")
                //登入页面,在Controller重写get方法即可
                .loginPage("/login")
                //登录逻辑验证 post方法
                .loginProcessingUrl("/login")
                //登入成功逻辑 新增get方法 如果要重定向,调用重载方法,在后加true
                .defaultSuccessUrl("/success")
                // 自定义登录失败处理
                .failureHandler(securityAuthenticationFailureHandler);
        // 自定义权限不足处理
        http.exceptionHandling().accessDeniedHandler(securityAccessDeniedHandler);
        //请求路径验证(配合权限验证)
        http.authorizeRequests().antMatchers("/login").permitAll().anyRequest().authenticated();
    }
}
