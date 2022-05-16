package com.taotao.cloud.common.execl.demo.write;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;



/**
 * 复杂头数据.这里最终效果是第一行就一个主标题，第二行分类
 *

 */
public class ComplexHeadData {
    @ExcelProperty({"主标题", "字符串标题"})
    private String string;
    @ExcelProperty({"主标题", "日期标题"})
    private Date date;
    @ExcelProperty({"主标题", "数字标题"})
    private Double doubleData;
}
