package com.zuoyueer.utils2;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zuoyueer
 * Date: 2019/12/10
 * Time: 21:45
 * @projectName health_parent
 * @description: 自己写工具类:读取Excel文件,并解析返回List<String[]>
 */
public class PoiUtils {
    //Excel 2003的文件后缀
    private final static String XLS = "xls";
    //Excel 2007的文件后缀
    private final static String XLSX = "xlsx";
    //日期格式
    private final static String DATE_FORMAT = "yyyy/MM/dd";

    /**
     * 读入excel文件，解析后返回
     *
     * @param file
     */
    public static List<String[]> readExcel(MultipartFile file) throws IOException {
        //检查文件
        checkFile(file);
        //获得工作簿对象
        Workbook workBook = getWorkBook(file);
        //创建返回对象,把每行的数据作为一个数组, 所有的行数组再封装到List集合中
        List<String[]> list = new ArrayList<>();
        if (workBook != null) {
            //遍历工作表对象
            for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
                //获取工作表对象
                Sheet sheet = workBook.getSheetAt(sheetNum);
                //如果是空就跳过该sheet的遍历
                if (sheet == null) {
                    continue;
                }
                //获取当前sheet的开始行号
                int firstRowNum = sheet.getFirstRowNum();
                //获取当前sheet的最后行号
                int lastRowNum = sheet.getLastRowNum();
                //循环遍历当前sheet表的全部行(除了第一行)
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得行
                    Row row = sheet.getRow(rowNum);
                    //如果是空,就跳过该行的遍历
                    if (row == null) {
                        continue;
                    }
                    //每一行开始的列号
                    int firstCellNum = row.getFirstCellNum();
                    //每一行结束的列号
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    //每一行都创建一个数组,来存放当前行的全部列数据
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    //遍历当前行的全部列
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        //获取列
                        Cell cell = row.getCell(cellNum);
                        //列的数据存储到数组中
                        cells[cellNum] = getCellValue(cell);
                    }
                    //把每一行的全部数据封装到list集合中
                    list.add(cells);
                }
            }
            workBook.close();
        }
        return list;
    }

    /**
     * 检查文件
     */
    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (file == null) {
            throw new FileNotFoundException("文件不存在");
        }
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //判断文件后缀名
        if (!originalFilename.endsWith(XLS) && !originalFilename.endsWith(XLSX)) {
            throw new IOException(originalFilename + "不是Excel文件");
        }
    }

    /**
     * 获取工作簿对象
     * ------------------------
     * 因为版本不同,Workbook接口的实现类不同, 创建的对象也不同
     * ----------------------
     * 创建对象的时候使用文件输入流作为参数
     */
    public static Workbook getWorkBook(MultipartFile file) {
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //创建一个工作簿对象,表示整个Excel对象
        Workbook workbook = null;
        try {
            //获取文件的io流
            InputStream inputStream = file.getInputStream();
            //根据文件后缀名不同, 获取不同的工作簿对象
            if (originalFilename.endsWith(XLS)) {
                //2003
                workbook = new HSSFWorkbook(inputStream);
            } else if (originalFilename.endsWith(XLSX)) {
                //2007
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return workbook;
    }

    //对数据做类型转换
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //如果当前单元格的内容是日期类型,需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if ("m/d/yy".equals(dataFormatString)) {
            cellValue = new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
            return cellValue;
        }
        //把数字当做String来读,避免出现1读取成1.0的情况
        if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING://字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN://Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA://公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK:
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "为知类型";
                break;
        }
        return cellValue;
    }


}
