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

package com.taotao.cloud.promotion.api.model.vo;

import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 促销活动基础类
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 09:43:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotionsVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 7814832369110695758L;

	private Long id;
	@Schema(description = "商家名称，如果是平台，这个值为 platform")
	private String storeName;
	@Schema(description = "商家id，如果是平台，这个值为 0")
	private String storeId;
	@NotEmpty(message = "活动名称不能为空")
	@Schema(description = "活动名称", requiredMode = Schema.RequiredMode.REQUIRED)
	private String promotionName;
	@Schema(description = "活动开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
	private LocalDateTime startTime;
	@Schema(description = "活动结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
	private LocalDateTime endTime;

	/**
	 * @see PromotionsScopeTypeEnum PromotionsScopeTypeEnum.PORTION_GOODS.name()
	 */
	private String scopeType;

	@Schema(description = "范围关联的id")
	private String scopeId;

	/**
	 * @return 促销状态
	 * @see PromotionsStatusEnum
	 */
	public String getPromotionStatus() {
		if (endTime == null) {
			return startTime != null ? PromotionsStatusEnum.START.name() : PromotionsStatusEnum.CLOSE.name();
		}
		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(startTime)) {
			return PromotionsStatusEnum.NEW.name();
		} else if (endTime.isBefore(now)) {
			return PromotionsStatusEnum.END.name();
		} else if (now.isBefore(endTime)) {
			return PromotionsStatusEnum.START.name();
		}
		return PromotionsStatusEnum.CLOSE.name();
	}
}
