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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Zuoyueer
 * Date: 2019/12/16
 * Time: 9:52
 * @projectName health_parent
 * @description: 安全框架校验的实现类
 */
public class UserSecurityService implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                Set<Permission> permissions = role.getPermissions();
                //授予角色
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getKeyword()));
                if (permissions!=null&&permissions.size()>0){
                    for (Permission permission : permissions) {
                        //授予权限
                        grantedAuthorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),grantedAuthorities);
    }
}
