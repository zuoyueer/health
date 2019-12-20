package com.zuoyueer.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author Zuoyueer
 * Date: 2019/12/13
 * Time: 21:31
 * @projectName health_parent
 * @description: 使用md5的算法进行加密
 */


public class MD5Utils {
    /**
     * md5：不可逆的算法
     * 相同的明文---->相同的密文
     * 密文-----不可逆--->明文
     *
     * @param plainText 明文
     * @return 密文字符串
     */
    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * 测试上面的加密
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(md5("1234"));
        System.out.println(md5("1234").length());
    }

}