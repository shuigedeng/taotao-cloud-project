package com.taotao.cloud.office.easypoi.easypoi.test.excel.read;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.taotao.cloud.office.easypoi.test.en.EnumDataEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ExcelImportEnumUtilTest {

	///ExcelExportMsgClient 测试是这个到处的数据

	@Test
	public void test() {
		try {
			ImportParams params = new ImportParams();
			params.setTitleRows(1);
			long start = new Date().getTime();
			List<EnumDataEntity> list = ExcelImportUtil.importExcel(
				new FileInputStream(
					new File(FileUtilTest.getWebRootPath("import/EnumDataEntity.xlsx"))),
				EnumDataEntity.class, params);

			Assert.assertEquals(6, list.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
