package com.taotao.cloud.common.execl.demo.web;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;



/**
 * 基础数据类
 *

 */
com.taotao.cloud.common.execl
public class DownloadData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
}
