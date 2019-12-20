package com.zuoyueer.service;

import com.zuoyueer.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/7
 * Time: 23:27
 * @projectName health_parent
 * @description: 订单管理服务层接口
 */
public interface OrderSettingService {

    /**
     * 增加
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList);

    /**
     * 查询预约信息,根据年月
     * @param date
     * @return
     */
    List<Map> getOrderSettingByMonth(String date);

    /**
     * 修改预约人数, 修改成功返回true ,否则false
     */
    Boolean editNumberByDate(OrderSetting orderSetting);


    /**
     * 查询已经预约的人数
     */
    int getReservations(Date date);

    /**
     * 删除时间之前的预约信息
     */
    void deleteBeforeDate(Date date);
}
