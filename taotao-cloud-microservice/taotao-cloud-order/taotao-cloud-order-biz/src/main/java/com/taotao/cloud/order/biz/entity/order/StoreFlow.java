package com.taotao.cloud.order.biz.entity.order;

import cn.lili.modules.order.order.entity.enums.FlowTypeEnum;
import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 商家订单流水
 *
 * 
 * @since 2020/11/17 7:31 下午
 */
@Data
@TableName("li_store_flow")
@ApiModel(value = "商家订单流水")
public class StoreFlow extends BaseIdEntity {

    private static final long serialVersionUID = -5998757398902747939L;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "流水编号")
    private String sn;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "订单sn")
    private String orderSn;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "子订单sn")
    private String orderItemSn;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "售后SN")
    private String refundSn;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "店铺id")
    private String storeId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "店铺名称 ")
    private String storeName;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "会员id")
    private String memberId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "会员名称")
    private String memberName;

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "商品ID")
    private String goodsId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "货品ID")
    private String skuId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "图片")
    private String image;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "分类ID")
    private String categoryId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "规格json")
    private String specs;

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    /**
     * @see FlowTypeEnum
     */
    @ApiModelProperty(value = "流水类型：PAY/REFUND 支付/退款", allowableValues = "PAY,REFUND")
    private String flowType;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    /**
     * @see cn.lili.modules.order.order.entity.enums.OrderPromotionTypeEnum
     */
    @ApiModelProperty(value = "订单促销类型")
    private String orderPromotionType;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "积分活动商品结算价格")
    private Double pointSettlementPrice;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "砍价活动商品结算价格")
    private Double kanjiaSettlementPrice;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "平台优惠券 使用金额")
    private Double siteCouponPrice;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "站点优惠券佣金比例")
    private Double siteCouponPoint;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "站点优惠券佣金")
    private Double siteCouponCommission;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "单品分销返现支出")
    private Double distributionRebate;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "平台收取交易佣金")
    private Double commissionPrice;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "流水金额")
    private Double finalPrice;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "最终结算金额")
    private Double billPrice;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "第三方交易流水号")
    private String transactionId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "支付方式名称")
    private String paymentName;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "销售量")
    private Integer num;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;
}
