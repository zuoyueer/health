package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.constant.RedisMessageConstant;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.Order;
import com.zuoyueer.service.OrderService;
import com.zuoyueer.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/12
 * Time: 22:55
 * @projectName health_parent
 * @description: 体检预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 使用Map接受用户提交的json数据,这是spring帮我们封装的
     *
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        //获取请求数据中的手机号
        String telephone = (String) map.get("telephone");
        //从Redis中获取缓存的验证码,key是  手机号+"_"+RedisMessageConstant.SENDTYPE_ORDER
        String codeInRedis = jedisPool.getResource().get(telephone + "_" + RedisMessageConstant.SENDTYPE_ORDER);
        //获取用户输入(请求数据中)的验证码
        String validateCode = (String) map.get("validateCode");
        //如果缓存为空,或者输入的值和缓存中的不相等,就返回错误提示
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        Result result = null;
        //调用体检预约服务
        try {
            //在map中增加一条信息,表示当前预约是微信预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //调用服务层,返回预约结果,封装到Result对象中(实际上是,Data属性里)
            result = orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

        // 这个判断是什么意思,这个对象都没有值把,为什么能用,[有值]  上面已经有返回了,这里还会执行吗[没有返回,我看错了]
       /* if (result.isFlag()) {
            //如果预约成功,还需要发送预约成功的短信
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }*/

        return result;
    }

    /**
     * 根据id查询用户预约的详细信息,需要关联3张表:套餐表,会员表,预约表
     *
     * @param id
     * @return
     */
    @RequestMapping("/findByDetailById")
    public Result findByDetailById(Integer id) {
        //关联查询3张表.返回的信息封装到map集中
        Map map = null;
        try {
            map = orderService.findByDetailById(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (map != null) {
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        }
        return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
    }
}
