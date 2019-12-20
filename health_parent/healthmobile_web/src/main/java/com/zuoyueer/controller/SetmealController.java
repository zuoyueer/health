package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.Setmeal;
import com.zuoyueer.service.SetmealService;
import javassist.compiler.ast.Stmnt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/11
 * Time: 10:04
 * @projectName health_parent
 * @description: 套餐控制层
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取全部套餐数据
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        try {
            String allSetmeal = jedisPool.getResource().get("AllSetmeal");
            if (allSetmeal == null || "[]".equals(allSetmeal)) {
                List<Setmeal> list = setmealService.findAll();
                ObjectMapper objectMapper = new ObjectMapper();
                allSetmeal = objectMapper.writeValueAsString(list);
                jedisPool.getResource().set("AllSetmeal", allSetmeal);
            }
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, allSetmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 根据id查询套擦数据
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            String setmeal = jedisPool.getResource().get("SetmealBy_"+id);
            if (setmeal == null || "{}".equals(setmeal)) {
                Setmeal setmealById = setmealService.findById(id);
                ObjectMapper objectMapper = new ObjectMapper();
                setmeal = objectMapper.writeValueAsString(setmealById);
                jedisPool.getResource().set("SetmealBy_"+id, setmeal);
            }
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }
}
