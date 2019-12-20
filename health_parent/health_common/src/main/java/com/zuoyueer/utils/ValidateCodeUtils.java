package com.zuoyueer.utils;

import java.util.Random;

/**
 * @author Zuoyueer
 * Date: 2019/12/10
 * Time: 11:21
 * @projectName health_parent
 * @description: 随机生成验证码工具类
 */
public class ValidateCodeUtils {


    /**
     * 随机生成验证码
     * @param length 长度是4或者6
     * @return
     */
    public static Integer generateValidateCode(int length) {
        Integer code = null;
        if (length == 4) {
            //生成4位随机数
            code = new Random().nextInt(9000) + 1000;
        } else if (length == 6) {
            //生成6位随机数
            code = new Random().nextInt(900000) + 100000;
        }
        return code;
    }

    /**
     * 随机生成制定长度的字符串验证码
     */
    public static String generateValidateCode4String(int length){
        Random random = new Random();
        //toHexString方法返回的字符串表示的无符号整数参数所表示的值以十六进制
        String hexString = Integer.toHexString(random.nextInt());
        return hexString.substring(0, length);
    }


    /**
     * 测试
     */
    public static void main(String[] args) {
        boolean b = false;
        for (int i = 0; i < 5; i++) {
            int num = new Random().nextInt(9) + 1;
            System.out.println(num);
            if (num < 1 || num > 9) {
                b = true;
            }
        }
        System.out.println(b);

        System.out.println((int) Math.round(Math.random() * (2 - 1) + 1));
        System.out.println(Math.round(1.5999));
        System.out.println( 5% 3 + 1);
        Random random = new Random();
        System.out.println(Integer.toHexString(random.nextInt()));
    }
}
