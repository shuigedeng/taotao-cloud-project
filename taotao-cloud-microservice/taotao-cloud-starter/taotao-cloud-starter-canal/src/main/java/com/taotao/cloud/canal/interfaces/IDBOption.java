/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
 * 数据库操作接口层
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:50:43
 */
@FunctionalInterface
public interface IDBOption {

	/**
	 * 操作
	 *
	 * @param destination 指令
	 * @param schemaName  实例名称
	 * @param tableName   表名称
	 * @param rowChange   数据
	 * @since 2021-09-03 20:50:48
	 */
	void doOption(String destination, String schemaName, String tableName,
		CanalEntry.RowChange rowChange);
}
