package com.zuoyueer.service;

import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.Permission;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/18
 * Time: 20:18
 * @projectName health_parent
 * @description: 权限服务接口
 */
public interface PermissionService {
    void add(Permission permission);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);

    Permission findById(Integer id);

    void edit(Permission permission);

    List<Permission> findAll();

}
