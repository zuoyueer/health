package com.zuoyueer.service;

import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.Role;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/18
 * Time: 15:24
 * @projectName health_parent
 * @description: 角色服务接口
 */
public interface RoleService {
    /**
     * 查询全部角色
     * @return
     */
    List<Role> findAll();

    void add(Role role, Integer[] menuIds, Integer[] permissionIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    Role findById(Integer id);

    List<Integer> findPermissionIdsByRoleId(Integer id);

    List<Integer> findMenuIdsByRoleId(Integer id);

    void edit(Role role, Integer[] menuIds, Integer[] permissionIds);

    void delete(Integer id);
}
