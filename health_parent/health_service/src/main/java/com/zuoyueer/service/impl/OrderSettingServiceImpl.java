package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zuoyueer.dao.OrderSettingDao;
import com.zuoyueer.pojo.OrderSetting;
import com.zuoyueer.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Zuoyueer
 * Date: 2019/12/8
 * Time: 9:01
 * @projectName health_parent
 * @description: 订单管理服务层实现类
 */

@Transactional
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量增加
     *
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                //调用添加单个的方法
                editNumberByDate(orderSetting);
            }
        }
    }


    /**
     * 查询预约信息,根据年月
     *
     * @param date 2019-12
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //输入的数据只有月份,我们需要查询当月的全部数据,先把日期范围设置好
        String dateBegin = date + "-1";
        String dateEnd = date + "-31";
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        //查询当月的全部数据,根据当月的第一天和最后一天
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(map);
        //把查询到的list数据封装成客户端需要的格式, List集合中的Map中存储一天的数据
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            //把数据封装到Map中, 之所以用Map是因为,我们没有与之对应的JavaBean, JSon中的Map和JavaBean传输一样的
            HashMap<String, Object> stringObjectMap = new HashMap<>(3);
            stringObjectMap.put("date", orderSetting.getOrderDate().getDate());
            stringObjectMap.put("number", orderSetting.getNumber());
            stringObjectMap.put("reservations", orderSetting.getReservations());
            //把每条数据又添加到List集合中
            data.add(stringObjectMap);
        }
        return data;
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
     * 查询已经预约的人数, 查询的前提是数据库中 必须要有该条数据
     * 否则返回值是null,而我们需要的是int ,就会出现类型异常
     */
    public int getReservations(Date date) {
        //如果该数据已经存在,返回已预约数
        return orderSettingDao.getReservations(date);
    }

    /**
     * 删除 时间之前的预约信息
     *
     * @param date
     */
    @Override
    public void deleteBeforeDate(Date date) {
        orderSettingDao.deleteBeforeDate(date);
    }
}
