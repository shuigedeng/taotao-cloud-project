//package com.taotao.cloud.office.utils.easyexcel.other;
//
//import com.alibaba.excel.annotation.ExcelProperty;
//import com.alibaba.excel.annotation.write.style.ColumnWidth;
//import java.io.Serializable;
//import lombok.Data;
//
///**
// * 数据导入的Excel模板实体
// */
//@Data
//public class ImportExcelVo implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	@ColumnWidth(20)
//	@ExcelProperty(value = "公司名称", index = 0)
//	private String name;
//
//	@ColumnWidth(20)
//	@ExcelProperty(value = "公司联系电话", index = 1)
//	private String phone;
//
//	@ColumnWidth(28)
//	@ExcelProperty(value = "公司统一社会信用代码", index = 2)
//	private String creditCode;
//
//	@ColumnWidth(15)
//	@ExcelProperty(value = "区域", index = 3)
//	private String province;
//
//	@ColumnWidth(15)
//	@ExcelProperty(value = "公司法人", index = 4)
//	private String legalPerson;
//
//	@ExcelProperty(value = "备注", index = 5)
//	private String remark;
//}
