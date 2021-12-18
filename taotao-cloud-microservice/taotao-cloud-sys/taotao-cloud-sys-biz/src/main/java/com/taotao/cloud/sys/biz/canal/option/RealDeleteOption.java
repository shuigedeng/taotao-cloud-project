package com.taotao.cloud.sys.biz.canal.option;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.abstracts.DeleteOption;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 真正的删除数据操作
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/31 09:05
 */
@Component
public class RealDeleteOption extends DeleteOption {

	@Override
	public void doOption(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange) {
		System.out.println("======================接口方式（删除数据操作）==========================");
		List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
		for (CanalEntry.RowData rowData : rowDatasList) {
			if (!CollectionUtils.isEmpty(rowData.getBeforeColumnsList())) {
				String sql = "use " + schemaName + ";\n";

				sql += "DELETE FROM " + tableName + " WHERE ";
				StringBuffer idKey = new StringBuffer();
				StringBuffer idValue = new StringBuffer();
				for (CanalEntry.Column c : rowData.getBeforeColumnsList()) {
					if (c.getIsKey()) {
						idKey.append(c.getName());
						idValue.append(c.getValue());
						break;
					}


				}

				sql += idKey + " =" + idValue + ";";

				System.out.println(sql);
			}
			System.out.println("\n======================================================");

		}
	}
}
