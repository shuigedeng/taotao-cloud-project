package com.taotao.cloud.order.biz.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.api.enums.cart.DeliveryMethodEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * 交易表
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Trade.TABLE_NAME)
@TableName(Trade.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Trade.TABLE_NAME, comment = "交易表")
public class Trade extends BaseSuperEntity<Trade, Long> {

	public static final String TABLE_NAME = "li_trade";

	private static final long serialVersionUID = 5177608752643561827L;

	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "交易编号")
	private String sn;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "买家id")
	private String memberId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "买家用户名")
	private String memberName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "支付方式")
	private String paymentMethod;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	/**
	 * @see PayStatusEnum
	 */
	@Schema(description = "付款状态")
	private String payStatus;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "总价格")
	private BigDecimal flowPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "原价")
	private BigDecimal goodsPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "运费")
	private BigDecimal freightPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "优惠的金额")
	private BigDecimal discountPrice;

	/**
	 * @see DeliveryMethodEnum
	 */
	@Schema(description = "配送方式")
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	private String deliveryMethod;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "收货人姓名")
	private String consigneeName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "收件人手机")
	private String consigneeMobile;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "地址名称， '，'分割")
	private String consigneeAddressPath;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
	@Schema(description = "地址id，'，'分割 ")
	private String consigneeAddressIdPath;

	//public Trade(TradeDTO tradeDTO) {
	//    String originId = this.getId();
	//    if (tradeDTO.getMemberAddress() != null) {
	//        BeanUtil.copyProperties(tradeDTO.getMemberAddress(), this);
	//        this.setConsigneeMobile(tradeDTO.getMemberAddress().getMobile());
	//        this.setConsigneeName(tradeDTO.getMemberAddress().getName());
	//    }
	//    BeanUtil.copyProperties(tradeDTO, this);
	//    BeanUtil.copyProperties(tradeDTO.getPriceDetailDTO(), this);
	//    this.setId(originId);
	//}
}
