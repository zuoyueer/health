package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zuoyueer.constant.RedisConstant;
import com.zuoyueer.dao.SetmealDao;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.Setmeal;
import com.zuoyueer.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/7
 * Time: 0:01
 * @projectName health_parent
 * @description: 套餐管理的业务层实现类
 */
@Transactional
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;


    /**
     * 添加套餐
     *
     * @param setmeal       套餐数据
     * @param checkgroupIds 检查组的id集合
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐数据
        setmealDao.add(setmeal);
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        }
    }

    /**
     * 绑定套餐和检查组的多对多关系
     *
     * @param id            套餐的id
     * @param checkgroupIds 检查组的id集合
     */
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", id);
            map.put("checkgroup_id", checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }

    /**
     * 分页查询+条件
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除套餐: 先删除中间表 套餐绑定的检查组信息, 再删除套餐数据
     */
    @Override
    public void delete(Integer id) {
        //删除中间表 绑定数据
        setmealDao.deleteAssociation(id);
        //删除套餐记录
        setmealDao.delete(id);
    }

    /**
     * 根据查询套餐
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     * 查询套餐绑定的项目组的id集合
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 编辑套餐
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //删除原来的绑定关系
        setmealDao.deleteAssociation(setmeal.getId());
        //设置最新的绑定关系
        setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        //更新套餐的数据
        setmealDao.edit(setmeal);
    }

    /**
     * 查询全部套餐
     */
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 统计会员预约的各个套餐占比情况: 返回套餐名和该套餐被预约数
     *
     * @return 每个套餐数据封装到map中, 最终返回list集合
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
