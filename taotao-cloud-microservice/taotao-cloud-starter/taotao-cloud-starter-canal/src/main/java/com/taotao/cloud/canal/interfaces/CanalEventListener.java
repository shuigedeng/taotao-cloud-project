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
package com.taotao.cloud.canal.interfaces;

import com.alibaba.otter.canal.protocol.CanalEntry;

/**
 * canal 的事件接口层
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 21:46
 */
@FunctionalInterface
public interface CanalEventListener {


	/**
	 * 处理事件 处理数据库的操作
	 *
	 * @param destination 指令
	 * @param schemaName  库实例
	 * @param tableName   表名
	 * @param rowChange   詳細參數
	 * @author shuigedeng
	 * @since 2021/8/30 21:46
	 */
	void onEvent(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange);

}
