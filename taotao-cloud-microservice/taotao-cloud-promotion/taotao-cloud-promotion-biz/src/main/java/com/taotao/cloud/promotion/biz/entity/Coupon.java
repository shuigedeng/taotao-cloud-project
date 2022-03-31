package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.enums.CouponRangeDayEnum;
import com.taotao.cloud.promotion.api.enums.CouponTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 优惠券活动实体类
 *
 * @since 2020-03-19 10:44 上午
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Coupon.TABLE_NAME)
@TableName(Coupon.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Coupon.TABLE_NAME, comment = "优惠券活动实体类")
public class Coupon extends BasePromotions<Coupon, Long> {

	public static final String TABLE_NAME = "li_coupon";

	@Column(name = "coupon_name", nullable = false, columnDefinition = "varchar(64) not null comment '优惠券名称'")
	private String couponName;

	/**
	 * POINT("打折"), PRICE("减免现金");
	 *
	 * @see CouponTypeEnum
	 */
	@Column(name = "coupon_type", nullable = false, columnDefinition = "varchar(64) not null comment '优惠券类型'")
	private String couponType;

	@Column(name = "price", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '面额'")
	private BigDecimal price;

	@Column(name = "coupon_discount", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '折扣'")
	private BigDecimal couponDiscount;

	/**
	 * @see CouponGetEnum
	 */
	@Column(name = "get_type", nullable = false, columnDefinition = "varchar(64) not null comment '优惠券类型，分为免费领取和活动赠送'")
	private String getType;

	@Column(name = "store_commission", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '店铺承担比例,平台发布时可以提供一定返点'")
	private BigDecimal storeCommission;

	@Column(name = "description", nullable = false, columnDefinition = "varchar(64) not null comment '活动描述'")
	private String description;

	@Column(name = "publish_num", nullable = false, columnDefinition = "int not null default 0 comment '发行数量,如果是0则是不限制'")
	private Integer publishNum;

	@Column(name = "coupon_limit_num", nullable = false, columnDefinition = "int not null default 0 comment '领取限制'")
	private Integer couponLimitNum;

	@Column(name = "used_num", nullable = false, columnDefinition = "int not null default 0 comment '已被使用的数量'")
	private Integer usedNum;

	@Column(name = "received_num", nullable = false, columnDefinition = "int not null default 0 comment '已被领取的数量'")
	private Integer receivedNum;

	@Column(name = "consume_threshold", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '消费门槛'")
	private BigDecimal consumeThreshold;

	/**
	 * @see CouponRangeDayEnum
	 */
	@Column(name = "range_day_type", nullable = false, columnDefinition = "varchar(64) not null comment '时间范围类型'")
	private String rangeDayType;

	@Column(name = "effective_days", nullable = false, columnDefinition = "int not null defaultt 0  comment '有效期'")
	private Integer effectiveDays;

	//public Coupon(CouponVO couponVO) {
	//    BeanUtils.copyProperties(couponVO, this);
	//}
	//
	///**
	// * @return 促销状态
	// * @see cn.lili.modules.promotion.entity.enums.PromotionsStatusEnum
	// */
	//@Override
	//public String getPromotionStatus() {
	//    if (this.rangeDayType != null && this.rangeDayType.equals(CouponRangeDayEnum.DYNAMICTIME.name())
	//            && (this.effectiveDays != null && this.effectiveDays > 0 && this.effectiveDays <= 365)) {
	//        return PromotionsStatusEnum.START.name();
	//    }
	//    return super.getPromotionStatus();
	//}
}
