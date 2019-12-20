package com.zuoyueer.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.pojo.Permission;
import com.zuoyueer.pojo.Role;
import com.zuoyueer.pojo.User;
import com.zuoyueer.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Zuoyueer
 * Date: 2019/12/15
 * Time: 17:40
 * @projectName health_parent
 * @description: 按照SpringSecurity框架要求提供类，负责查询数据库获取用户信息
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. 调用服务,根据用户名查询用户
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        //2. 对用户进行授权

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //获得用户所属的角色集合
        Set<Role> roles = user.getRoles();
        //遍历角色集合
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getKeyword()));
            //获取角色对应的权限集合
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //添加权限,使用的是权限的关键字permission.getKeyword()
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        //创建UserDetails返回对象
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthorities);
    }
}
