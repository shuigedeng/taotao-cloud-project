package com.taotao.cloud.common.execl.temp.bug;

import com.alibaba.excel.annotation.ExcelProperty;



com.taotao.cloud.common.execl
public class HeadType {


    /**
     * 任务id
     */
    @ExcelProperty("任务ID")
    private Integer id;


    @ExcelProperty(value = "备注1")
    private String firstRemark;

    @ExcelProperty(value = "备注2")
    private String secRemark;

}
