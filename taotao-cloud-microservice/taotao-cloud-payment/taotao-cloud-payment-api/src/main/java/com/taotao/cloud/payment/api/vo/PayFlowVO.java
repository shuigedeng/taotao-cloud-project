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
package com.taotao.cloud.dubbo.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author shuigedeng
 * @since 2020/11/20 上午9:42
 * @version 1.0.0
 */
@Schema(description = "支付流水信息VO")
public class PayFlowVO implements Serializable {
	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description =  "id")
	private Long id;

	@Schema(description =  "支付流水号")
	private String code;

	@Schema(description =  "订单号")
	private String orderCode;

	@Schema(description =  "商品id")
	private Long productId;

	@Schema(description =  "支付金额")
	private BigDecimal paidAmount;

	@Schema(description =  "支付方式")
	private Integer paidMethod;

	@Schema(description =  "购买个数")
	private Integer buyCount;

	@Schema(description =  "支付时间")
	private LocalDateTime payTime;

	@Schema(description =  "创建时间")
	private LocalDateTime createTime;

	@Schema(description =  "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
