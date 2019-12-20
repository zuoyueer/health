package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.entity.QueryPageBean;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.Role;
import com.zuoyueer.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/18
 * Time: 15:22
 * @projectName health_parent
 * @description: 角色控制层
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;


    /**
     * 添加角色
     * @param role
     * @param menuIds 菜单id集合
     * @param permissionIds 权限id集合
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer [] menuIds , Integer [] permissionIds) {
        try {

            //添加
            roleService.add(role, menuIds,permissionIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页查询,还有条件查询
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return roleService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    /**
     * 根据id查询角色
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Role role = roleService.findById(id);
        if (role != null) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, role);
        }
        return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
    }

    /**
     * 查询权限id集合
     * @param id
     * @return
     */
    @RequestMapping("/findPermissionIdsByRoleId")
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        return roleService.findPermissionIdsByRoleId(id);
    }


    /**
     * 查询菜单id集合
     * @param id
     * @return
     */
    @RequestMapping("/findMenuIdsByRoleId")
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        return roleService.findMenuIdsByRoleId(id);
    }


    /**
     * 修改角色
     * @param role
     * @param menuIds
     * @param permissionIds
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role, Integer [] menuIds , Integer [] permissionIds) {
        try {
            roleService.edit(role, menuIds,permissionIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            roleService.delete(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }



    /**
     * 查询全部
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        List<Role> roleList = roleService.findAll();
        if (roleList != null && roleList.size() > 0) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, roleList);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }


}
