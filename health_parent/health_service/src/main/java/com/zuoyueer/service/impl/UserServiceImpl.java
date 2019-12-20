package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zuoyueer.constant.DateUtils;
import com.zuoyueer.dao.PermissionDao;
import com.zuoyueer.dao.RoleDao;
import com.zuoyueer.dao.UserDao;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.CheckGroup;
import com.zuoyueer.pojo.User;
import com.zuoyueer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/15
 * Time: 18:10
 * @projectName health_parent
 * @description: 用户管理实现类
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;



    /**
     * 根据用户名查询用户
     * ------------------------
     * 不仅要查询用户信息,还包括角色集合,以及角色对应的权限集合
     * -----------------------------
     * 因为封装到一个对象中,所以需要使用多步查询,
     * 之所以不进行多表联合一次查询,是因为需要封装到User对象中,而不是map集合
     *
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User findUserByUsername(String username) {
       return userDao.findUserByUsername(username);
    }


    //_______________________以下是CURD_____________________


    /**
     * 增加用户,同时需要增加中间表的对应关系
     */
    @Override
    public void add(User user, Integer[] roleIds) {

        //添加用户
        userDao.add(user);
        setUserAndRole(user.getId(), roleIds);
    }

    /**
     * 添加中间表的对应关系
     * @param userId
     * @param roleIds
     */
    private void setUserAndRole(Integer userId, Integer[] roleIds) {
        if (roleIds != null && roleIds.length > 0) {
            for (Integer roleId : roleIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("user_id", userId);
                map.put("role_id", roleId);
                userDao.setUserAndRole(map);
            }
        }
    }

    /**
     * 分页+条件 查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<User> page = userDao.selectByCondition(queryString);
        /*
        这样不行,不但不行,而且如果生气是空,会报空指针异常

        for (User user : page) {
            Date birthday = user.getBirthday();
            String format = new SimpleDateFormat("yyyy-MM-dd").format(birthday);
            try {
                user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(format));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/
        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    /**
     * 根据id查询中间表,查询角色的id集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleIdsByUserId(Integer id) {
        return userDao.findRoleIdsByUserId(id);
    }


    /**
     * 修改用户信息
     * @param user
     * @param roleIds
     */
    @Override
    public void edit(User user, Integer[] roleIds) {
        //删除中间表中 原来的关联关系
        userDao.deleteAssociation(user.getId());
        //增加中间表中  修改的关联关系
        setUserAndRole(user.getId(), roleIds);

        //更新用户信息
        userDao.edit(user);
    }

    @Override
    public void delete(Integer id) {
        //删除中间表  的关联数据
        userDao.deleteAssociation(id);
        //删除检查组数据
        userDao.delete(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
