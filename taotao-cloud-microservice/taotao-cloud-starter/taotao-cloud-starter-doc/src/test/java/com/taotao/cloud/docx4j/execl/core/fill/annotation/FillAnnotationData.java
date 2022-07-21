package com.taotao.cloud.docx4j.execl.core.fill.annotation;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentLoopMerge;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;

import java.util.Date;


@ContentRowHeight(100)
public class FillAnnotationData {
    @ExcelProperty("日期")
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private Date date;

    @ExcelProperty(value = "数字")
    @NumberFormat("#.##%")
    private Double number;

    @ContentLoopMerge(columnExtend = 2)
    @ExcelProperty("字符串1")
    private String string1;
    @ExcelProperty("字符串2")
    private String string2;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
	}

	public String getString1() {
		return string1;
	}

	public void setString1(String string1) {
		this.string1 = string1;
	}

	public String getString2() {
		return string2;
	}

	public void setString2(String string2) {
		this.string2 = string2;
	}
}
