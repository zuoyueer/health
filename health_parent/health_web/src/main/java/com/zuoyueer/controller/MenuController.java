package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.entity.QueryPageBean;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.CheckItem;
import com.zuoyueer.pojo.Menu;
import com.zuoyueer.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/17
 * Time: 9:51
 * @projectName health_parent
 * @description: 菜单控制层
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 动态菜单栏的显示
     * @return
     */
    @RequestMapping("/getMenuList")
    public String  getMenuList() {
        String result = null;
        //获取登录用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            //从redis中取
            result = jedisPool.getResource().get("menus_"+user.getUsername());
            //如果没有,就去数据库查
            if (result==null||"[]".equals(result)){
                //把查询的结果转换成json格式的字符串,存储到redis中
                ObjectMapper objectMapper = new ObjectMapper();
                result = objectMapper.writeValueAsString(menuService.getMenuList(user.getUsername()));
                jedisPool.getResource().set("menus_"+user.getUsername(),result);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 增加体检项
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Menu menu) {
        try {
            menuService.add(menu);
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
        PageResult pageResult = menuService.pageQuery(
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
            menuService.delete(id);
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
            Menu menu = menuService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 修改检查项
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody Menu menu) {
        try {
            menuService.edit(menu);
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
        List<Menu> menuList = menuService.findAll();
        if (menuList!=null&&menuList.size()>0){
            return  new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,menuList);
        }
        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }

}
