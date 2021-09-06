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
package com.taotao.cloud.canal.model;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.abstracts.AbstractDBOption;
import com.taotao.cloud.canal.interfaces.CanalEventListener;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * 处理 Canal 监听器 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:31:11
 */
public class DealCanalEventListener implements CanalEventListener {

	/**
	 * 头结点
	 */
	private AbstractDBOption header;

	public DealCanalEventListener(AbstractDBOption... dbOptions) {
		AbstractDBOption tmp = null;
		for (AbstractDBOption dbOption : dbOptions) {
			if (tmp != null) {
				tmp.setNext(dbOption);
			} else {
				this.header = dbOption;
			}
			tmp = dbOption;
		}

	}

	public DealCanalEventListener(List<AbstractDBOption> dbOptions) {
		if (CollectionUtils.isEmpty(dbOptions)) {
			return;
		}
		AbstractDBOption tmp = null;
		for (AbstractDBOption dbOption : dbOptions) {
			if (tmp != null) {
				tmp.setNext(dbOption);
			} else {
				this.header = dbOption;
			}
			tmp = dbOption;
		}
	}

	@Override
	public void onEvent(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange) {
		this.header.doChain(destination, schemaName, tableName, rowChange);
	}

}
