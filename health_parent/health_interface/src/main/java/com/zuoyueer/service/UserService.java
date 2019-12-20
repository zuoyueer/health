package com.zuoyueer.service;

import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.User;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/15
 * Time: 18:06
 * @projectName health_parent
 * @description: 用户管理接口
 */
public interface UserService {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User findUserByUsername(String username);

    //----------------------下面是CURD---------------------------------------

    void add(User user, Integer[] roleIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void edit(User user, Integer[] roleIds);

    void delete(Integer id);

    List<User> findAll();
}
