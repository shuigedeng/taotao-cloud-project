package com.taotao.cloud.sys.biz.canal.option;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.abstracts.InsertOption;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 真正的插入数据操作
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/31 09:05
 */
@Component
public class RealInsertOptoin extends InsertOption {

	//@Autowired
	//private Mapper mapper;
	//

	@Override
	public void doOption(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange) {
		System.out.println("======================接口方式（新增数据操作）==========================");
		List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
		for (CanalEntry.RowData rowData : rowDatasList) {

			String sql = "use " + schemaName + ";\n";
			StringBuffer colums = new StringBuffer();
			StringBuffer values = new StringBuffer();
			rowData.getAfterColumnsList().forEach((c) -> {
				colums.append(c.getName() + ",");
				values.append("'" + c.getValue() + "',");
			});

			sql += "INSERT INTO " + tableName + "(" + colums.substring(0, colums.length() - 1)
				+ ") VALUES(" + values.substring(0, values.length() - 1) + ");";
			System.out.println(sql);
			//mapper.doOption(sql);

		}
		System.out.println("\n======================================================");

	}
}
