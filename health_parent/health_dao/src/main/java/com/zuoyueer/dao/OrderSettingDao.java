package com.zuoyueer.dao;

import com.zuoyueer.pojo.OrderSetting;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/8
 * Time: 9:05
 * @projectName health_parent
 * @description: 订单管理持久层接口
 */
public interface OrderSettingDao {
    /**
     * 根据日期查询,是否存在预约
     * @param orderDate
     * @return
     */
    Long findCountByOrderDate(Date orderDate);

    /**
     * 修改可预约人数
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 增加预约
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * @param map
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(HashMap<String, Object> map);


    /**
     * 根据日期查询当天已经预约的人数
     * @param date 2019-12-12
     * @return
     */
    int getReservations(Date date);

    /**
     * 查询当天的预约数据
     * @param date
     * @return
     */
    OrderSetting findByOrderDate(Date date);

    /**
     * 修改已预约人数
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);

    /**
     * 删除传入时间之前的预约数据
     * @param date  时间
     */
    void deleteBeforeDate(Date date);
}
