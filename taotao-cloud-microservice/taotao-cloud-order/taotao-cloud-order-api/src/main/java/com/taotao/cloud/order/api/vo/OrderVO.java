package com.taotao.cloud.order.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单VO
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "订单VO", description = "订单VO")
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "买家ID")
    private Long memberId;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "秒杀活动id")
    private Long seckillId;

    @ApiModelProperty(value = "订单编码")
    private String code;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime paySuccessTime;

    @ApiModelProperty(value = "订单主状态")
    private Integer mainStatus;

    @ApiModelProperty(value = "订单子状态")
    private Integer childStatus;

    @ApiModelProperty(value = "售后主状态")
    private Integer refundMainStatus;

    @ApiModelProperty(value = "售后子状态")
    private Integer refundChildStatus;

    /**
     * 是否可评价
     * <br/>不可评价 --0
     * <br/>可评价 --1
     * <br/>可追评 --2
     */
    @ApiModelProperty(value = "是否可评价")
    private Integer evaluateStatus;

    @ApiModelProperty(value = "申请售后code")
    private String refundCode;

    @ApiModelProperty(value = "申请售后是否撤销")
    private Boolean hasCancel;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime shipTime;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;

    @ApiModelProperty(value = "交易结束时间")
    private LocalDateTime tradeEndTime;

    @ApiModelProperty(value = "交易结束时间")
    private String receiverName;

    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "收货地址:json的形式存储")
    private String receiverAddressJson;

    @ApiModelProperty(value = "冗余收货地址字符串")
    private String receiverAddress;

    @ApiModelProperty(value = "买家留言")
    private String memberMsg;

    @ApiModelProperty(value = "取消订单说明")
    private String cancelMsg;

    @ApiModelProperty(value = "物流公司code")
    private String expressCode;

    @ApiModelProperty(value = "物流公司名称")
    private String expressName;

    @ApiModelProperty(value = "物流单号")
    private String expressNumber;

    @ApiModelProperty(value = "买家IP")
    private String memberIp;

    @ApiModelProperty(value = "是否结算")
    private Boolean hasSettlement;

    @ApiModelProperty(value = "订单类型")
    private Integer type;

    @ApiModelProperty(value = "条形码")
    private String barCode;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime lastModifiedTime;
}
