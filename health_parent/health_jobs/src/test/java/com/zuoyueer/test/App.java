package com.zuoyueer.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Zuoyueer
 * Date: 2019/12/7
 * Time: 11:16
 * @projectName health_parent
 * @description: 测试
 */
public class App {
    public static void main(String[] args) {
        //初始化容器,那么就会执行定时任务了
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext-jobs.xml");
    }
}
