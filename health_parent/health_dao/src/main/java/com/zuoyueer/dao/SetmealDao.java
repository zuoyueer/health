package com.zuoyueer.dao;

import com.github.pagehelper.Page;
import com.zuoyueer.pojo.Setmeal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/7
 * Time: 0:02
 * @projectName health_parent
 * @description: 套餐持久层接口
 */
public interface SetmealDao {
    /**
     * 添加套餐
     */
    void add(Setmeal setmeal);

    /**
     * 绑定套餐和检查组的多对多关系
     * @param map  套餐id和检查组id
     */
    void setSetmealAndCheckGroup(HashMap<String, Integer> map);

    /**
     * 分页的条件查询
     */
    Page<Setmeal> findPage(String queryString);

    /**
     * 删除套餐和检查组的多对多关系
     */
    void deleteAssociation(Integer id);

    /**
     * 删除套餐记录
     */
    void delete(Integer id);

    /**
     * 查询套餐
     */
    Setmeal findById(Integer id);

    /**
     * 查询套餐绑定的项目组的集合
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    /**
     * 修改套餐数据
     */
    void edit(Setmeal setmeal);

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
