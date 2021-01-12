# Spring Security
> cmyz学习SpringSecurity<br/>
> 框架:Springboot<br>
> 具体代码可参考demo

## maven依赖
```xml
<dependencys>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    // Security 测试类
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencys>
```

## application配置文件
> 默认没有配置即可生效
```yaml
spring:
  security:
    user:
      # 没有重写登录前可设置默认的用户
      name: admin
      password: admin
      roles: super
```

## 开启权限注解
在SpringBoot启动类或者配置类上添加`@EnableGlobalMethodSecurity(securedEnabled = true)`

## 排除不受Security控制的资源
```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        //排除资源
        web.ignoring().antMatchers("/druid/**","/favicon.ico","/css/**","/img/**","/js/**","/login.html");
    }
}
```

## 修改默认Security整体流程
```java
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true) //开启注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //一旦重写了此类,SpringSecurity的默认流程将失效
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // csrf 安全访问协议,默认开启,前后端分离不知道能不能开
        http.csrf().disable();
        // 开启跨域,默认关闭
        http.cors();
        //form表单形式提交
        http.formLogin()
                //自定义登录的用户名和密码名称
                .usernameParameter("username")
                .passwordParameter("password")
                //登入页面,在Controller重写get方法即可
                .loginPage("/login")
                //登录逻辑验证 post方法
                .loginProcessingUrl("/login")
                .successHandler(securityAuthenticationSuccessHandler)
                //登入成功逻辑 新增get方法 如果要重定向,调用重载方法,在后加true
                //.defaultSuccessUrl("/success",true)
                // 自定义登录失败处理
                .failureHandler(securityAuthenticationFailureHandler);
        //登出
        http.logout().logoutSuccessHandler(securityLogoutSuccessHandler);
        // 自定义权限不足处理
        http.exceptionHandling().accessDeniedHandler(securityAccessDeniedHandler);
        //关闭预检请求(跨域options请求)
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll();
        //请求路径验证(配合权限验证)
        http.authorizeRequests().antMatchers("/login").permitAll()
                .antMatchers("/user/add").hasRole("admin")
                .anyRequest().authenticated();
    }
}
```

## 开启跨域
SpringWebMvc配置：
```java
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
```
SpringSecurity配置：
```java
//開啓跨域
http.cors();
http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
```
跨域请求配置：
开启携带跨域Cookie功能即可
列如：ajax请求
```javascript
xhrFields: {
    withCredentials: true,
}
```

## 重写登录逻辑
```java
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
```

## 密码加密
重写`PasswordEncoder`接口
```java
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
```
注入`bean`
```java
//注入密码加密器
@Bean
public PasswordEncoder passwordEncoder(){
    // return new BCryptPasswordEncoder(); 默认加密器
    return new SecurityPasswordEncoder();
}
```

## 登陆失败自定义处理
```java
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
```

## 权限不足处理器
```java
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
```

## 登录成功处理器
```java
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
```

## 登出成功处理器
```java
/**
 * 登出成功处理器
 * @author ：cmyz
 * @date ：2021/1/11 20:52
 */
@Component
public class SecurityLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        Result<Object> result = new Result<>(200, "登出成功");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
```

## 权限注解
> 默认未开启注解模式<br/>
> 开启注解:`@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)`

使用:
* `@Secured("ROLE_admin")`角色匹配,角色前需要加`ROLE_`
* `@PreAuthorize("hasAuthority('/user/add')")`匹配权限和角色 但需要使用其access内置的表达方法
