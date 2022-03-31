package com.taotao.cloud.promotion.biz.entity;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 会员优惠券实体类
 *
 * 
 */
@Entity
@Table(name = MemberCoupon.TABLE_NAME)
@TableName(MemberCoupon.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberCoupon.TABLE_NAME, comment = "会员优惠券")
public class MemberCoupon extends BaseSuperEntity<MemberCoupon, Long> {

	public static final String TABLE_NAME = "li_member_coupon";
	
    @Schema(description =  "从哪个模版领取的优惠券")
    private String couponId;

    @Schema(description =  "商家id，如果是平台发送，这个值为 platform")
    private String storeId;

    @Schema(description =  "商家名称，如果是平台，这个值为 platform")
    private String storeName;

    @Schema(description =  "面额")
    private Double price;

    @Schema(description =  "折扣")
    private Double discount;

    @Schema(description =  "消费门槛")
    private Double consumeThreshold;

    @Schema(description =  "会员名称")
    private String memberName;

    @Schema(description =  "会员id")
    private String memberId;

    /**
     * @see PromotionsScopeTypeEnum
     */
    @Schema(description =  "关联范围类型")
    private String scopeType;

    /**
     * POINT("打折"), PRICE("减免现金");
     *
     * @see CouponTypeEnum
     */
    @Schema(description =  "活动类型")
    private String couponType;


    @Schema(description =  "范围关联的id")
    private String scopeId;

    @Schema(description =  "使用起始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description =  "使用截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * @see CouponGetEnum
     */
    @Schema(description =  "优惠券类型，分为免费领取和活动赠送")
    private String getType;

    @Schema(description =  "是否是平台优惠券")
    private Boolean isPlatform;

    @Schema(description =  "店铺承担比例")
    private Double storeCommission;

    @Schema(description =  "核销时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date consumptionTime;

    /**
     * @see MemberCouponStatusEnum
     */
    @Schema(description =  "会员优惠券状态")
    private String memberCouponStatus;

    public MemberCoupon() {
    }

    public MemberCoupon(Coupon coupon) {
        setCouponId(coupon.getId());
        setStoreId(coupon.getStoreId());
        setStoreName(coupon.getStoreName());
        setPrice(coupon.getPrice());
        setDiscount(coupon.getCouponDiscount());
        setConsumeThreshold(coupon.getConsumeThreshold());
        setScopeType(coupon.getScopeType());
        setScopeId(coupon.getScopeId());
        setCouponType(coupon.getCouponType());
        setStartTime(coupon.getStartTime() == null ? new Date() : coupon.getStartTime());

        setGetType(coupon.getGetType());
        setStoreCommission(coupon.getStoreCommission());
        if (coupon.getRangeDayType().equals(CouponRangeDayEnum.FIXEDTIME.name())) {
            setEndTime(coupon.getEndTime());
        } else {
            setEndTime(DateUtil.endOfDay(DateUtil.offset(new DateTime(), DateField.DAY_OF_YEAR, (coupon.getEffectiveDays() - 1))));
        }
    }
}
