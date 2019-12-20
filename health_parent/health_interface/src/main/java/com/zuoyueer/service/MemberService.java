package com.zuoyueer.service;

import com.zuoyueer.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/13
 * Time: 20:57
 * @projectName health_parent
 * @description: 会员管理的接口服务
 */
public interface MemberService {
    /**
     * 通过手机号验证会员是否存在,如果存在就返回该会员信息
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     */
    void add(Member member);

    /**
     * 根据月份查询 当月会员的总数
     * @param list 月份的集合
     * @return  每月对应的会员总数集合
     */
    List<Integer> findMemberCountByMonth(List<String> list);

    /**
     * 查询会员数性别的占比
     * @return
     */
    List<Map<String, Object>> findMemberCountOfSex();

    /**
     * 统计会员数和年龄段占比
     * @return
     */
    List<Map<String, Object>> findMemberOfAgeReport();

    /**
     * 动态时间段内的月份 会员统计
     * @param date
     * @return
     */
    List<Integer> findMemberOfDateReport(List<String> date);
}
