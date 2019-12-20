package com.zuoyueer.dao;

import com.zuoyueer.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/13
 * Time: 10:00
 * @projectName health_parent
 * @description: 预约持久层
 */
public interface OrderDao {

    /**
     * 动态条件查询  体检预约信息t_order表
     *
     * @param order
     * @return
     */
    List<Order> findByCondition(Order order);

    /**
     * 添加  会员的预约信息
     *
     * @param order
     */
    void add(Order order);

    /**
     * 根据id查询用户预约的详细信息,需要关联3张表:套餐表,会员表,预约表
     *
     * @param id 这个id是t_order表中的id,是在添加预约的时候返回的主键id
     * @return
     */
    Map findDetailById(Integer id);

    /**
     * 根据日期统计预约数
     *
     * @param date 2019-12-15
     * @return 当天的预约总人数
     */
    Integer findOrderCountByDate(String date);

    /**
     * 根据日期统计预约数，统计指定日期之后的预约数
     *
     * @param date 2019-12-15
     * @return
     */
    Integer findOrderCountAfterDate(String date);

    /**
     * 根据日期统计到诊数
     *
     * @param date 2019-21-15
     * @return
     */
    Integer findVisitsCountByDate(String date);

    /**
     * 根据日期统计到诊数，统计指定日期之后的到诊数
     *
     * @param date 2019-21-15
     * @return
     */
    Integer findVisitsCountAfterDate(String date);

    /**
     * 统计预约最多的5个套餐,
     *
     * @return 返回套餐名称, 套餐预约数, 该套餐预约占比, 封装成map
     */
    List<Map> findHotSetmeal();


}
