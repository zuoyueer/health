package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zuoyueer.dao.CheckItemDao;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.CheckItem;
import com.zuoyueer.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/4
 * Time: 21:48
 * @projectName health_parent
 * @description: 检查项服务
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页查询
     * @param currentPage 当前页
     * @param pageSize  每页大小
     * @param queryString  查询条件(项目编码或者项目名称)
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //调用分页助手，指定分页条件，在执行SQL之前进行拦截，处理SQL（加入分页关键字）
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据检查项id删除
     */
    @Override
    public void delete(Integer id) {
        //先检查检查项id 到中间表 是否关联了检查组
        Long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            //如果当前检查项 已经被 关联到了检查组,那么就不能被删除
            throw new RuntimeException("当前检查项被引用,不能删除");
        }
        //如果没有关联, 则直接删除检查项
        checkItemDao.deleteById(id);
    }

    /**
     * 修改检查项
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 根据id查询检查项
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 查询全部
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

}
