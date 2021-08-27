package com.taotao.cloud.canal.client.interfaces;

import com.alibaba.otter.canal.protocol.CanalEntry;

/**
 * 数据库操作接口层
 *
 * @author 阿导
 * @CopyRight 万物皆导
 * @created 2018年05月28日 21:21:00
 * @Modified_By 阿导 2018/5/28 21:21
 */
@FunctionalInterface
public interface IDBOption {
	
	/**
	 * 操作
	 *
	 * @param destination 指令
	 * @param schemaName  实例名称
	 * @param tableName   表名称
	 * @param rowChange     数据
	 * @return
	 * @author 阿导
	 * @time 2018/5/29 08:59
	 * @CopyRight 万物皆导
	 */
	void doOption(String destination, String schemaName, String tableName, CanalEntry.RowChange rowChange);
}
