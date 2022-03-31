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
package com.taotao.cloud.bulletin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 上午9:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "提现申请VO")
public class BulletinVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(name = "id")
	private Long id;

	@Schema(description = "申请单号")
	private String code;

	@Schema(description = "公司ID")
	private Long companyId;

	@Schema(description = "商城ID")
	private Long mallId;

	@Schema(description = "提现金额")
	private BigDecimal amount;

	@Schema(description = "钱包余额")
	private BigDecimal balanceAmount;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
