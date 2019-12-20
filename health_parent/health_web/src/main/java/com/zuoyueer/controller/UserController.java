package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.entity.QueryPageBean;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.CheckGroup;
import com.zuoyueer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/16
 * Time: 11:12
 * @projectName health_parent
 * @description: 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 直接调用框架获取用户名
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }




    /**
     * 增加检查组
     *
     * @param user   检查组信息
     * @param roleIds 检查组关联的检查项的id集合
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody com.zuoyueer.pojo.User user, Integer[] roleIds) {
        try {
            //对密码进行加密
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            //添加
            userService.add(user, roleIds);
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
        return userService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    /**
     * 根据id查询项目组
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        com.zuoyueer.pojo.User user = userService.findById(id);
        if (user != null) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, user);
        }
        return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
    }

    /**
     * 查询检查组中 全部检查项的id集合
     * @param id 检查组的id
     */
    @RequestMapping("/findRoleIdsByUserId")
    public List<Integer> findRoleIdsByUserId(Integer id) {
        return userService.findRoleIdsByUserId(id);
    }

    /**
     * 修改检查组
     * @param user  检查组的数据
     * @param roleIds  需要修改的 检查项的id集合
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody com.zuoyueer.pojo.User user, Integer[] roleIds) {
        try {
            //密码加密
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.edit(user, roleIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 删除检查组
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            userService.delete(id);
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
        List<com.zuoyueer.pojo.User> userList = userService.findAll();
        if (userList != null && userList.size() > 0) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, userList);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

}
