
package com.taotao.cloud.promotion.api.web.vo;

import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 促销活动基础类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotionsVO implements Serializable {

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

	/**
	 * @return 促销状态
	 * @see PromotionsStatusEnum
	 */
	public String getPromotionStatus() {
		if (endTime == null) {
			return startTime != null ? PromotionsStatusEnum.START.name()
				: PromotionsStatusEnum.CLOSE.name();
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
