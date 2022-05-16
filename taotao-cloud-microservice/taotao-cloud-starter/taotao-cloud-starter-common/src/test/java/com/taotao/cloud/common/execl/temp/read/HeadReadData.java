package com.taotao.cloud.common.execl.temp.read;

import com.alibaba.excel.annotation.ExcelProperty;



/**
 * 临时测试
 *

 */
public class HeadReadData {
    @ExcelProperty({"主标题","数据1"})
    private String h1;
    @ExcelProperty({"主标题", "数据2"})
    private String h2;
}
