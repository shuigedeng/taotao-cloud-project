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

package com.taotao.cloud.goods.api.dubbo.response;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serializable;

/**
 * CompanyBO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-19 20:47:05
 */
@RecordBuilder
public record GoodsQueryRpcResponse(
	/* 租户id */
	String tenantId,

	/*
	  租户密钥
	 */
	String tenantSecret,

	/** 公司名称 */
	String name,

	/** 公司类型 */
	Integer type)
	implements Serializable {

	static final long serialVersionUID = 5126530068827085130L;
}
