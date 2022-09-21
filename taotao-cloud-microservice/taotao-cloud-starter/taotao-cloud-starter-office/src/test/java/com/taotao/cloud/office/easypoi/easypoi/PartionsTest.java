package com.taotao.cloud.office.easypoi.easypoi;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.taotao.cloud.office.easypoi.easypoi.csv.read.CsvDataRead;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by jueyue on 18-9-30.
 */
public class PartionsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsvDataRead.class);

	public static void main(String[] args) {
		try {
			List<ComInfoDTO> list = ExcelImportUtil.importExcel(
				new FileInputStream(new File(
					"C:\\Users\\jueyue\\Desktop\\查陈周公司导出数据结果—天眼查(W20122016981545306973526).xlsx")),
				ComInfoDTO.class, new ImportParams());
			for (ComInfoDTO c : list
			) {
				try {
					System.out.println(c.getCompanyName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
