package com.zuoyueer.dao;

import com.github.pagehelper.Page;
import com.zuoyueer.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/6
 * Time: 13:36
 * @projectName health_parent
 * @description: 持久层接口
 */
public interface CheckGroupDao {
    /**
     * 添加检查组
     */
    void add(CheckGroup checkGroup);

    /**
     * 增加检查组和检查项的关系记录,在中间表中增加
     * @param map  两个参数封装成了一个map集合
     */
    void setCheckGroupAndCheckItem(HashMap<String, Integer> map);

    /**
     * 条件查询
     */
    Page<CheckGroup> selectByCondition(@Param(value = "xx") String queryString);

    /**
     * 根据id查询
     */
    CheckGroup findById(Integer id);

    /**
     * 查询id对应检查组中的检查项的id集合
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 修改检查组
     */
    void edit(CheckGroup checkGroup);

    /**
     * 根据检查组的id 删除中间表的数据
     * @param id
     */
    void deleteAssociation(Integer id);

    /**
     * 删除检查组数据
     */
    void delete(Integer id);

    /**
     * 查询全部
     */
    List<CheckGroup> findAll();
}
