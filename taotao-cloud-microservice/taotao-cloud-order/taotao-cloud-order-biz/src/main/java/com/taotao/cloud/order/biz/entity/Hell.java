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
package com.taotao.cloud.order.biz.entity;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.Column;

/**
 * Hell
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2021/10/12 22:18
 */
public record Hell(
	@Column(name = "item_code", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '订单子编码'")
	String itemCode,
	@Column(name = "product_spu_id", nullable = false, columnDefinition = "bigint not null comment '商品SPU ID'")
	Long productSpuId) implements Serializable {

	@Serial
	private static final long serialVersionUID = -4603650115461757622L;

}
