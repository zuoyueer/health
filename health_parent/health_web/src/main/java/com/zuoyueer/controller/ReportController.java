package com.zuoyueer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuoyueer.constant.MessageConstant;
import com.zuoyueer.entity.Result;
import com.zuoyueer.service.MemberService;
import com.zuoyueer.service.ReportService;
import com.zuoyueer.service.SetmealService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zuoyueer
 * Date: 2019/12/15
 * Time: 21:20
 * @projectName health_parent
 * @description: echart的数据控制层
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 获取当前时间的前12个月的月份数据,封装成list
     * 根据每个月,获取月份的会员数量,封装到list中
     * 再把两个list封装到map中
     * 再把map封装到Result对象中,返回给客户端
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        Calendar calendar = Calendar.getInstance();
        //获取当前日期,前12个月(一年前)的时间
        calendar.add(Calendar.MONTH, -12);
        List<String> list = new ArrayList<>();
        //循环12次
        for (int i = 0; i < 12; i++) {
            //每次增加一个月
            calendar.add(Calendar.MONTH, 1);
            //把月份存储到list集合中
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        System.out.println(list);
        Map<String, Object> map = new HashMap<>();
        map.put("months", list);
        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    /**
     * 统计会员预约的各个套餐占比情况: 返回套餐名和该套餐被预约数
     * -------------数据封装如下--------------------
     * data--->Map
     * setmealNames--->List<String>
     * setmealCount--->List<Map>
     *
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        //统计会员预约的各个套餐占比情况: 返回套餐名和该套餐被预约数
        List<Map<String, Object>> list = setmealService.findSetmealCount();
        //创建响应对象
        Map<String, Object> map = new HashMap<>();
        map.put("setmealCount", list);

        List<String> names = new ArrayList<>();
        //把套餐的名字单独封装到一个list集合中
        for (Map<String, Object> m : list) {
            //获取套餐的名字
            String name = (String) m.get("name");
            names.add(name);
        }
        map.put("setmealNames", names);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
    }

    /**
     * 获取运营统计数据,Excel报表
     *
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }


    /**
     * 下载Excel文件
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //a.获取需要导出的运营数据 getBusinessReportData()
            Map<String, Object> result = reportService.getBusinessReportData();
            //读取模板,获得模板的路径, File.separator是根据系统自动的分隔符
            String realPath = request.getSession().getServletContext().getRealPath("template" + File.separator + "report_template.xlsx");
            //根据输入流创建工作簿对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(realPath)));


            //使用JXL技术将数据填充到模板中即可
            XLSTransformer transformer = new XLSTransformer();
            //第一个参数是工作簿对象,第二个是数据的,必须是Map类型
            transformer.transformWorkbook(workbook, result);


            //输出流
            ServletOutputStream outputStream = response.getOutputStream();
            ////文件类型 xls xlsx
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //attachment表示已附件形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL, null);
        }
    }

    /**
     * 根据性别统计会员数量
     * @return
     */
    @RequestMapping("/getMemberOfSexReport")
    public Result getMemberOfSexReport() {
        //统计会员预约的各个套餐占比情况: 返回套餐名和该套餐被预约数
        List<Map<String, Object>> list = memberService.findMemberCountOfSex();
        //创建响应对象
        Map<String, Object> map = new HashMap<>();
        map.put("memberCount", list);

        List<String> sexs = new ArrayList<>();
        //把套餐的名字单独封装到一个list集合中
        for (Map<String, Object> m : list) {
            //获取套餐的名字
            String name = (String) m.get("name");
            sexs.add(name);
        }
        map.put("sexs", sexs);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
    }

    /**
     * 根据年龄统计会员数
     * @return
     */
    @RequestMapping("/getMemberOfAgeReport")
    public Result getMemberOfAgeReport(){
        //统计会员预约的各个套餐占比情况: 返回套餐名和该套餐被预约数
        List<Map<String, Object>> list = memberService.findMemberOfAgeReport();
        //创建响应对象
        Map<String, Object> map = new HashMap<>();
        map.put("memberCount", list);

        List<String> ages = new ArrayList<>();
        //把套餐的名字单独封装到一个list集合中
        for (Map<String, Object> m : list) {
            //获取套餐的名字
            String name = (String) m.get("name");
            ages.add(name);
        }
        map.put("ages", ages);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
    }

    /**
     * 动态月份数据显示(包括起始月和末月)
     * @param time
     * @return
     */
    @RequestMapping("/getMemberOfDateReport")
    public Result getMemberOfDateReport(@RequestBody String [] time) {

        List<String> date=new ArrayList<>();
        try{
            Date d1 = new SimpleDateFormat("yyyy-MM").parse(time[0]);//定义起始日期

            Date d2 = new SimpleDateFormat("yyyy-MM").parse(time[1]);//定义结束日期

            Calendar dd = Calendar.getInstance();//定义日期实例

            dd.setTime(d1);//设置日期起始时间

            while (dd.getTime().before(d2)) {//判断是否到结束日期

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

                String str = sdf.format(dd.getTime());

                date.add(str);

                dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
            }
            date.add(new SimpleDateFormat("yyyy-MM").format(d2));//加上end的月份

        }catch (Exception e){
            System.out.println("异常"+e.getMessage());
        }

        System.out.println(date);
        Map<String, Object> map = new HashMap<>();
        map.put("months", date);
        List<Integer> memberCount = memberService.findMemberOfDateReport(date);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }



/*
    //手动填充数据到Excel文件中
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            String realPath = request.getSession().getServletContext().getRealPath("template" + File.separator + "report_template.xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(realPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;

            //热门套餐
            for (Map map : hotSetmeal) {
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(map.get("name").toString());
                row.getCell(5).setCellValue(map.get("setmeal_count").toString());
                row.getCell(6).setCellValue(map.get("setmeal_count").toString());
            }
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            //attachment表示已附件形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL, null);
        }
    }*/


}
