package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.entity.QueryPageBean;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.Permission;
import com.zuoyueer.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/18
 * Time: 20:04
 * @projectName health_parent
 * @description: 权限控制层
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    /**
     * 增加体检项
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Permission permission) {
        try {
            permissionService.add(permission);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = permissionService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    /**
     * 根据检查项id删除检查项
     */
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Result delete(Integer id) {
        try {
            permissionService.delete(id);
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 根据检查项id查询检查项数据
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result findById(Integer id) {
        try {
            Permission permission = permissionService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 修改检查项
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody Permission permission) {
        try {
            permissionService.edit(permission);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 查询全部
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        List<Permission> permissionList = permissionService.findAll();
        if (permissionList!=null&&permissionList.size()>0){
            return  new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,permissionList);
        }
        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }


}
