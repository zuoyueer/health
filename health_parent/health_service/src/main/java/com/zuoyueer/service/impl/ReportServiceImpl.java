package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zuoyueer.constant.DateUtils;
import com.zuoyueer.dao.MemberDao;
import com.zuoyueer.dao.OrderDao;
import com.zuoyueer.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/16
 * Time: 17:33
 * @projectName health_parent
 * @description: 统计报表的实现类
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 获得运营统计数据
     * Map数据格式：
     * todayNewMember -> number
     * totalMember -> number
     * thisWeekNewMember -> number
     * thisMonthNewMember -> number
     * todayOrderNumber -> number
     * todayVisitsNumber -> number
     * thisWeekOrderNumber -> number
     * thisWeekVisitsNumber -> number
     * thisMonthOrderNumber -> number
     * thisMonthVisitsNumber -> number
     * hotSetmeals -> List<Setmeal>
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        //获取现在的时间
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //获取本周一的日期,在外国第一天不是周一
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获取本月的第一天日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //统计今日新增的会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);

        //统计总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        //统计本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

        //统计本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //统计今天的预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);

        //统计本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);

        //统计本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);

        //统计今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);

        //统计本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);

        //统计本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //统计热门套餐
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        //创建返回对象,并封装以上数据
        Map<String ,Object> result = new HashMap<>();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);
        return result;
    }
}
