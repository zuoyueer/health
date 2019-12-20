package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zuoyueer.dao.RoleDao;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.Role;
import com.zuoyueer.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/18
 * Time: 15:25
 * @projectName health_parent
 * @description: 角色管理的服务实现类
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;


    @Override
    public void add(Role role, Integer[] menuIds, Integer[] permissionIds) {
        roleDao.add(role);
        setRoleAndMenu(role.getId(), menuIds);
        setRoleAndPermission(role.getId(), permissionIds);
    }

    /**
     * 添加角色菜单中间表的对应关系
     */
    private void setRoleAndMenu(Integer roleId, Integer[] menuIds) {
        if (menuIds != null && menuIds.length > 0) {
            for (Integer meunId : menuIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("role_id", roleId);
                map.put("menu_id", meunId);
                roleDao.setRoleAndMenu(map);
            }
        }
    }

    /**
     * 添加角色权限中间表的对应关系
     */
    private void setRoleAndPermission(Integer roleId, Integer[] permissionIds) {
        if (permissionIds != null && permissionIds.length > 0) {
            for (Integer permissionId : permissionIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("role_id", roleId);
                map.put("permission_id", permissionId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Role> page = roleDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        return roleDao.findPermissionIdsByRoleId(id);
    }

    @Override
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        return roleDao.findMenuIdsByRoleId(id);
    }

    @Override
    public void edit(Role role, Integer[] menuIds, Integer[] permissionIds) {
        //删除角色权限中间表中 原来的关联关系
        roleDao.deleteAssociationOfPermission(role.getId());
        //删除角色菜单中间表中 原来的关联关系
        roleDao.deleteAssociationOfMenu(role.getId());
        //增加角色权限中间表中  修改的关联关系
        setRoleAndPermission(role.getId(), permissionIds);
        //增加角色菜单中间表中  修改的关联关系
        setRoleAndMenu(role.getId(), menuIds);
        //更新用户信息
        roleDao.edit(role);
    }

    @Override
    public void delete(Integer id) {
        //删除角色权限中间表中 原来的关联关系
        roleDao.deleteAssociationOfPermission(id);
        //删除角色菜单中间表中 原来的关联关系
        roleDao.deleteAssociationOfMenu(id);
        //删除角色
        roleDao.delete(id);
    }

    /**
     * 查询全部角色
     *
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }
}
