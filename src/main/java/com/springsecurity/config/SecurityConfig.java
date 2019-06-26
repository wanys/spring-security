package com.springsecurity.config;

import com.springsecurity.entity.Permission;
import com.springsecurity.handler.AuthenticationFailureHandler;
import com.springsecurity.handler.AuthenticationSuccessHandler;
import com.springsecurity.mapper.PermissionMapper;
import com.springsecurity.mapper.UserMapper;
import com.springsecurity.security.MyUserDetailsService;
import com.springsecurity.until.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationFailureHandler failureHandler;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private PermissionMapper permissionMapper;
    //配置认证用户信息和权限
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       /* auth.inMemoryAuthentication().withUser("admin").password("123")
             .authorities("showOrder","addOrder","updateOrder","deleteOrder");
        auth.inMemoryAuthentication().withUser("useradd").password("123").authorities("showOrder");*/

       auth.userDetailsService(myUserDetailsService).passwordEncoder(new PasswordEncoder() {
           //加密的密码与数据库进行比对  charSequence是表单密码，s是数据库密码
           @Override
           public boolean matches(CharSequence charSequence, String s) {
               System.out.println("charSequence表单密码:"+charSequence+"  s数据库密码:"+s);
               return  MD5Util.encode((String)charSequence).equals(s);
           }


        //对表单密码进行加密-----不重要
        @Override
        public String encode(CharSequence charSequence) {
            System.out.println("charSquence表单加密:"+charSequence);
            return MD5Util.encode((String)charSequence);
        }
    });

    }


    //配置拦截请求资源
    protected void configure(HttpSecurity http) throws Exception {
        //如何实现权限控制  给每个请求路径分配一个权限名称 然后账号只要关联该名称，就可以有访问权限
    /* http.authorizeRequests()
             .antMatchers("/login").permitAll()
             .antMatchers("/showOrder").hasAnyAuthority("showOrder")
             .antMatchers("/addOrder").hasAnyAuthority("addOrder")
             .antMatchers("/updateOrder").hasAnyAuthority("updateOrder")
             .antMatchers("/deleteOrder").hasAnyAuthority("deleteOrder")
            .antMatchers("/**").fullyAuthenticated().and().formLogin().loginPage("/login")
             .successHandler(successHandler).failureHandler(failureHandler)
             .and().csrf().disable()   ;//拦截所有请求*/
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http.authorizeRequests();
        List<Permission> listPermission=permissionMapper.findAllPermission();
        for (Permission permission:listPermission){
            authorizeRequests.antMatchers(permission.getUrl()).hasAnyAuthority(permission.getPermTag());
        }
        authorizeRequests.antMatchers("/login").permitAll()
                .antMatchers("/**").fullyAuthenticated().and().formLogin().loginPage("/login")
                .failureHandler(failureHandler) .successHandler(successHandler)
                .and().csrf().disable();


    }

    //springboot2.0  升级为Security5.0（必须密码加密）以上密码支持多中加密方式，回复以前模式
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}
