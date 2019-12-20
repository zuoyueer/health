package com.zuoyueer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zuoyueer.constant.DateUtils;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.dao.MemberDao;
import com.zuoyueer.dao.OrderDao;
import com.zuoyueer.dao.OrderSettingDao;
import com.zuoyueer.entity.Result;
import com.zuoyueer.pojo.Member;
import com.zuoyueer.pojo.Order;
import com.zuoyueer.pojo.OrderSetting;
import com.zuoyueer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Zuoyueer
 * Date: 2019/12/12
 * Time: 23:32
 * @projectName health_parent
 * @description: 预约管理服务层实现类
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 体检预约
     *
     * @param map 用户预约数据
     */
    @Override
    public Result submitOrder(Map map) throws Exception {
        //1. 判断当前日期是否设置了预约,先获取用户预约日期
        String orderDate = (String) map.get("orderDate");
        //这里的异常必须抛,如果try catch 那么 那么后续程序会执行,这不符合我们的要求,因为失败了就不能继续了
        Date date = DateUtils.parseString2Date(orderDate);
        //这里必须查询完整记录,(不查询记录的数量) 因为后面需要这些数据
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        //如果当前日期不能预约,那么就直接返回错误信息
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2. 判断当前日期是否预约满了
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        //如果预约人数已经满了,就不能预约了,直接返回错误信息
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //3. 根据手机号判断当前用户是否是会员
        String telephont = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephont);

        if (member != null) {
            //4. 如果是会员,需要避免重复预约
            //会员id
            Integer memberId = member.getId();
            //套餐id
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            //创建体检预约查询的对象,查询条件就3个:一个是会员id,一个是日期,一个是套擦id,其他的不能加
            Order order = new Order(memberId, date, null, null, setmealId);
            //根据预约信息查询t_order
            List<Order> list = orderDao.findByCondition(order);
            //如果查询到了结果,说明是重复预约,直接返回信息
            if (list != null && list.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //如果当前用户不是会员,需要添加到会员表中
            //因为member是空,使用直接创建一个对象
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephont);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
            System.out.println("测试下增加返回的主键" + member);
        }

        //4. 进行预约
        //4.1 修改预约设置表,把已预约数+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //封装预约对象
        Order order = new Order(
                member.getId(),//会员id
                date,//日期
                (String) map.get("orderType"),//预约类型
                Order.ORDERSTATUS_NO,//预约状态,这里是未预约
                Integer.parseInt((String) map.get("setmealId"))//预约的套餐id
        );
        //保存预约信息到t_order表
        orderDao.add(order);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);

    }

    /**
     * 根据id查询用户预约的详细信息,需要关联3张表:套餐表,会员表,预约表
     *
     * @return 返回信息封装成map
     */
    @Override
    public Map findByDetailById(Integer id) throws Exception {
        Map map = orderDao.findDetailById(id);
        //先获取orderDate,然后再转换成String,存储到map中,覆盖原来的Date类型,
        // 否则页面渲染的时候会出现2019-12-12 0:0:0,转换之后就是2019-12-12了
        Date orderDate = (Date) map.get("orderDate");
        map.put("orderDate", DateUtils.parseDate2String(orderDate));
        return map;
    }
}
