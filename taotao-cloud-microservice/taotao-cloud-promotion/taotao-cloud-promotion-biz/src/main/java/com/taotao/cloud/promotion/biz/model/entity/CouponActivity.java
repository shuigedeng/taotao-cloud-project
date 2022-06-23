package com.taotao.cloud.promotion.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.promotion.api.enums.CouponActivitySendTypeEnum;
import com.taotao.cloud.promotion.api.enums.CouponActivityTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 优惠券活动实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:25:09
 */
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = CouponActivity.TABLE_NAME)
@TableName(CouponActivity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CouponActivity.TABLE_NAME, comment = "优惠券活动实体类")
public class CouponActivity extends BasePromotions<CouponActivity, Long> {

	public static final String TABLE_NAME = "tt_coupon_activity";

	/**
	 * 优惠券活动类型 REGISTERED:新人赠券,SPECIFY：精确发券
	 *
	 * @see CouponActivityTypeEnum
	 */
	@Column(name = "coupon_activity_type", columnDefinition = "varchar(255) not null comment '优惠券活动类型 REGISTERED:新人赠券,SPECIFY：精确发券'")
	private String couponActivityType;

	/**
	 * 活动范围 ALL:全部会员,DESIGNATED：指定会员
	 *
	 * @see CouponActivitySendTypeEnum
	 */
	@Column(name = "activity_scope", columnDefinition = "varchar(255) not null comment '活动范围 ALL:全部会员,DESIGNATED：指定会员'")
	private String activityScope;
	/**
	 * 活动范围详情,只有精准发券使用
	 */
	@Column(name = "activity_scope_info", columnDefinition = "varchar(255) not null comment '活动范围详情,只有精准发券使用'")
	private String activityScopeInfo;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
			o)) {
			return false;
		}
		CouponActivity couponActivity = (CouponActivity) o;
		return getId() != null && Objects.equals(getId(), couponActivity.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
