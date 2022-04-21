package com.taotao.cloud.common.execl.demo.write;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;



/**
 * 基础数据类
 *

 */
com.taotao.cloud.common.execl
public class LongestMatchColumnWidthData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题很长日期标题很长日期标题很长很长")
    private Date date;
    @ExcelProperty("数字")
    private Double doubleData;
}
