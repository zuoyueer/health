package com.zuoyueer.dao;

import com.github.pagehelper.Page;
import com.zuoyueer.pojo.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/17
 * Time: 9:06
 * @projectName health_parent
 * @description: 菜单的持久层
 */
public interface MenuDao {

    /**
     * 根据角色id查询的一级菜单,需要根据优先级排序
     * @return
     */
    List<Map<String, Object>> findFirstMenu(List<Integer> ids);

    /**
     * 根据id查询菜单及其二级菜单
     */
    List<Map<String, Object>> findSecondMenu(Map map);


    List<Integer> findMenuIdsByRolesIds(ArrayList<Integer> roleIdList);

    List<Integer> findMenuIdsByUsername(String username);

    //  下面全部是CURD

    void add(Menu menu);

    Page<Menu> selectByCondition(String queryString);


    void deleteById(Integer id);

    Menu findById(Integer id);

    List<Menu> findAll();

    Long findCountByMenuId(Integer id);

    void edit(Menu menu);
}
