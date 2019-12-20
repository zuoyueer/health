package com.zuoyueer.service;

import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.Menu;

import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/17
 * Time: 10:10
 * @projectName health_parent
 * @description: 菜单服务接口
 */
public interface MenuService {

    List<Map<String, Object>>getMenuList(String username);

    void add(Menu menu);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);

    Menu findById(Integer id);

    void edit(Menu menu);

    List<Menu> findAll();

}
