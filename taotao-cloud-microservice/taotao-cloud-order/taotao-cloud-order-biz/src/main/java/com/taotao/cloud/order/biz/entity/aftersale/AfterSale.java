package com.taotao.cloud.order.biz.entity.aftersale;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 售后表
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AfterSale.TABLE_NAME)
@TableName(AfterSale.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = AfterSale.TABLE_NAME, comment = "售后表")
public class AfterSale extends BaseSuperEntity<AfterSale, Long> {

	public static final String TABLE_NAME = "tt_after_sale";

	/**
	 * 应用ID
	 */
	@Schema(description =  "售后服务单号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String sn;

	/**
	 * 应用ID
	 */
	@Schema(description =  "订单编号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String orderSn;
	/**
	 * 应用ID
	 */
	@Schema(description =  "订单货物编号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String orderItemSn;
	/**
	 * 应用ID
	 */
	@Schema(description =  "交易编号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String tradeSn;
	/**
	 * 应用ID
	 */
	@Schema(description =  "会员ID")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String memberId;
	/**
	 * 应用ID
	 */
	@Schema(description =  "会员名称")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String memberName;
	/**
	 * 应用ID
	 */
	@Schema(description =  "商家ID")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String storeId;
	/**
	 * 应用ID
	 */
	@Schema(description =  "商家名称")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String storeName;

	//商品信息
	/**
	 * 应用ID
	 */
	@Schema(description =  "商品ID")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String goodsId;
	/**
	 * 应用ID
	 */
	@Schema(description =  "货品ID")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String skuId;
	/**
	 * 应用ID
	 */
	@Schema(description =  "申请数量")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private Integer num;

	/**
	 * 应用ID
	 */
	@Schema(description =  "商品图片")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String goodsImage;
	/**
	 * 应用ID
	 */
	@Schema(description =  "商品名称")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String goodsName;
	/**
	 * 应用ID
	 */
	@Schema(description =  "规格json")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String specs;
	/**
	 * 应用ID
	 */
	@Schema(description =  "实际金额")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private BigDecimal flowPrice;

	//交涉信息
	/**
	 * 应用ID
	 */
	@Schema(description =  "申请原因")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String reason;
	/**
	 * 应用ID
	 */
	@Schema(description =  "问题描述")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String problemDesc;
	/**
	 * 应用ID
	 */
	@Schema(description =  "评价图片")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String afterSaleImage;

	/**
	 * @see AfterSaleTypeEnum
	 */
	@Schema(description =  "售后类型", allowableValues = "RETURN_GOODS,RETURN_MONEY")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String serviceType;

	/**
	 * @see AfterSaleStatusEnum
	 */
	@Schema(description =  "售后单状态", allowableValues = "APPLY,PASS,REFUSE,BUYER_RETURN,SELLER_RE_DELIVERY,BUYER_CONFIRM,SELLER_CONFIRM,COMPLETE")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String serviceStatus;

	//退款信息

	/**
	 * @see AfterSaleRefundWayEnum
	 */
	@Schema(description =  "退款方式", allowableValues = "ORIGINAL,OFFLINE")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String refundWay;
	/**
	 * 应用ID
	 */
	@Schema(description =  "账号类型", allowableValues = "ALIPAY,WECHATPAY,BANKTRANSFER")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String accountType;
	/**
	 * 应用ID
	 */
	@Schema(description =  "银行账户")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String bankAccountNumber;
	/**
	 * 应用ID
	 */
	@Schema(description =  "银行开户名")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String bankAccountName;
	/**
	 * 应用ID
	 */
	@Schema(description =  "银行开户行")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String bankDepositName;
	/**
	 * 应用ID
	 */
	@Schema(description =  "商家备注")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String auditRemark;
	/**
	 * 应用ID
	 */
	@Schema(description =  "订单支付方式返回的交易号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String payOrderNo;
	/**
	 * 应用ID
	 */
	@Schema(description =  "申请退款金额")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private BigDecimal applyRefundPrice;
	/**
	 * 应用ID
	 */
	@Schema(description =  "实际退款金额")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private BigDecimal actualRefundPrice;
	/**
	 * 应用ID
	 */
	@Schema(description =  "退还积分")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private Integer refundPoint;
	/**
	 * 应用ID
	 */
	@Schema(description =  "退款时间")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private LocalDateTime refundTime;

	/**
	 * 买家物流信息
	 */
	/**
	 * 应用ID
	 */
	@Schema(description =  "发货单号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String mLogisticsNo;
	/**
	 * 应用ID
	 */
	@Schema(description =  "物流公司CODE")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String mLogisticsCode;
	/**
	 * 应用ID
	 */
	@Schema(description =  "物流公司名称")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String mLogisticsName;
	/**
	 * 应用ID
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description =  "买家发货时间")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private LocalDateTime mDeliverTime;

}
