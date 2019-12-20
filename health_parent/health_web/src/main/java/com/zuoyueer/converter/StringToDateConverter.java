package com.zuoyueer.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Zuoyueer
 * Date: 2019/12/18
 * Time: 17:00
 * @projectName health_parent
 * @description: 自定义类型转换器
 */
public class StringToDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String s) {
        try {
            if (StringUtils.isEmpty(s)){
                //允许输入的日期为空
               return null;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(s);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("输入的日期有误!");
        }
    }
}
