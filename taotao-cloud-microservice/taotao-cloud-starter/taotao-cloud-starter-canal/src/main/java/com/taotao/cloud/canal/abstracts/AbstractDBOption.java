/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.canal.abstracts;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.interfaces.IDBOption;

/**
 * 数据库操作抽象类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 21:49
 */
public abstract class AbstractDBOption implements IDBOption {

	/**
	 * 操作类型
	 */
	protected CanalEntry.EventType eventType;
	/**
	 * 下一个节点
	 */
	protected AbstractDBOption next;

	public AbstractDBOption() {
		this.setEventType();
	}

	/**
	 * 进行类型设置
	 *
	 * @author shuigedeng
	 * @since 2021/8/30 21:48
	 */
	protected abstract void setEventType();


	/**
	 * 设置下一个节点
	 *
	 * @author shuigedeng
	 * @since 2021/8/30 21:48
	 */
	public void setNext(AbstractDBOption next) {
		this.next = next;
	}


	/**
	 * 责任链处理
	 *
	 * @author shuigedeng
	 * @since 2021/8/30 21:48
	 */
	public void doChain(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange) {
		if (this.eventType.equals(rowChange.getEventType())) {
			this.doOption(destination, schemaName, tableName, rowChange);
		} else {
			if (this.next == null) {
				return;
			}
			this.next.doChain(destination, schemaName, tableName, rowChange);
		}
	}


}
