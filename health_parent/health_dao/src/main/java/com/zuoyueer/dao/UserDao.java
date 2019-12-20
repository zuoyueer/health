package com.zuoyueer.dao;

import com.github.pagehelper.Page;
import com.zuoyueer.pojo.CheckGroup;
import com.zuoyueer.pojo.User;

import java.util.HashMap;
import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/15
 * Time: 18:18
 * @projectName health_parent
 * @description: 用户持久层
 */
public interface UserDao {
    /**
     * 根据用户名查询用户
     * ------------------------
     * 不仅要查询用户信息,还包括角色集合,以及角色对应的权限集合
     * -----------------------------
     * 因为封装到一个对象中,所以需要使用多步查询,
     * 之所以不进行多表联合一次查询,是因为需要封装到User对象中,而不是map集合
     *
     * @param username 用户名
     * @return 用户对象
     */
    User findUserByUsername(String username);


    //___________________________下面是CURD_____________________________________

    void add(User user);

    void setUserAndRole(HashMap<String, Integer> map);

    Page<User> selectByCondition(String queryString);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void deleteAssociation(Integer id);

    void edit(User user);

    void delete(Integer id);

    List<User> findAll();
}
