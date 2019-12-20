package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zuoyueer.dao.MenuDao;
import com.zuoyueer.dao.RoleDao;
import com.zuoyueer.dao.UserDao;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.pojo.Menu;
import com.zuoyueer.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/17
 * Time: 10:12
 * @projectName health_parent
 * @description: 菜单接口实现类
 */
@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


    /**
     * 根据用户名, 权限关联查询该用户能访问的菜单
     * --------------------------------------
     * 多角色,每个菜单(包括)的权限,也考虑到了
     * @param username
     * @return
     */
    @Override
    public List<Map<String, Object>> getMenuList(String username) {

        //根据用户名,多表联合查询,获得用户名对应的菜单的主键集合
        List<Integer> menuIds =  menuDao.findMenuIdsByUsername(username);
        //根据菜单主键集合,获取一级菜单
        List<Map<String, Object>> firstMenus = menuDao.findFirstMenu(menuIds);

        //遍历一级
        for (Map<String, Object> firstMenu : firstMenus) {
            //每个一级的id
            Integer id = (Integer) firstMenu.get("id");
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",id);
            map.put("menuIds",menuIds);
            //根据一级的id查询二级的菜单集合,同时需要满足这个二级菜单该用户有权限,所以第二个参数是menuIds
            List<Map<String, Object>> secondMenus = menuDao.findSecondMenu(map);
            //把查询到的二级菜单集合 关联到一级菜单children 上
            firstMenu.put("children", secondMenus);
            //删除一级的id,因为前端不需要
            firstMenu.remove("id");
        }
        return firstMenus;
    }

    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //调用分页助手，指定分页条件，在执行SQL之前进行拦截，处理SQL（加入分页关键字）
        PageHelper.startPage(currentPage,pageSize);
        Page<Menu> page = menuDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        //先检查检查项id 到中间表 是否关联了检查组
        Long count = menuDao.findCountByMenuId(id);
        if (count > 0) {
            //如果当前检查项 已经被 关联到了检查组,那么就不能被删除
            throw new RuntimeException("当前菜单被引用,不能删除");
        }
        //如果没有关联, 则直接删除检查项
        menuDao.deleteById(id);
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }

    @Override
    public List<Menu> findAll() {
        return menuDao.findAll();
    }



    /*
      //这个初级版本,没有实现多角色,权限只能实现一级菜单
      @Override
    public List<Map<String, Object>> getMenuList(String username) {

        List<Map<String, Object>> firstMenus = null;

        //根据用户名获取对应角色
        User user = userDao.findUserByUsername(username);
        Set<Role> roles = user.getRoles();
        //存储角色的id集合
        ArrayList<Integer> roleIdList = new ArrayList<>();
        //获取角色对应的一级菜单
        for (Role role : roles) {
            roleIdList.add(role.getId());
        }

        firstMenus = menuDao.findFirstMenu(roleIdList);

        //一级
        // List<Map<String, Object>> firstMenus = menuDao.findFirstMenu();
        //遍历一级
        for (Map<String, Object> firstMenu : firstMenus) {
            //一级id
            Integer id = (Integer) firstMenu.get("id");
            //二级
            List<Map<String, Object>> secondMenus = menuDao.findSecondMenu(id);
            //关联二级到一级
            firstMenu.put("children", secondMenus);
            //删除一级的id,因为前端不需要
            firstMenu.remove("id");
        }
        return firstMenus;
    }
    *
    * */
}
