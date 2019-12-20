package com.zuoyueer.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zuoyueer
 * Date: 2019/12/15
 * Time: 15:10
 * @projectName health_parent
 * @description: 注解方式实现角色权限控制
 */
@Controller
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")
    public String addMethod(){
        System.out.println("拥有add角色权限的用户才能调用当前的方法...");
        return null;
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(){
        System.out.println("拥有delete角色权限的用户才能调用当前的方法...");
        return null;
    }

    /**
     * @PreAuthorize("ROLE_ADMIN")写是错误的,必须使用表达式
     * @return
     */
    @RequestMapping("/delete2")
    @PreAuthorize("ROLE_ADMIN")
    public String delete2(){
        System.out.println("拥有delete角色权限的用户才能调用当前的方法2222...");
        return null;
    }

}
