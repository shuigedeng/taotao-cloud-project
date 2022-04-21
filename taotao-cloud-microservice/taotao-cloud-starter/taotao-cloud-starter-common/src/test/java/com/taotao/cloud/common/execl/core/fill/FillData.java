package com.taotao.cloud.common.execl.core.fill;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.converters.doubleconverter.DoubleStringConverter;


public class FillData {
    private String name;
    @NumberFormat("#")
    @ExcelProperty(converter = DoubleStringConverter.class)
    private Double number;
    private String empty;
}
