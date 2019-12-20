package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.constant.RedisMessageConstant;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.Member;
import com.zuoyueer.service.MemberService;
import com.zuoyueer.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/13
 * Time: 20:40
 * @projectName health_parent
 * @description: 登录客户端
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 登录验证:
     * 1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
     * <p>
     * 2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
     *
     * @param map
     * @param response
     * @return
     */
    @RequestMapping("/check")
    public Result check(@RequestBody Map map, HttpServletResponse response) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String redisCode = jedisPool.getResource().get(telephone + "_" + RedisMessageConstant.SENDTYPE_LOGIN);
        if (redisCode == null || !validateCode.equals(redisCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Member member = memberService.findByTelephone(telephone);
        if (member == null) {
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //登录校验成功之后,把用户手机号存储到cookid中
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        //有效路径
        cookie.setPath("/");
        //有效时间
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }


    /**
     * 再写一遍
     */
    @RequestMapping("/login2")
    public Result login2(@RequestBody Map map, HttpServletResponse response) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String redisCOde = jedisPool.getResource().get(telephone + "_" + RedisMessageConstant.SENDTYPE_LOGIN);
        if (StringUtils.isEmpty(validateCode) || StringUtils.isEmpty(redisCOde) || !validateCode.equals(redisCOde)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //判断是不是会员
        Member member = memberService.findByTelephone(telephone);
        if (member==null){
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }

        Cookie cookie = new Cookie("login_memeber_telephone", telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60*24*60);
        response.addCookie(cookie);
        return new Result(false, MessageConstant.LOGIN_SUCCESS);
    }
}
