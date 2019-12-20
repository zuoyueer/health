package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.entity.PageResult;
import com.zuoyueer.entity.QueryPageBean;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.CheckGroup;
import com.zuoyueer.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/6
 * Time: 13:15
 * @projectName health_parent
 * @description: 检查组管理
 */
@RestController
@RequestMapping("/checkgroup")
public class checkGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 增加检查组
     *
     * @param checkGroup   检查组信息
     * @param checkitemIds 检查组关联的检查项的id集合
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页查询,还有条件查询
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

    /**
     * 根据id查询项目组
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        if (checkGroup != null) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        }
        return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
    }

    /**
     * 查询检查组中 全部检查项的id集合
     * @param id 检查组的id
     */
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupService.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 修改检查组
     * @param checkGroup  检查组的数据
     * @param checkitemIds  需要修改的 检查项的id集合
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.edit(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 删除检查组
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkGroupService.delete(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    /**
     * 查询全部
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> groupList = checkGroupService.findAll();
        if (groupList != null && groupList.size() > 0) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, groupList);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }
}
