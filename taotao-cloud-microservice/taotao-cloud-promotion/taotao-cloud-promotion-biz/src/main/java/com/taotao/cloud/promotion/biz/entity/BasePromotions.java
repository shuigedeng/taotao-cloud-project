
package com.taotao.cloud.promotion.biz.entity;

import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 促销活动基础类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasePromotions<T extends SuperEntity<T, I>, I extends Serializable> extends
	BaseSuperEntity<T, I> {

	@Serial
	private static final long serialVersionUID = 7814832369110695758L;

	@Column(name = "store_name", columnDefinition = "varchar(64) not null comment '商家名称，如果是平台，这个值为 platform'")
	private String storeName;

	@Column(name = "store_id", columnDefinition = "varchar(64) not null comment '商家id，如果是平台，这个值为 platform'")
	private String storeId;

	@Column(name = "promotion_name", columnDefinition = "varchar(64) not null comment '活动名称'")
	private String promotionName;

	@Column(name = "start_time", columnDefinition = "varchar(64) not null comment '活动开始时间'")
	private LocalDateTime startTime;

	@Column(name = "end_time", columnDefinition = "varchar(64) not null comment '活动结束时间'")
	private LocalDateTime endTime;

	/**
	 * @see PromotionsScopeTypeEnum
	 */
	@Column(name = "scope_type", columnDefinition = "varchar(64) not null comment '关联范围类型'")
	private String scopeType = PromotionsScopeTypeEnum.PORTION_GOODS.name();

	@Column(name = "scope_id", columnDefinition = "varchar(64) not null comment '范围关联的id'")
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
