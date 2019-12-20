package com.zuoyueer.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.constant.DateUtils;
import com.zuoyueer.service.OrderSettingService;

/**
 * @author Zuoyueer
 * Date: 2019/12/18
 * Time: 23:11
 * @projectName health_parent
 * @description: 定义清理OrderSetting(预约信息)表中的数据
 */
public class ClearOrderSettingJob {

    @Reference
    private OrderSettingService orderSettingService;

    public void ClearOrderSettingBeforeDate(){
        orderSettingService.deleteBeforeDate(DateUtils.getToday());
    }
}
