package com.zuoyueer.dao;

import com.github.pagehelper.Page;
import com.zuoyueer.pojo.Permission;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/15
 * Time: 18:19
 * @projectName health_parent
 * @description: 权限管理持久层
 */
public interface PermissionDao {
    Permission findPermissionByRoleId(Integer id);

    //_______________________下面是CURD__________________________________________

    void add(Permission permission);

    Page<Permission> selectByCondition(String queryString);

    Long findCountByPermissionId(Integer id);

    void deleteById(Integer id);

    Permission findById(Integer id);

    void edit(Permission permission);

    List<Permission> findAll();
}
