package com.zuoyueer.controller2;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.OrderSetting;
import com.zuoyueer.service.OrderSettingService;
import com.zuoyueer.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/10
 * Time: 20:48
 * @projectName health_parent
 * @description: 第二遍
 */
@RestController
@RequestMapping("/order")
public class OrderSettingController2 {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * Excel文件上传,并解析文件内存存储到数据库
     */
    @RequestMapping("/uplodad")
    public Result upload(@RequestParam("excelFile") MultipartFile multipartFile) {
        try {
            //获取行的集合
            List<String[]> list = POIUtils.readExcel(multipartFile);
            //把List<String[]> 转换成List<OrderSetting>
            if (list.size() > 0) {
                List<OrderSetting> orderSettingList = new ArrayList<>();
                //遍历行
                for (String[] strings : list) {
                    //把每一行的数据封装到javaBean中
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    //封装到List<OrderSetting>中
                    orderSettingList.add(orderSetting);
                }
                //调用服务层方法,存储到数据库中
                orderSettingService.add(orderSettingList);
            }
        } catch (Exception e) {
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }


    /**
     * 根据月份查询当月的全部数据
     * -----------------
     * 接受的参数是月份的字符串,
     * 直接调用服务层,在服务层,查询当月的全部数据
     * ----------------------------
     * 一种是使用模糊查询,日期包含当前月的
     * 另外一种是使用between来查询,手动设置当月的第一天和最后一天
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getALl(String date){

        List<Map> orderSettingByMonth = orderSettingService.getOrderSettingByMonth(date);
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, orderSettingByMonth);

    }

    /**
     * 根据日期修改可预约的人数
     * -------------------------
     * 接受的参数是某一天的日期,和 修改的人数
     * -------------------------------
     * 修改前需要判断,当前已经预约的人数是否小于修改的人数,否则不能修改
     */

    public void edit(OrderSetting orderSetting){
        if (orderSetting.getNumber()<orderSettingService.getReservations(orderSetting.getOrderDate())){
            return;
        }
        orderSettingService.editNumberByDate(orderSetting);
    }
}

