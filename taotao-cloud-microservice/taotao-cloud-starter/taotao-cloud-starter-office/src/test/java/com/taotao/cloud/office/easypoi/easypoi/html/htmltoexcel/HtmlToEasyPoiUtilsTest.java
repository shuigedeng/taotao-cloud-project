package com.taotao.cloud.office.easypoi.easypoi.html.htmltoexcel;

import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

/**
 * @author JueYue 2017年4月16日
 */
public class HtmlToEasyPoiUtilsTest {

	@Test
	public void htmlToExcelByStr() throws Exception {
		StringBuilder html = new StringBuilder();
		Scanner s = new Scanner(getClass().getResourceAsStream("/html/sample.html"), "utf-8");
		while (s.hasNext()) {
			html.append(s.nextLine());
		}
		s.close();
		Workbook workbook = ExcelXorHtmlUtil.htmlToExcel(html.toString(), ExcelType.XSSF);
		File savefile = new File("D:\\home\\lemur");
		if (!savefile.exists()) {
			savefile.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream("D:\\home\\lemur\\htmlToExcelByStr.xlsx");
		workbook.write(fos);
		fos.close();
		workbook = ExcelXorHtmlUtil.htmlToExcel(html.toString(), ExcelType.HSSF);
		fos = new FileOutputStream("D:\\home\\lemur\\htmlToExcelByStr.xls");
		workbook.write(fos);
		fos.close();
	}

	@Test
	public void htmlToExcelByIs() throws Exception {
		Workbook workbook = ExcelXorHtmlUtil.htmlToExcel(
			getClass().getResourceAsStream("/html/sample.html"), ExcelType.XSSF);
		File savefile = new File("D:\\home\\lemur");
		if (!savefile.exists()) {
			savefile.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream("D:\\home\\lemur\\htmlToExcelByIs.xlsx");
		workbook.write(fos);
		fos.close();
		workbook = ExcelXorHtmlUtil.htmlToExcel(getClass().getResourceAsStream("/html/sample.html"),
			ExcelType.HSSF);
		fos = new FileOutputStream("D:\\home\\lemur\\htmlToExcelByIs.xls");
		workbook.write(fos);
		fos.close();
	}

}
