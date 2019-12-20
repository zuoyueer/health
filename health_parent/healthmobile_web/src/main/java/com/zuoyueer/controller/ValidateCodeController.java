package com.zuoyueer.controller;

import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.constant.RedisMessageConstant;
import com.zuoyueer.entity.Result;
import com.zuoyueer.utils.SMSUtils;
import com.zuoyueer.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author Zuoyueer
 * Date: 2019/12/12
 * Time: 22:41
 * @projectName health_parent
 * @description: 短信验证码
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 提交体检预约时  发送手机验证码
     * send4Order 是 send for  Order ,不是0 是O
     *
     * @param telephone 用户输入的手机号码
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send40rder(String telephone) {
        //调用根据类生成4位的验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //发送短信,测试通过了就先把他注释,节约费用
            //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (Exception e) {
            //发送验证码失败
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("提交体检预约发送的手机验证码是: " + code);
        //将生成的验证码缓存到redis中
        jedisPool.getResource().setex(telephone + "_" + RedisMessageConstant.SENDTYPE_ORDER, 30 * 60, code.toString());
        //验证码发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 登录时的发送验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //发送验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        try {
           //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("用户登录发送的验证码为: "+code);
        //存储到redis中
        jedisPool.getResource().setex(telephone+"_"+RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
