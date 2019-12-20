package com.zuoyueer.service;

import com.zuoyueer.entity.Result;

import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/12
 * Time: 23:27
 * @projectName health_parent
 * @description: 预约接口
 */
public interface OrderService {
    /**
     * 体检预约
     * @param map 用户预约数据
     * @return
     */
    Result submitOrder(Map map) throws Exception;

    /**
     * 根据id查询用户预约的详细信息,需要关联3张表:套餐表,会员表,预约表
     * @return 返回信息封装成map
     */
    Map findByDetailById(Integer id) throws Exception;
}
