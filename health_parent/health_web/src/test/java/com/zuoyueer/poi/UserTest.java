package com.zuoyueer.poi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.pojo.User;
import com.zuoyueer.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Zuoyueer
 * Date: 2019/12/15
 * Time: 19:44
 * @projectName health_parent
 * @description: 测试,查询User对象关联3张表
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springmvc.xml")

public class UserTest {

    @Reference
    private UserService userService;
    @Test
    public void method(){
        //通过dubug启动查看
        User admin = userService.findUserByUsername("xiaoming");
    }
}
