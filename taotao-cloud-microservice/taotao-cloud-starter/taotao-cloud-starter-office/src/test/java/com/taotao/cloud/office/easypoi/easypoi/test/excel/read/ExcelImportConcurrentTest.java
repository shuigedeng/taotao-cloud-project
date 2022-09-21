package com.taotao.cloud.office.easypoi.easypoi.test.excel.read;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by jueyue on 19-6-23.
 */
public class ExcelImportConcurrentTest {

	private Logger log = LoggerFactory.getLogger(ExcelImportConcurrentTest.class);

	@Test
	public void test() {
		try {
			Date start = new Date();
			log.debug("start");
			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			params.setConcurrentTask(true);
			params.setCritical(500);
			List<MsgClient> result = ExcelImportUtil.importExcel(
				new File(FileUtilTest.getWebRootPath("import/BigDataExport.xlsx")),
				MsgClient.class, params);
			log.debug("end,time is {}", ((new Date().getTime() - start.getTime()) / 1000));
			Assert.assertTrue(result.size() == 200000);
		} catch (Exception e) {
		}
	}

	@Test
	public void testSax() {
		try {
			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			long start = new Date().getTime();
			log.debug("start");
			List<MsgClient> result = new ArrayList<>();
			ExcelImportUtil.importExcelBySax(
				new FileInputStream(
					new File(FileUtilTest.getWebRootPath("import/BigDataExport.xlsx"))),
				MsgClient.class, params, new IReadHandler<MsgClient>() {
					@Override
					public void handler(MsgClient o) {
						result.add(o);
					}

					@Override
					public void doAfterAll() {
						System.out.println("全部执行完毕了--------------------------------");
						log.debug("end,time is {}", ((new Date().getTime() - start) / 1000));
					}
				});
			Assert.assertTrue(result.size() == 200000);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
