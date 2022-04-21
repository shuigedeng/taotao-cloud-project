/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.elasticsearch.model;


/**
 * 逻辑删除条件对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/3 07:49
 */
public class LogicDelDto {

	/**
	 * 逻辑删除字段名
	 */
	private String logicDelField;
	/**
	 * 逻辑删除字段未删除的值
	 */
	private String logicNotDelValue;

	public String getLogicDelField() {
		return logicDelField;
	}

	public void setLogicDelField(String logicDelField) {
		this.logicDelField = logicDelField;
	}

	public String getLogicNotDelValue() {
		return logicNotDelValue;
	}

	public void setLogicNotDelValue(String logicNotDelValue) {
		this.logicNotDelValue = logicNotDelValue;
	}
}
