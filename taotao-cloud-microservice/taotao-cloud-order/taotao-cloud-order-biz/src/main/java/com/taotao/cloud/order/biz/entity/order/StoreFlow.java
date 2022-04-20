package com.taotao.cloud.order.biz.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.api.enums.order.FlowTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serial;
import java.math.BigDecimal;

/**
 * 商家订单流水表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = StoreFlow.TABLE_NAME)
@TableName(StoreFlow.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StoreFlow.TABLE_NAME, comment = "商家订单流水表")
public class StoreFlow extends BaseSuperEntity<StoreFlow, Long> {

	public static final String TABLE_NAME = "tt_store_flow";

	@Serial
	private static final long serialVersionUID = -5998757398902747939L;
	/**
	 * 流水编号
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '流水编号'")
	private String sn;
	/**
	 * 订单sn
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '订单sn'")
	private String orderSn;
	/**
	 * 子订单sn
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '子订单sn'")
	private String orderItemSn;
	/**
	 * 售后SN
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '售后SN'")
	private String refundSn;
	/**
	 * 店铺id
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '店铺id'")
	private String storeId;
	/**
	 * 店铺名称
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '店铺名称'")
	private String storeName;
	/**
	 * 会员id
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员id'")
	private String memberId;
	/**
	 * 会员名称
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员名称'")
	private String memberName;
	/**
	 * 商品ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '商品ID'")
	private String goodsId;
	/**
	 * 商品名称
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '商品名称'")
	private String goodsName;
	/**
	 * 货品ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '货品ID'")
	private String skuId;
	/**
	 * 图片
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '图片'")
	private String image;
	/**
	 * 分类ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '分类ID'")
	private String categoryId;
	/**
	 * 规格json
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '规格json'")
	private String specs;
	/**
	 * 流水类型：PAY/REFUND 支付/退款
	 *
	 * @see FlowTypeEnum
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '流水类型：PAY/REFUND 支付/退款'")
	private String flowType;

	/**
	 * 订单促销类型
	 *
	 * @see OrderPromotionTypeEnum
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '订单促销类型'")
	private String orderPromotionType;
	/**
	 * 积分活动商品结算价格
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '积分活动商品结算价格'")
	private BigDecimal pointSettlementPrice;
	/**
	 * 砍价活动商品结算价格
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '砍价活动商品结算价格'")
	private BigDecimal kanjiaSettlementPrice;
	/**
	 * 平台优惠券 使用金额
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '平台优惠券 使用金额'")
	private BigDecimal siteCouponPrice;
	/**
	 * 站点优惠券佣金比例
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '站点优惠券佣金比例'")
	private BigDecimal siteCouponPoint;
	/**
	 * 站点优惠券佣金
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '站点优惠券佣金'")
	private BigDecimal siteCouponCommission;
	/**
	 * 单品分销返现支出
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '单品分销返现支出'")
	private BigDecimal distributionRebate;
	/**
	 * 平台收取交易佣金
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '平台收取交易佣金'")
	private BigDecimal commissionPrice;
	/**
	 * 流水金额
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '流水金额'")
	private BigDecimal finalPrice;
	/**
	 * 最终结算金额
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '最终结算金额'")
	private BigDecimal billPrice;
	/**
	 * 第三方交易流水号
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '第三方交易流水号'")
	private String transactionId;
	/**
	 * 支付方式名称
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '支付方式名称'")
	private String paymentName;
	/**
	 * 销售量
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '销售量'")
	private Integer num;

}
