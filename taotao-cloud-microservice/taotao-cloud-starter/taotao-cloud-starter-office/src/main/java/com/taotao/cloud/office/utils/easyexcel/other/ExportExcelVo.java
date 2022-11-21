//package com.taotao.cloud.office.utils.easyexcel.other;
//
//import com.alibaba.excel.annotation.ExcelProperty;
//import com.alibaba.excel.annotation.write.style.ColumnWidth;
//import java.io.Serializable;
//import lombok.Data;
//import lombok.experimental.Accessors;
//
///**
// * 资质信息导出实体
// */
//@Data   // Lombok注解，用于生成getter setter
//@Accessors(chain = true) //Lombok注解，链式赋值使用
//public class ExportExcelVo implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	@ColumnWidth(25)
//	@ExcelProperty(value = "企业名称", index = 0)
//	private String name;
//
//	@ColumnWidth(25)
//	@ExcelProperty(value = "社会统一信用代码", index = 1)
//	private String creditCode;
//
//	@ColumnWidth(15)
//	@ExcelProperty(value = "曾用名", index = 2)
//	private String formerName;
//
//	@ColumnWidth(15)
//	@ExcelProperty(value = "公司法人", index = 3)
//	private String legalPerson;
//
//	@ExcelProperty(value = "区域", index = 4)
//	private String province;
//
//	@ExcelProperty(value = "录入时间", index = 5)
//	private String createTime;
//
//	@ColumnWidth(15)
//	@ExcelProperty(value = "公司股东", index = 6)
//	private String stockholder;
//
//	@ExcelProperty(value = "企业联系方式", index = 7)
//	private String contact;
//
//}
