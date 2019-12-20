package com.zuoyueer.security;

import com.zuoyueer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/14
 * Time: 11:15
 * @projectName health_parent
 * @description: 自定义授权类
 */
public class MySpringSecurityService implements UserDetailsService {

    //注入依赖
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * @param username 客户端传递来的用户名
     * @return UserDetails的实现类org.springframework.security.core.userdetails.User
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. 根据用户查询查询数据库,获得User对象
        User dbUser = findUserFromDbByUsername(username);

        //用户对象判断,如果是空, 返回空
        if (dbUser == null) {
            return null;
        }

        //如果不是空,把用户名,数据库查询出来的密码和访问权限, 创建UserDetails对象返回

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //给该用户设置权限,这里写死了,实际上是从数据库中查询出来的
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
       // grantedAuthorities.add(new SimpleGrantedAuthority("add"));
        //1.页面发请求，经过框架的登录请求，获取到用户输入的密码
        //2.安全框架调用loadUserByUsername 获取数据库的中的密码
        //3.安全框架 将 用户输入的密码和 数据库的密码进行验证（框架内部实现的）
        //3个参数,一个是用户名,二是数据库密码,三是该用户的权限
        //return new org.springframework.security.core.userdetails.User(username, "${noop}"+dbUser.getPassword(), grantedAuthorities);
        return new org.springframework.security.core.userdetails.User(username, dbUser.getPassword(), grantedAuthorities);
    }

    //为了简化和本文不相关的代码,我们模拟从数据库查询用户信息
    private User findUserFromDbByUsername(String username) {
        if ("admin".equals(username)) {
            //pojo的User类
            User user = new User();
            user.setUsername(username);
            //user.setPassword("123456");
            user.setPassword(encoder.encode("123456"));
            return user;
        }
        //查询不到返回空
        return null;
    }
}
