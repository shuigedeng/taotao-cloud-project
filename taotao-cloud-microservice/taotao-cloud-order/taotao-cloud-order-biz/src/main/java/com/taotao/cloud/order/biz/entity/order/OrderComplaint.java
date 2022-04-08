package com.taotao.cloud.order.biz.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.order.api.enums.aftersale.ComplaintStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单交易投诉表
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = OrderComplaint.TABLE_NAME)
@TableName(OrderComplaint.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderComplaint.TABLE_NAME, comment = "订单交易投诉表")
public class OrderComplaint extends BaseSuperEntity<OrderInfo, Long> {

	public static final String TABLE_NAME = "tt_order_complaint";

	private static final long serialVersionUID = 7185050229757228184L;

	/**
	 * 应用ID
	 */
	@Schema(description = "投诉主题")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String complainTopic;
	/**
	 * 应用ID
	 */
	@Schema(description = "投诉内容")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String content;
	/**
	 * 应用ID
	 */
	@Schema(description = "投诉凭证图片")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String images;

	/**
	 * @see ComplaintStatusEnum
	 */
	@Schema(description = "交易投诉状态")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String complainStatus;
	/**
	 * 应用ID
	 */
	@Schema(description = "申诉商家内容")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String appealContent;
	/**
	 * 应用ID
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "申诉商家时间")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private LocalDateTime appealTime;
	/**
	 * 应用ID
	 */
	@Schema(description = "申诉商家上传的图片")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String appealImages;
	/**
	 * 应用ID
	 */
	@Schema(description = "订单号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String orderSn;
	/**
	 * 应用ID
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "下单时间")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private LocalDateTime orderTime;
	/**
	 * 应用ID
	 */
	@Schema(description = "商品名称")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String goodsName;
	/**
	 * 应用ID
	 */
	@Schema(description = "商品id")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private Long goodsId;
	/**
	 * 应用ID
	 */
	@Schema(description = "sku主键")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private Long skuId;
	/**
	 * 应用ID
	 */
	@Schema(description = "商品价格")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private BigDecimal goodsPrice;
	/**
	 * 应用ID
	 */
	@Schema(description = "商品图片")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String goodsImage;
	/**
	 * 应用ID
	 */
	@Schema(description = "购买的商品数量")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private Integer num;
	/**
	 * 应用ID
	 */
	@Schema(description = "运费")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private BigDecimal freightPrice;
	/**
	 * 应用ID
	 */
	@Schema(description = "订单金额")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private BigDecimal orderPrice;
	/**
	 * 应用ID
	 */
	@Schema(description = "物流单号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String logisticsNo;
	/**
	 * 应用ID
	 */
	@Schema(description = "商家id")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private Long storeId;
	/**
	 * 应用ID
	 */
	@Schema(description = "商家名称")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String storeName;
	/**
	 * 应用ID
	 */
	@Schema(description = "会员id")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private Long memberId;
	/**
	 * 应用ID
	 */
	@Schema(description = "会员名称")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String memberName;
	/**
	 * 应用ID
	 */
	@Schema(description = "收货人")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String consigneeName;
	/**
	 * 应用ID
	 */
	@Schema(description = "收货地址")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String consigneeAddressPath;
	/**
	 * 应用ID
	 */
	@Schema(description = "收货人手机")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String consigneeMobile;
	/**
	 * 应用ID
	 */
	@Schema(description = "仲裁结果")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String arbitrationResult;
}
