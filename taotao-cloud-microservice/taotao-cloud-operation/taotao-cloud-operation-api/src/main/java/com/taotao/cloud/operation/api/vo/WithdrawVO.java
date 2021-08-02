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
package com.taotao.cloud.operation.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author shuigedeng
 * @since 2020/11/20 上午9:42
 * @version 1.0.0
 */
@ApiModel(value = "提现申请VO", description = "提现申请VO")
public class WithdrawVO implements Serializable {
	private static final long serialVersionUID = 5126530068827085130L;

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "申请单号")
	private String code;

	@ApiModelProperty(value = "公司ID")
	private Long companyId;

	@ApiModelProperty(value = "商城ID")
	private Long mallId;

	@ApiModelProperty(value = "提现金额")
	private BigDecimal amount;

	@ApiModelProperty(value = "钱包余额")
	private BigDecimal balanceAmount;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	@ApiModelProperty(value = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
