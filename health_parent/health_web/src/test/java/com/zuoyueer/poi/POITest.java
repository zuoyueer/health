package com.zuoyueer.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Zuoyueer
 * Date: 2019/12/7
 * Time: 22:39
 * @projectName health_parent
 * @description: POI的快速入门使用测试
 */
public class POITest {

    /**
     * 从Excel文件读取数据
     */
    @Test
    public void method() throws IOException {
        //1.创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("F:/test.xlsx");
        //2.获取工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        //3.遍历工作表,获取行对象
        for (Row row : sheet) {
            //遍历行对象,获取列(单元格)对象
            for (Cell cell : row) {
                //获取单元格中的数据
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("----------getStringCellValue无法获取单元格是数字的数据--------");
        }
        //关闭工作簿对象,关闭资源
        workbook.close();
    }

    /**
     * 通过索引读取数据
     */
    @Test
    public void method2() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("F:\\test.xlsx");
        //获取工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取最后一行的行号
        int lastRowNum = sheet.getLastRowNum();
        //遍历行,这里有等号 <=
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            //获取最后一列的列号
            short lastCellNum = row.getLastCellNum();
            //遍历列,这里没有等号 <
            for (int j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("------------------");
        }
        workbook.close();
    }

    /**
     * 向Excel文件写入数据
     */
    @Test
    public void method3() throws Exception {
        //创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workbook.createSheet("学生名单");
        //创建行
        XSSFRow row = sheet.createRow(0);
        //创建列,并设置内容
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("性别");
        row.createCell(2).setCellValue("地址");
        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("张三");
        row2.createCell(1).setCellValue("男");
        row2.createCell(2).setCellValue("深圳");
        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue("李四");
        row3.createCell(1).setCellValue("男");
        row3.createCell(2).setCellValue("北京");
        //通过输出流对象写到磁盘
        FileOutputStream fos = new FileOutputStream("F:/student.xlsx");
        workbook.write(fos);
        fos.flush();
        fos.close();
        workbook.close();
    }
}
