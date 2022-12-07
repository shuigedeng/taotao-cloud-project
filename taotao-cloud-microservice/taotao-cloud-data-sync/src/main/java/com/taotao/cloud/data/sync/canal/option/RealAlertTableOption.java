package com.taotao.cloud.data.sync.canal.option;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.abstracts.AlertTableOption;
import org.springframework.stereotype.Component;

/**
 * 真正的修改表信息操作
 *
 * @author shuigedeng
 * @version 2022.04 1.0.0
 * @since 2021/8/31 09:03
 */
@Component
public class RealAlertTableOption extends AlertTableOption {

	@Override
	public void doOption(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange) {
		System.out.println(
			"======================接口方式（修改表信息操作）==========================");
		System.out.println("use " + schemaName + ";\n" + rowChange.getSql());
		System.out.println("\n======================================================");
	}
}
