package com.taotao.cloud.sys.biz.canal.option;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.abstracts.DropTableOption;
import org.springframework.stereotype.Component;

/**
 * 真正的删除表操作
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/31 09:04
 */
@Component
public class RealDropTableOption extends DropTableOption {

	@Override
	public void doOption(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange) {
		System.out.println("======================接口方式（删除表操作）==========================");
		System.out.println("use " + schemaName + ";\n" + rowChange.getSql());
		System.out.println("\n======================================================");
	}
}
