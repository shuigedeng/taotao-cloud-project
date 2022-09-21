package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

/**
 * 图片导出 Created by JueYue on 2017/9/15.
 */
public class TemplateImage {

	@Test
	public void img03() throws Exception {
		TemplateExportParams params = new TemplateExportParams(
			"doc/exportTemp_image.xls", true);
		Map<String, Object> map = new HashMap<String, Object>();
		// sheet 2
		map.put("month", 10);
		Map<String, Object> temp;
		for (int i = 1; i < 8; i++) {
			temp = new HashMap<String, Object>();
			temp.put("per", i * 10);
			temp.put("mon", i * 1000);
			temp.put("summon", i * 10000);
			ImageEntity image = new ImageEntity();
			image.setHeight(400);
			image.setWidth(500);
			image.setUrl("imgs/company/baidu.png");
			temp.put("image", image);
			map.put("i" + i, temp);
		}
		Workbook book = ExcelExportUtil.exportExcel(params, map);
		File savefile = new File("D:/home/excel/");
		if (!savefile.exists()) {
			savefile.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream("D:/home/excel/exportTemp_image.xls");
		book.write(fos);
		fos.close();

	}

	@Test
	public void img07Span() throws Exception {
		TemplateExportParams params = new TemplateExportParams(
			"doc/exportTemp_image.xlsx", true);
		Map<String, Object> map = new HashMap<String, Object>();
		// sheet 2
		map.put("month", 10);
		Map<String, Object> temp;
		for (int i = 1; i < 8; i++) {
			temp = new HashMap<String, Object>();
			temp.put("per", i * 10);
			temp.put("mon", i * 1000);
			temp.put("summon", i * 10000);
			ImageEntity image = new ImageEntity();
			image.setHeight(200);
			image.setWidth(500);
			image.setRowspan(4);
			image.setColspan(2);
			image.setUrl("imgs/company/baidu.png");
			temp.put("image", image);
			map.put("i" + i, temp);
		}
		Workbook book = ExcelExportUtil.exportExcel(params, map);
		File savefile = new File("D:/home/excel/");
		if (!savefile.exists()) {
			savefile.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream("D:/home/excel/exportTemp_image_span.xlsx");
		book.write(fos);
		fos.close();

	}

	@Test
	public void img07() throws Exception {
		TemplateExportParams params = new TemplateExportParams(
			"doc/exportTemp_image.xlsx", true);
		Map<String, Object> map = new HashMap<String, Object>();
		// sheet 2
		map.put("month", 10);
		Map<String, Object> temp;
		for (int i = 1; i < 8; i++) {
			temp = new HashMap<String, Object>();
			temp.put("per", i * 10);
			temp.put("mon", i * 1000);
			temp.put("summon", i * 10000);
			ImageEntity image = new ImageEntity();
			image.setHeight(200);
			image.setWidth(500);
			image.setUrl("imgs/company/baidu.png");
			temp.put("image", image);
			map.put("i" + i, temp);
		}
		Workbook book = ExcelExportUtil.exportExcel(params, map);
		File savefile = new File("D:/home/excel/");
		if (!savefile.exists()) {
			savefile.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream("D:/home/excel/exportTemp_image.xlsx");
		book.write(fos);
		fos.close();

	}


	@Test
	public void listimg07() throws Exception {
		TemplateExportParams params = new TemplateExportParams(
			"doc/kyoffice_address_book_img.xls");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("companyName", ";智能");
		data.put("editDate", new Date());
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		Map<String, Object> emp1 = Maps.newHashMap();
		emp1.put("deptId", "0001");
		emp1.put("deptName", "人事部");
		emp1.put("staffName", "小红");
		emp1.put("gender", 0);
		emp1.put("mobile", "13876543210");
		emp1.put("email", "xiaohong@kyoffice.com");
		emp1.put("workDate", new Date());
		emp1.put("salary", 8000);
		emp1.put("remark", "新人");

		Map<String, Object> emp2 = Maps.newHashMap();
		emp2.put("deptId", "0002");
		emp2.put("deptName", "研发部");
		emp2.put("staffName", "小明");
		emp2.put("gender", 1);
		emp2.put("mobile", "18670044463");
		emp2.put("email", "xiaoming@kyoffice.com");
		emp2.put("workDate", new Date());
		emp2.put("salary", 20000);
		emp2.put("remark", "老司机");
		ImageEntity image = new ImageEntity();
		image.setHeight(20 * 45);
		image.setWidth(500);
		image.setUrl("imgs/company/baidu.png");
		emp2.put("img", image);
		emp1.put("img", image);
		for (int i = 0; i < 4; i++) {
			listMap.add(emp1);
			listMap.add(emp2);
		}
		data.put("employeeList", listMap);

		Workbook workbook = ExcelExportUtil.exportExcel(params, data);
		File savefile = new File("D:/home/excel/");
		if (!savefile.exists()) {
			savefile.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream("D:/home/excel/exportTemp_listimg07.xls");
		workbook.write(fos);
		fos.close();

	}
}
