package com.taotao.cloud.demo.canal.option;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.abstracts.UpdateOption;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 真正的更新数据操作
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/31 09:06
 */
@Component
public class RealUpdateOption extends UpdateOption {

	@Override
	public void doOption(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange) {
		System.out.println("======================接口方式（更新数据操作）==========================");

		List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
		for (CanalEntry.RowData rowData : rowDatasList) {
			String sql = "use " + schemaName + ";\n";
			StringBuffer updates = new StringBuffer();
			StringBuffer conditions = new StringBuffer();
			rowData.getAfterColumnsList().forEach((c) -> {
				if (c.getIsKey()) {
					conditions.append(c.getName() + "='" + c.getValue() + "'");
				} else {
					updates.append(c.getName() + "='" + c.getValue() + "',");
				}
			});
			sql += "UPDATE " + tableName + " SET " + updates.substring(0, updates.length() - 1)
				+ " WHERE " + conditions;

			System.out.println(sql);
		}
		System.out.println("\n======================================================");

	}
}
