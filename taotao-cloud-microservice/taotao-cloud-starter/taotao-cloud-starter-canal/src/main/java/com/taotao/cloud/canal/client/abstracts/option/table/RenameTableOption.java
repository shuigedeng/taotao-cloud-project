package com.taotao.cloud.canal.client.abstracts.option.table;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.client.abstracts.option.AbstractDBOption;

/**
 * 重命名表名稱操作
 *
 * @author 阿导
 * @CopyRight 青团社
 * @created 2018年05月30日 16:55:00
 * @Modified_By 阿导 2018/5/30 16:55
 */
public abstract class RenameTableOption extends AbstractDBOption {
	
	/**
	 * 重命名表操作
	 *
	 * @author 阿导
	 * @CopyRight 萬物皆導
	 * @created 2018/5/29 09:21
	 */
	@Override
	protected void setEventType() {
		this.eventType = CanalEntry.EventType.RENAME;
	}
}
