package com.zuoyueer.service;

import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.CheckGroup;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/6
 * Time: 13:23
 * @projectName health_parent
 * @description: 检查组服务接口
 */
public interface CheckGroupService {

    /**
     * 增加检查组
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 分页+条件 查询
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据id查询项目组
     */
    CheckGroup findById(Integer id);

    /**
     * 根据id查询中间表,查询检查组中的检查项id集合
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 修改检查组
     */
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 删除检查组
     */
    void delete(Integer id);

    /**
     * 查询全部
     */
    List<CheckGroup> findAll();
}
