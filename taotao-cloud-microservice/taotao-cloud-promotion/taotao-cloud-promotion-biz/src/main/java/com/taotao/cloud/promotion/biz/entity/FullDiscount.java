package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;

/**
 * 满优惠活动实体类
 *
 * @since 2020-03-19 10:44 上午
 */
@Entity
@Table(name = FullDiscount.TABLE_NAME)
@TableName(FullDiscount.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = FullDiscount.TABLE_NAME, comment = "满优惠活动实体类")
public class FullDiscount extends BasePromotions<FullDiscount, Long> {

	public static final String TABLE_NAME = "li_full_discount";

	@DecimalMax(value = "99999999.00", message = "优惠券门槛金额超出限制")
	@Column(name = "full_money", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '优惠门槛金额'")
	private BigDecimal fullMoney;

	@Column(name = "is_full_minus", nullable = false, columnDefinition = "boolean not null default false comment '活动是否减现金'")
	private Boolean isFullMinus;

	@Column(name = "full_minus", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '减现金'")
	private BigDecimal fullMinus;

	@Column(name = "is_full_rate", nullable = false, columnDefinition = "boolean not null default false  comment '是否打折'")
	private Boolean isFullRate;

	@Column(name = "full_rate", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '打折'")
	private BigDecimal fullRate;

	@Column(name = "is_point", nullable = false, columnDefinition = "boolean not null default false comment '是否赠送积分'")
	private Boolean isPoint;

	@Column(name = "point", nullable = false, columnDefinition = "int not null default 0 comment '赠送多少积分'")
	private Integer point;

	@Column(name = "is_free_freight", nullable = false, columnDefinition = "boolean not null default false comment '是否包邮'")
	private Boolean isFreeFreight;

	@Column(name = "is_gift", nullable = false, columnDefinition = "boolean not null default false comment '是否有赠品'")
	private Boolean isGift;

	@Column(name = "gift_id", nullable = false, columnDefinition = "varchar(64) not null comment '赠品id'")
	private String giftId;

	@Column(name = "is_coupon", nullable = false, columnDefinition = "varchar(64) not null comment '是否赠优惠券'")
	private Boolean isCoupon;

	@Column(name = "coupon_id", nullable = false, columnDefinition = "varchar(64) not null comment '优惠券id'")
	private String couponId;

	@Column(name = "title", nullable = false, columnDefinition = "varchar(64) not null comment '活动标题'")
	private String title;

	@Column(name = "description", nullable = false, columnDefinition = "varchar(64) not null comment '活动说明'")
	private String description;

	public BigDecimal getFullMoney() {
		return fullMoney;
	}

	public void setFullMoney(BigDecimal fullMoney) {
		this.fullMoney = fullMoney;
	}

	public Boolean getFullMinus() {
		return isFullMinus;
	}

	public void setFullMinus(BigDecimal fullMinus) {
		this.fullMinus = fullMinus;
	}

	public Boolean getFullRate() {
		return isFullRate;
	}

	public void setFullRate(BigDecimal fullRate) {
		this.fullRate = fullRate;
	}

	public Boolean getPoint() {
		return isPoint;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Boolean getFreeFreight() {
		return isFreeFreight;
	}

	public void setFreeFreight(Boolean freeFreight) {
		isFreeFreight = freeFreight;
	}

	public Boolean getGift() {
		return isGift;
	}

	public void setGift(Boolean gift) {
		isGift = gift;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public Boolean getCoupon() {
		return isCoupon;
	}

	public void setCoupon(Boolean coupon) {
		isCoupon = coupon;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPoint(Boolean point) {
		isPoint = point;
	}

	public void setFullRate(Boolean fullRate) {
		isFullRate = fullRate;
	}

	public void setFullMinus(Boolean fullMinus) {
		isFullMinus = fullMinus;
	}

	public Boolean getIsFullMinus() {
	    if (isFullMinus == null) {
	        return false;
	    }
	    return isFullMinus;
	}

	public Boolean getIsFullRate() {
	    if (isFullRate == null) {
	        return false;
	    }
	    return isFullRate;
	}

	public Boolean getIsPoint() {
	    if (isPoint == null) {
	        return false;
	    }
	    return isPoint;
	}

	public Boolean getIsFreeFreight() {
	    if (isFreeFreight == null) {
	        return false;
	    }
	    return isFreeFreight;
	}

	public Boolean getIsGift() {
	    if (isGift == null) {
	        return false;
	    }
	    return isGift;
	}

	public Boolean getIsCoupon() {
	    if (isCoupon == null) {
	        return false;
	    }
	    return isCoupon;
	}
}
