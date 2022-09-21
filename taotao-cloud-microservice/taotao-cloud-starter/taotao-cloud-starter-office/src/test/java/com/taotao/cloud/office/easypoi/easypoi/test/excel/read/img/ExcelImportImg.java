package com.taotao.cloud.office.easypoi.easypoi.test.excel.read.img;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.taotao.cloud.office.easypoi.easypoi.test.excel.read.FileUtilTest;
import com.taotao.cloud.office.easypoi.test.entity.img.CompanyHasImgModel;
import java.io.File;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Created by JueYue on 2017/8/25.
 */
public class ExcelImportImg {

	@Test
	public void test() {
		try {
			ImportParams params = new ImportParams();
			params.setNeedSave(true);
			List<CompanyHasImgModel> result = ExcelImportUtil.importExcel(
				new File(FileUtilTest.getWebRootPath("import/imgexcel.xls")),
				CompanyHasImgModel.class, params);
			for (int i = 0; i < result.size(); i++) {
				System.out.println(ReflectionToStringBuilder.toString(result.get(i)));
			}
			Assert.assertTrue(result.size() == 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
