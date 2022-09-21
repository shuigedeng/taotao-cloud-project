package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.taotao.cloud.office.easypoi.easypoi.test.excel.styler.ExcelExportStatisticStyler;
import com.taotao.cloud.office.easypoi.test.entity.statistics.StatisticEntity;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

public class ExcelExportStatisticTest {

	@Test
	public void test() throws Exception {

		List<StatisticEntity> list = new ArrayList<StatisticEntity>();
		for (int i = 0; i < 20; i++) {
			StatisticEntity client = new StatisticEntity();
			client.setName("index" + i);
			client.setIntTest(1 + i);
			client.setLongTest(1 + i);
			client.setDoubleTest(1.2D + i);
			client.setBigDecimalTest(new BigDecimal(1.2 + i));
			client.setStringTest("12" + i);
			list.add(client);
		}
		Date start = new Date();
		ExportParams params = new ExportParams("2412312", "测试", ExcelType.XSSF);
		params.setStyle(ExcelExportStatisticStyler.class);
		Workbook workbook = ExcelExportUtil.exportExcel(params, StatisticEntity.class, list);
		System.out.println(new Date().getTime() - start.getTime());
		FileOutputStream fos = new FileOutputStream(
			"D:/home/excel/ExcelExportStatisticTest.export.xlsx");
		workbook.write(fos);
		fos.close();
	}
}
