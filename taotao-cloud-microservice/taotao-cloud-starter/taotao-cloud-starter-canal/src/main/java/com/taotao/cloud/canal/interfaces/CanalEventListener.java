package com.taotao.cloud.canal.interfaces;

import com.alibaba.otter.canal.protocol.CanalEntry;

/**
 * canal 的事件接口层（表数据改变）
 *
 * @author 阿导
 * @CopyRight 萬物皆導
 * @created 2018/5/28 16:37
 * @Modified_By 阿导 2018/5/28 16:37
 */
@FunctionalInterface
public interface CanalEventListener {
	
	
	/**
	 * 处理事件
	 *
	 * @param destination 指令
	 * @param schemaName  库实例
	 * @param tableName   表名
	 * @param rowChange   詳細參數
	 * @return
	 * @author 阿导
	 * @time 2018/5/28 16:37
	 * @CopyRight 万物皆导
	 */
	void onEvent(String destination, String schemaName, String tableName, CanalEntry.RowChange rowChange);
	
}
