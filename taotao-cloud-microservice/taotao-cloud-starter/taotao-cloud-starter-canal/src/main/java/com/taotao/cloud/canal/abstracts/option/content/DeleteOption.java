package com.taotao.cloud.canal.abstracts.option.content;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.abstracts.option.AbstractDBOption;

/**
 * 删除数据
 *
 * @author 阿导
 * @CopyRight 万物皆导
 * @created 2018年05月29日 09:02:00
 * @Modified_By 阿导 2018/5/29 09:02
 */

public abstract class DeleteOption extends AbstractDBOption {
	
	/**
	 * 设置删除操作
	 *
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/29 09:22
	 * @CopyRight 万物皆导
	 */
	@Override
	protected void setEventType() {
		this.eventType = CanalEntry.EventType.DELETE;
	}
	
}
