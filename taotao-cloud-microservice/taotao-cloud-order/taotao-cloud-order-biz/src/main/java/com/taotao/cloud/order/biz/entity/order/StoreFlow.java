package com.taotao.cloud.order.biz.entity.order;

import cn.lili.modules.order.order.entity.enums.FlowTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 商家订单流水表
 */
@Entity
@Table(name = StoreFlow.TABLE_NAME)
@TableName(StoreFlow.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StoreFlow.TABLE_NAME, comment = "商家订单流水表")
public class StoreFlow extends BaseSuperEntity<StoreFlow, Long> {

	public static final String TABLE_NAME = "tt_store_flow";

	/**
	 * 应用ID
	 */
	private static final long serialVersionUID = -5998757398902747939L;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "流水编号")
	private String sn;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "订单sn")
	private String orderSn;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "子订单sn")
	private String orderItemSn;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "售后SN")
	private String refundSn;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "店铺id")
	private String storeId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "店铺名称 ")
	private String storeName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "会员id")
	private String memberId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "会员名称")
	private String memberName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "商品ID")
	private String goodsId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "商品名称")
	private String goodsName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "货品ID")
	private String skuId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "图片")
	private String image;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "分类ID")
	private String categoryId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "规格json")
	private String specs;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	/**
	 * @see FlowTypeEnum
	 */
	@Schema(description = "流水类型：PAY/REFUND 支付/退款", allowableValues = "PAY,REFUND")
	private String flowType;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	/**
	 * @see OrderPromotionTypeEnum
	 */
	@Schema(description = "订单促销类型")
	private String orderPromotionType;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "积分活动商品结算价格")
	private BigDecimal pointSettlementPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "砍价活动商品结算价格")
	private BigDecimal kanjiaSettlementPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "平台优惠券 使用金额")
	private BigDecimal siteCouponPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "站点优惠券佣金比例")
	private BigDecimal siteCouponPoint;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "站点优惠券佣金")
	private BigDecimal siteCouponCommission;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "单品分销返现支出")
	private BigDecimal distributionRebate;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "平台收取交易佣金")
	private BigDecimal commissionPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "流水金额")
	private BigDecimal finalPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "最终结算金额")
	private Double billPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "第三方交易流水号")
	private String transactionId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "支付方式名称")
	private String paymentName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "销售量")
	private Integer num;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	@CreatedDate
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间", hidden = true)
	private LocalDateTime createTime;
}
