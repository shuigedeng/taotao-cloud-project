package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 优惠券活动实体类
 *
 * 
 * @since 2020-03-19 10:44 上午
 */
@Entity
@Table(name = CouponActivityItem.TABLE_NAME)
@TableName(CouponActivityItem.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CouponActivityItem.TABLE_NAME, comment = "优惠券活动-优惠券关联实体类")
public class CouponActivityItem extends BaseSuperEntity<CouponActivityItem, Long> {

	public static final String TABLE_NAME = "li_coupon_activity_item";

	@Column(name = "activity_id", nullable = false, columnDefinition = "varchar(64) not null comment '优惠券活动ID'")
	private String activityId;

	@Column(name = "coupon_id", nullable = false, columnDefinition = "varchar(64) not null comment '优惠券ID'")
	private String couponId;

	@Column(name = "num", nullable = false, columnDefinition = "int not null default 0 comment '优惠券数量'")
	private Integer num;


}
