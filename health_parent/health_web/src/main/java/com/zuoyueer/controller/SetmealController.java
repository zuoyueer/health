package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.constant.RedisConstant;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.entity.QueryPageBean;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.Setmeal;
import com.zuoyueer.service.SetmealService;
import com.zuoyueer.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * @author Zuoyueer
 * Date: 2019/12/6
 * Time: 23:23
 * @projectName health_parent
 * @description: 套餐管理控制层
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;


    /**
     * 图片上传
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //产生随机字符串
            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            //存储的文件名(唯一)
            String fileName = uuid + "_" + originalFilename;
            //上传图片
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //将上传图片的名称存入Redis,基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            //返回提示结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (Exception e) {
            //图片上传失败提示结果
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     *
     * @param setmeal       套餐数据
     * @param checkgroupIds 套餐的项目组id集合
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
            //添加成功后, 将图片名称保存到Redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
            //返回响应信息
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 分页查询
     *
     * @param queryPageBean 分页条件
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    /**
     * 删除套餐
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setmealService.delete(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

    /**
     * 根据id查询套餐的数据
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Setmeal setmeal = setmealService.findById(id);
        if (setmeal != null) {
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        }
        return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
    }

    /**
     * 根据套餐id查询绑定的项目组id集合
     */
    @RequestMapping("/findCheckGroupIdsBySetmealId")
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id){
        return  setmealService.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐
     * @param setmeal 套餐数据
     * @param checkgroupIds 套餐绑定的检查组的id集合
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer [] checkgroupIds){
        try {
            setmealService.edit(setmeal,checkgroupIds);
            return new Result( true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_STEAMAL_FAIL);
        }
    }
}
