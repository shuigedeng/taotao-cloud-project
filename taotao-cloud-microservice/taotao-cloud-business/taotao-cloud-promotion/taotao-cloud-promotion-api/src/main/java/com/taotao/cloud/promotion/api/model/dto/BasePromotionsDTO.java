
package com.taotao.cloud.promotion.api.model.dto;

import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;

/**
 * 促销活动基础类
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotionsDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 7814832369110695758L;

	private Long id;

	private String storeName;

	private Long storeId;

	private String promotionName;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	/**
	 * @see PromotionsScopeTypeEnum PromotionsScopeTypeEnum.PORTION_GOODS.name()
	 */
	private String scopeType ;

	private Long scopeId;
	
}
