package com.zuoyueer.service;

import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.CheckItem;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/4
 * Time: 21:44
 * @projectName health_parent
 * @description: 检查项服务接口
 */
public interface CheckItemService {
    /**
     * 增加
     * @param checkItem
     */
    public void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param currentPage 当前页
     * @param pageSize  每页大小
     * @param queryString  查询条件(项目编码或者项目名称)
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据检查项id删除
     */
    void delete(Integer id);

    /**
     * 修改检查项
     */
    void edit(CheckItem checkItem);

    /**
     * 根据id查询检查项
     */
    CheckItem findById(Integer id);

    /**
     * 查询全部
     */
    List<CheckItem> findAll();
}
