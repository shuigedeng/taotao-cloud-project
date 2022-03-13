package com.taotao.cloud.order.biz.entity.order;

import cn.lili.mybatis.BaseEntity;
import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.order.cart.entity.enums.DeliveryMethodEnum;
import cn.lili.modules.order.cart.entity.dto.TradeDTO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 交易
 *
 * 
 * @since 2020/11/17 7:34 下午
 */
@Data
@TableName("li_trade")
@ApiModel(value = "交易")
@NoArgsConstructor
public class Trade extends BaseEntity {

    private static final long serialVersionUID = 5177608752643561827L;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "交易编号")
    private String sn;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "买家id")
    private String memberId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "买家用户名")
    private String memberName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "支付方式")
    private String paymentMethod;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    /**
     * @see cn.lili.modules.order.order.entity.enums.PayStatusEnum
     */
    @Schema(description =  "付款状态")
    private String payStatus;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "总价格")
    private Double flowPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "原价")
    private Double goodsPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "运费")
    private Double freightPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "优惠的金额")
    private Double discountPrice;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    /**
     * @see DeliveryMethodEnum
     */@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "配送方式")
    private String deliveryMethod;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "收货人姓名")
    private String consigneeName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "收件人手机")
    private String consigneeMobile;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "地址名称， '，'分割")
    private String consigneeAddressPath;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "地址id，'，'分割 ")
    private String consigneeAddressIdPath;

    public Trade(TradeDTO tradeDTO) {
        String originId = this.getId();
        if (tradeDTO.getMemberAddress() != null) {
            BeanUtil.copyProperties(tradeDTO.getMemberAddress(), this);
            this.setConsigneeMobile(tradeDTO.getMemberAddress().getMobile());
            this.setConsigneeName(tradeDTO.getMemberAddress().getName());
        }
        BeanUtil.copyProperties(tradeDTO, this);
        BeanUtil.copyProperties(tradeDTO.getPriceDetailDTO(), this);
        this.setId(originId);
    }
}
