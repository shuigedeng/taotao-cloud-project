package com.taotao.cloud.canal.client.abstracts.option.content;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.wwjd.starter.canal.client.abstracts.option.AbstractDBOption;

/**
 * 新增数据
 *
 * @author 阿导
 * @CopyRight 万物皆导
 * @created 2018年05月29日 08:58:00
 * @Modified_By 阿导 2018/5/29 08:58
 */

public abstract class InsertOption extends AbstractDBOption {
	
	
	/**
	 * 设置新增操作
	 *
	 * @param
	 * @return
	 * @author 阿导
	 * @time 2018/5/29 09:24
	 * @CopyRight 万物皆导
	 */
	@Override
	protected void setEventType() {
		this.eventType = CanalEntry.EventType.INSERT;
	}
}
