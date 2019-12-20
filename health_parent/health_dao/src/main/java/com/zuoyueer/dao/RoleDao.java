package com.zuoyueer.dao;

import com.github.pagehelper.Page;
import com.zuoyueer.pojo.Role;

import java.util.HashMap;
import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/15
 * Time: 18:19
 * @projectName health_parent
 * @description: 角色管理持久层
 */
public interface RoleDao {

    Role findRoleByUserId(Integer id);

    //__________________________下面是CURD_______________________________________________

    /**
     * 查询全部角色
     * @return
     */
    List<Role> findAll();

    Page<Role> selectByCondition(String queryString);

    Role findById(Integer id);

    void add(Role role);

    void setRoleAndMenu(HashMap<String, Integer> map);

    void setRoleAndPermission(HashMap<String, Integer> map);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    List<Integer> findMenuIdsByRoleId(Integer id);

    void deleteAssociationOfPermission(Integer id);

    void deleteAssociationOfMenu(Integer id);

    void edit(Role role);

    void delete(Integer id);
}
