
package com.taotao.cloud.promotion.api.dto;

import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 促销活动基础类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotionsDTO {

	@Serial
	private static final long serialVersionUID = 7814832369110695758L;

	private Long id;

	private String storeName;

	private String storeId;

	private String promotionName;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	/**
	 * @see PromotionsScopeTypeEnum PromotionsScopeTypeEnum.PORTION_GOODS.name()
	 */
	private String scopeType ;

	private String scopeId;
	
}
