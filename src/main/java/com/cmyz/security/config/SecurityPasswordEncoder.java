package com.cmyz.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码加密
 * 默认加密已经很好了
 * 创建这个是为了不加密
 * @author ：cmyz
 * @date ：2021/1/10 14:03
 */
public class SecurityPasswordEncoder implements PasswordEncoder {
    //加密方法
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString(); //不加密
    }

    //匹配方法
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}
