package com.taotao.cloud.office.execl.core.annotation;

import com.alibaba.excel.annotation.ExcelProperty;

public class AnnotationIndexAndNameData {
    @ExcelProperty(value = "第四个", index = 4)
    private String index4;
    @ExcelProperty(value = "第二个")
    private String index2;
    @ExcelProperty(index = 0)
    private String index0;
    @ExcelProperty(value = "第一个", index = 1)
    private String index1;

	public String getIndex4() {
		return index4;
	}

	public void setIndex4(String index4) {
		this.index4 = index4;
	}

	public String getIndex2() {
		return index2;
	}

	public void setIndex2(String index2) {
		this.index2 = index2;
	}

	public String getIndex0() {
		return index0;
	}

	public void setIndex0(String index0) {
		this.index0 = index0;
	}

	public String getIndex1() {
		return index1;
	}

	public void setIndex1(String index1) {
		this.index1 = index1;
	}
}
