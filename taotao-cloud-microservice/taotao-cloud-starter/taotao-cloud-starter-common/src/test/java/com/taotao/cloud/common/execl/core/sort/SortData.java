package com.taotao.cloud.common.execl.core.sort;

import com.alibaba.excel.annotation.ExcelProperty;



/**

 */
com.taotao.cloud.common.execl
public class SortData {
    private String column5;
    private String column6;
    @ExcelProperty(order = 100)
    private String column4;
    @ExcelProperty(order = 99)
    private String column3;
    @ExcelProperty(value = "column2", index = 1)
    private String column2;
    @ExcelProperty(value = "column1", index = 0)
    private String column1;
}
