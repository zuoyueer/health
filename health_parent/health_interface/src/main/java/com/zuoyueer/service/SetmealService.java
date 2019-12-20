package com.zuoyueer.service;

import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/6
 * Time: 23:26
 * @projectName health_parent
 * @description: 套餐管理接口
 */
public interface SetmealService {
    /**
     * 添加套餐
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 分页查询+条件
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 删除套餐
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询套餐
     */
    Setmeal findById(Integer id);

    /**
     * 查询套餐绑定的项目组的id集合
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    /**
     * 编辑套餐
     */
    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 查询全部套餐
     */
    List<Setmeal> findAll();

    /**
     * 统计会员预约的各个套餐占比情况: 返回套餐名和该套餐被预约数
     *
     * @return 每个套餐数据封装到map中, 最终返回list集合
     */
    List<Map<String, Object>> findSetmealCount();
}
