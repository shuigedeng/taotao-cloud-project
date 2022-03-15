package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.promotion.api.enums.CouponActivitySendTypeEnum;
import com.taotao.cloud.promotion.api.enums.CouponActivityTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 优惠券活动实体类
 *
 * @since 2020-03-19 10:44 上午
 */
@Entity
@Table(name = CouponActivity.TABLE_NAME)
@TableName(CouponActivity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CouponActivity.TABLE_NAME, comment = "优惠券活动实体类")
public class CouponActivity extends BasePromotions<CouponActivity, Long> {

	public static final String TABLE_NAME = "li_coupon_activity";

	/**
	 * @see CouponActivityTypeEnum
	 */
	@Column(name = "coupon_activity_type", nullable = false, columnDefinition = "varchar(64) not null comment '优惠券活动类型 REGISTERED:新人赠券,SPECIFY：精确发券'")
	private String couponActivityType;

	/**
	 * @see CouponActivitySendTypeEnum
	 */
	@Column(name = "activity_scope", nullable = false, columnDefinition = "varchar(64) not null comment '活动范围 ALL:全部会员,DESIGNATED：指定会员'")
	private String activityScope;

	@Column(name = "activity_scope_info", nullable = false, columnDefinition = "varchar(64) not null comment '活动范围详情,只有精准发券使用'")
	private String activityScopeInfo;

	public String getCouponActivityType() {
		return couponActivityType;
	}

	public void setCouponActivityType(String couponActivityType) {
		this.couponActivityType = couponActivityType;
	}

	public String getActivityScope() {
		return activityScope;
	}

	public void setActivityScope(String activityScope) {
		this.activityScope = activityScope;
	}

	public String getActivityScopeInfo() {
		return activityScopeInfo;
	}

	public void setActivityScopeInfo(String activityScopeInfo) {
		this.activityScopeInfo = activityScopeInfo;
	}
}
