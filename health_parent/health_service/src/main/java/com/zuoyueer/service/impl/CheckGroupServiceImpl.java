package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zuoyueer.dao.CheckGroupDao;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.CheckGroup;
import com.zuoyueer.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/6
 * Time: 13:24
 * @projectName health_parent
 * @description: 检查组实现类
 */
@Transactional
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 增加检查组,同时需要增加中间表的对应关系
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }

    /**
     * 添加中间表的对应关系
     *
     * @param checkGroupId
     * @param checkitemIds
     */
    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    /**
     * 分页+条件 查询
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询项目组
     *
     * @param id
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据id查询中间表,查询检查组中的检查项id集合
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 修改检查组,同时需要修改中间表的对应关系
     *
     * @param checkGroup   检查组的信息
     * @param checkitemIds 包含的检查项id集合
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //删除中间表中 原来的关联关系
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //增加中间表中  修改的关联关系
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
        //更新检查组信息
        checkGroupDao.edit(checkGroup);
    }

    /**
     * 删除检查组: 需要先删除中间表的关联数据,再删除检查组数据
     */
    @Override
    public void delete(Integer id) {
        //删除中间表  的关联数据
        checkGroupDao.deleteAssociation(id);
        //删除检查组数据
        checkGroupDao.delete(id);
    }

    /**
     * 查询全部
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}


