package com.zuoyueer.jobs;

import com.zuoyueer.constant.RedisConstant;
import com.zuoyueer.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author Zuoyueer
 * Date: 2019/12/7
 * Time: 15:00
 * @projectName health_parent
 * @description: 定时任务类: 定时清理垃圾图片(只上传,但是没有提交到数据库的图片)
 * -------------------------------------------------------------------------------
 * 在新增套餐时套餐的基本信息和图片是分两次提交到后台进行操作的。也就是用户首先将图片上传到七牛云服务器，
 * 然后再提交新增窗口中录入的其他信息。如果用户只是上传了图片而没有提交录入的其他信息，此时的图片就变为了垃圾图片，
 * 因为在数据库中并没有记录它的存在。此时我们要如何处理这些垃圾图片呢？
 *
 * 	解决方案就是通过定时任务组件定时清理这些垃圾图片。为了能够区分出来哪些图片是垃圾图片，
 * 	我们在文件上传成功后将图片保存到了一个redis集合中，
 * 	当套餐数据插入到数据库后我们又将图片名称保存到了另一个redis集合中，
 * 	通过计算这两个集合的差值就可以获得所有垃圾图片的名称。
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 清理图片方法(定时执行的方法)
     */
    public void clearImg() {
        System.out.println("clearImg().....");

        //计算set的差值
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //遍历集合
        for (String pic : set) {
            //删除七牛空间上的图片
            QiniuUtils.deleteFileFromQiniu(pic);
            //删除redis集合中的记录
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, pic);
        }
    }
}
