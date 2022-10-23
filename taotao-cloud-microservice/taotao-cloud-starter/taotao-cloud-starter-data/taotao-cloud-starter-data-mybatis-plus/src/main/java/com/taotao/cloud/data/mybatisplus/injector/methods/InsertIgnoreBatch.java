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

package com.taotao.cloud.data.mybatisplus.injector.methods;


import com.taotao.cloud.data.mybatisplus.injector.MateSqlMethod;

/**
 * 插入一条数据（选择字段插入）插入如果中已经存在相同的记录，则忽略当前新数据
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:41:53
 */
public class InsertIgnoreBatch extends AbstractInsertBatch {

	private static final String SQL_METHOD = "insertIgnoreBatch";

	public InsertIgnoreBatch() {
		super(MateSqlMethod.INSERT_IGNORE_ONE.getSql(), SQL_METHOD);
	}
}
