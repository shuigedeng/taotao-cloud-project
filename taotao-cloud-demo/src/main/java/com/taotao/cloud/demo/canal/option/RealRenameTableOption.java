package com.taotao.cloud.demo.canal.option;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.abstracts.RenameTableOption;
import org.springframework.stereotype.Component;

/**
 * 真正的重新命名操作
 *
 * @author shuigedeng
 * @version 2022.04 1.0.0
 * @since 2021/8/31 09:06
 */
@Component
public class RealRenameTableOption extends RenameTableOption {

	@Override
	public void doOption(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange) {
		System.out.println("======================接口方式（重新命名表操作）==========================");
		System.out.println("use " + schemaName + ";\n" + rowChange.getSql());
		System.out.println("\n======================================================");
	}
}
