package com.springsecurity.security;


import com.springsecurity.entity.Permission;
import com.springsecurity.entity.User;
import com.springsecurity.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//设置动态用户信息
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        /*
        * 1.根据用户的名称查询数据用户信息
        * 2.底层会根据数据库查询用户信息，判断密码是否正确
        * */
        User user=userMapper.findByUsername(s);
        System.out.println(user.getUsername()+"  "+user.getPassword()+user.getRealname());

        //3.给用户设置权限
        List<Permission> listPerminssion= userMapper.findPermissionByUsername(s);
        System.out.println("用户名："+s+"权限："+listPerminssion.toString());
        if (listPerminssion!=null&&listPerminssion.size()>0){
            //定义用户权限
            List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
            for (Permission permission:listPerminssion){
                authorities.add(new SimpleGrantedAuthority(permission.getPermTag()));
            }
            user.setAuthorities(authorities);
        }

        return user;
    }
}
