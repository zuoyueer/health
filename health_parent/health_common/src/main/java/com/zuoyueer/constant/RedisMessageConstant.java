package com.zuoyueer.constant;

/**
 * @author Zuoyueer
 * Date: 2019/12/11
 * Time: 8:58
 * @projectName health_parent
 * @description: 验证码常量类
 */
public class RedisMessageConstant {
    private RedisMessageConstant() {

    }

    //用于缓存体验预约时发送的验证码
    public static final String SENDTYPE_ORDER = "001";

    //用于缓存手机号快速登录时发送的验证码
    public static final String SENDTYPE_LOGIN = "002";

    //用于缓存找回密码时发送的验证码
    public static final String SENDTYPE_GETPWD = "003";
}
