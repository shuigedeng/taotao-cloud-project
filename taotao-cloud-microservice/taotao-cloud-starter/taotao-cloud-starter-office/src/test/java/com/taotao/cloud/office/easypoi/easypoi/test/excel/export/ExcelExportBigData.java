package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.taotao.cloud.office.easypoi.test.entity.MsgClient;
import com.taotao.cloud.office.easypoi.test.entity.MsgClientGroup;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

/**
 * 大数据量导出 Created by JueYue on 2017/9/7.
 */
public class ExcelExportBigData {

	@Test
	public void bigDataExport() throws Exception {

		Workbook workbook = null;
		Date start = new Date();
		ExportParams params = new ExportParams("大数据测试", "测试");
		workbook = ExcelExportUtil.exportBigExcel(params, MsgClient.class,
			new IExcelExportServer() {

				@Override
				public List<Object> selectListForExcelExport(Object obj, int page) {
					if (((int) obj) == page) {
						return null;
					}
					List<Object> list = new ArrayList<Object>();
					for (int i = 0; i < 10000; i++) {
						MsgClient client = new MsgClient();
						client.setBirthday(new Date());
						client.setClientName("小明" + i);
						client.setClientPhone("18797" + i);
						client.setCreateBy("JueYue");
						client.setId("1" + i);
						client.setRemark("测试" + i);
						MsgClientGroup group = new MsgClientGroup();
						group.setGroupName("测试" + i);
						client.setGroup(group);
						list.add(client);
					}
					return list;
				}
			}, 10);

		System.out.println(new Date().getTime() - start.getTime());
		File savefile = new File("D:/home/excel/");
		if (!savefile.exists()) {
			savefile.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(
			"D:/home/excel/ExcelExportBigData.bigDataExport.xlsx");
		workbook.write(fos);
		fos.close();
	}

}
