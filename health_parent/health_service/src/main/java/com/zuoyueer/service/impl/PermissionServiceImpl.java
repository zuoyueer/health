package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zuoyueer.dao.PermissionDao;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.Permission;
import com.zuoyueer.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/18
 * Time: 20:25
 * @projectName health_parent
 * @description: 权限服务接口实现类
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage, pageSize);
        Page<Permission> page = permissionDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        //先检查检查项id 到中间表 是否关联了检查组
        Long count = permissionDao.findCountByPermissionId(id);
        if (count > 0) {
            //如果当前检查项 已经被 关联到了检查组,那么就不能被删除
            throw new RuntimeException("当前权限被引用,不能删除");
        }
        //如果没有关联, 则直接删除检查项
        permissionDao.deleteById(id);
    }

    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }
}
