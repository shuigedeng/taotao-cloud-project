package com.taotao.cloud.docx4j.execl.core.style;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;



/**

 */
@HeadStyle
@HeadFontStyle
public class StyleData {
    @ExcelProperty("字符串")
    private String string;
    @ExcelProperty("字符串1")
    private String string1;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getString1() {
		return string1;
	}

	public void setString1(String string1) {
		this.string1 = string1;
	}
}
