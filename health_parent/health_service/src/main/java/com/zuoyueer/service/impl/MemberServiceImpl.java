package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zuoyueer.constant.DateUtils;
import com.zuoyueer.dao.MemberDao;
import com.zuoyueer.pojo.Member;
import com.zuoyueer.service.MemberService;
import com.zuoyueer.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zuoyueer
 * Date: 2019/12/13
 * Time: 21:02
 * @projectName health_parent
 * @description: 会员管理服务层实现类
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 通过手机号验证会员是否存在,如果存在就返回该会员信息
     *
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 添加会员
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        //如果添加会员的时候有密码,持久化的时候必须加密后再存储
        if (member.getPassword() != null) {
            String md5 = MD5Utils.md5(member.getPassword());
            member.setPassword(md5);
        }
        //持久化,添加会员
        memberDao.add(member);
    }

    /**
     * 根据月份查询 当月会员的总数
     *
     * @param list 月份的集合
     * @return 每月对应的会员总数集合
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> list) {
        //创建返回对象
        List<Integer> integers = new ArrayList<>();
        if (list != null) {
            //遍历月份的集合
            for (String moth : list) {
                //给月份拼接上31号
                String newMoth = moth + "-31";
                //调用持久层
                Integer memberCountBeforeDate = memberDao.findMemberCountBeforeDate(newMoth);
                //数据封装到集合中
                integers.add(memberCountBeforeDate);
            }
        }
        return integers;
    }


    /**
     * 统计会员数性别的占比
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findMemberCountOfSex() {
        List<Map<String, Object>> mapList = memberDao.findMemberCountOfSex();
        for (Map<String, Object> map : mapList) {
            String sex = (String) map.get("name");
            if (sex == null) {
                map.put("name", "未知");
            } else if ("1".equals(sex)) {
                map.put("name", "男");
            } else if ("2".equals(sex)) {
                map.put("name", "女");
            }
        }
        return mapList;
    }

    /**
     * 统计会员数和年龄段占比
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findMemberOfAgeReport() {
        Date now = DateUtils.getDateByPreYears(0);
        Date dateOf18Year = DateUtils.getDateByPreYears(18);
        Date dateOf30Year = DateUtils.getDateByPreYears(30);
        Date dateOf45Year = DateUtils.getDateByPreYears(45);
        Date dateOf60Year = DateUtils.getDateByPreYears(60);
        Map<String, Date> map = new HashMap<>();
        map.put("now",now);
        map.put("dateOf18Year",dateOf18Year);
        map.put("dateOf30Year",dateOf30Year);
        map.put("dateOf45Year",dateOf45Year);
        map.put("dateOf60Year",dateOf60Year);
        List<Map<String, Object>> mapList = memberDao.findMemberOfAgeReport(map);
        return mapList;
    }


    /**
     * 动态时间段内的每个月份 的 会员统计
     *
     * @param date
     * @return
     */
    @Override
    public List<Integer> findMemberOfDateReport(List<String> date) {

        //创建返回对象
        List<Integer> integers = new ArrayList<>();
        if (date != null) {
            //遍历月份的集合
            for (String moth : date) {
                //给月份拼接上31号
                String startMoth = moth + "-1";
                String ednMoth = moth + "-31";
                Map<String, String> map = new HashMap<>();
                map.put("startMoth",startMoth);
                map.put("ednMoth",ednMoth);
                //调用持久层
                Integer memberCountBeforeDate = memberDao.findMemberOfDateReport(map);
                //数据封装到集合中
                integers.add(memberCountBeforeDate);
            }
        }
        return integers;
    }
}
