package com.zuoyueer.dao;

import com.github.pagehelper.Page;
import com.zuoyueer.pojo.CheckItem;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/4
 * Time: 21:51
 * @projectName health_parent
 * @description: 检查项的持久层接口
 */
public interface CheckItemDao {
    /**
     * 增加
     */
    public void add(CheckItem checkItem);

    /**
     * 多条件查询(项目编码或者项目名称),不是模糊查询
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 根据id查询 该检查项是否被引用
     * 到中间表中查询就可以了...因为是多对多关系
     */
    Long findCountByCheckItemId(Integer id);

    /**
     * 根据id删除检查项
     */
    void deleteById(Integer id);

    /**
     * 根据id查询检查项数据
     */
    CheckItem findById(Integer id);

    /**
     * 修改检查项
     */
    void edit(CheckItem checkItem);

    /**
     * 查询全部
     */
    List<CheckItem> findAll();
}
