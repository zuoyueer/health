package com.zuoyueer.service.impl2;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.dubbo.config.annotation.Service;
import com.zuoyueer.dao.OrderSettingDao;
import com.zuoyueer.pojo.OrderSetting;
import com.zuoyueer.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Zuoyueer
 * Date: 2019/12/10
 * Time: 21:14
 * @projectName health_parent
 * @description: 第二遍
 */
//@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceimpl2 implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量增加
     * ----------------------
     * 先判断之前有没有,如果没有就添加,有就修改
     *
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                editNumberByDate(orderSetting);
            }
        }
    }

    /**
     * 查询预约信息,根据年月
     *
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String beginDate = date + "-1";
        String endDate = date + "-31";
        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("beginDate", beginDate);
        stringStringHashMap.put("endDate", endDate);
        List<OrderSetting> orderSettingByMonth = orderSettingDao.getOrderSettingByMonth(stringStringHashMap);

        List<Map> list = new ArrayList<>();
        if (orderSettingByMonth != null && orderSettingByMonth.size() > 0) {
            for (OrderSetting orderSetting : orderSettingByMonth) {
                HashMap<Object, Object> stringObjectMap = new HashMap<>();
                stringObjectMap.put("date", orderSetting.getOrderDate().getDate());
                stringObjectMap.put("number", orderSetting.getNumber());
                stringObjectMap.put("reservations", orderSetting.getReservations());
                list.add(stringObjectMap);
            }
        }
        return list;
    }

    /**
     * 修改预约人数
     *
     * @param orderSetting
     */
    @Override
    public Boolean editNumberByDate(OrderSetting orderSetting) {
        //检查此数据(是否)存在
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            //已结存在
            if (orderSetting.getNumber() > getReservations(orderSetting.getOrderDate())) {
                //设置的可预约数大于 已预约数 才能进行更新
                orderSettingDao.editNumberByOrderDate(orderSetting);
            }else {
                return false;
            }
        } else {
            //不存在,执行添加操作
            orderSettingDao.add(orderSetting);
        }
        return true;
    }

    /**
     * 查询已经预约的人数
     *
     * @param date
     */
    @Override
    public int getReservations(Date date) {
        return orderSettingDao.getReservations(date);
    }

    /**
     * 删除时间之前的预约信息
     *
     * @param date
     */
    @Override
    public void deleteBeforeDate(Date date) {

    }
}
