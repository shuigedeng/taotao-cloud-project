package com.taotao.cloud.common.execl.temp.issue1662;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

com.taotao.cloud.common.execl
@AllArgsConstructor
@NoArgsConstructor
public class Data1662 {
    @ExcelProperty(index = 0)
    private String str;
    @ExcelProperty(index = 1)
    private Date date;
    @ExcelProperty(index = 2)
    private double r;
}
