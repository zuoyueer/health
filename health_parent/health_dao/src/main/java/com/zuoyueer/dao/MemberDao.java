package com.zuoyueer.dao;

import com.github.pagehelper.Page;
import com.zuoyueer.pojo.Member;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/13
 * Time: 10:00
 * @projectName health_parent
 * @description: 会员持久层
 */
public interface MemberDao {

    /**
     * 根据手机号查询会员
     *
     * @param telephont 手机号
     * @return
     */
    Member findByTelephone(String telephont);

    /**
     * 添加会员
     *
     * @param member
     */
    void add(Member member);

    /**
     * 查询全部会员
     * @return 会员集合
     */
    List<Member> findAll();

    /**
     * 分页条件查询
     * 根据条件查询: fileNumber or  phoneNumber  or  name
     * @param queryString
     * @return
     */
    Page<Member> selectByCondition(String queryString);

    /**
     * 根据id删除会员
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据id查询会员
     * @param id
     * @return
     */
    Member findById(Integer id);

    /**
     * 根据id 编辑(修改)会员
     * @param member
     */
    void edit(Member member);

    /**
     * 根据日期统计会员数，统计指定日期之前的会员数
     * @param date
     * @return
     */
    Integer findMemberCountBeforeDate(String date);

    /**
     * 根据日期统计会员数
     * @param date
     * @return
     */
    Integer findMemberCountByDate(String date);

    /**
     * 根据日期统计会员数，统计指定日期之后的会员数
     * @param date
     * @return
     */
    Integer findMemberCountAfterDate(String date);

    /**
     * 总会员数
     * @return
     */
    Integer findMemberTotalCount();

    /**
     * 统计会员数性别的占比
     * @return
     */
    List<Map<String, Object>> findMemberCountOfSex();

    /**
     * 统计会员数和年龄段占比
     * @return
     */
    List<Map<String, Object>> findMemberOfAgeReport(Map<String, Date> map);

    /**
     * 每个月份的数据
     * @param map
     * @return
     */
    Integer findMemberOfDateReport(Map<String, String> map);
}
