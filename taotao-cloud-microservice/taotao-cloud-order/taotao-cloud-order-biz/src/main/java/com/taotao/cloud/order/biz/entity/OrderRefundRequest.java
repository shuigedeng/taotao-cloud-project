package com.taotao.cloud.order.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 售后申请表
 *
 * @author dengtao
 * @date 2020/4/30 15:51
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Table(name = "tt_order_refund_request")
@org.hibernate.annotations.Table(appliesTo = "tt_order_refund_request", comment = "售后申请表")
public class OrderRefundRequest extends BaseEntity {

    /**
     * 退款编号
     */
    private String refundCode;

    /**
     * 售后类型 1, 仅退款;0, 退货退款
     */
    private Short refundType;

    /**
     * 用户物流收货类型：未收到货--3;已收到货--4
     */
    private Short receiveStatus;

    /**
     * 主状态 10, 关闭;20, 处理中;30, 退款成功
     */
    private Short mainStatus;

    /**
     * 子状态
     */
    private Short childStatus;

    /**
     * 用户退货状态 0, 无需退货,10, 未退货，20, 已退货
     */
    private Short userReturnGoodsStatus;

    /**
     * 申请原因
     */
    private String refundCause;

    /**
     * 退款说明
     */
    private String refundRemark;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount = new BigDecimal(0);

    /**
     * 申请退款金额
     */
    private BigDecimal reqRefundAmount = new BigDecimal(0);

    /**
     * 实际退款金额--定时任务设置
     */
    private BigDecimal actRefundAmount = new BigDecimal(0);

    /**
     * 买家会员ID
     */
    private Long customerId;

    /**
     * 买家姓名
     */
    private String customerName;

    /**
     * 买家电话
     */
    private String customerPhone;

    /**
     * 收货人名称
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 收货人地区
     */
    private String receiverAddress;

    /**
     * 申请时间
     */
    private LocalDateTime subDate;

    /**
     * 结束关闭时间
     */
    private LocalDateTime endDate;

    /**
     * 管家处理时间
     */
    private LocalDateTime stewardDate;

    /**
     * 用户退货时间
     */
    private LocalDateTime returnGoodsDate;

    /**
     * 退款确认时间
     */
    private LocalDateTime mchHandleDate;

    /**
     * 退款成功时间
     */
    private LocalDateTime returnMoneyDate;

    /**
     * 商品SPU ID
     */
    private Long productSpuId;

    /**
     * 商品SPU名称
     */
    private String productSpuName;

    /**
     * 商品SKU ID
     */
    @ApiModelProperty("商品SKU ID")
    private Long productSkuId;

    /**
     * 商品SKU 规格名称
     */
    private String productSkuName;

    /**
     * 商品单价
     */
    private BigDecimal productPrice = new BigDecimal("0");

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 管家编码
     */
    private Long stewardId;

    /**
     * 管家名称
     */
    private String stewardName;

    /**
     * 管家昵称
     */
    private String stewardNick;

    /**
     * 关闭理由
     */
    private String closeCause;

    /**
     * 关闭说明
     */
    private String closeRemark;

    /**
     * 物流公司编码
     */
    private String expressCode;

    /**
     * 物流快递单号
     */
    private String postId;

    /**
     * 物流说明
     */
    private String expressRemark;

    /**
     * 此售后是否存在撤销 1 -存在撤销 0 -不存在撤销
     */
    private Short hasCancel;

    /**
     * 创建时间
     */
    private LocalDateTime createDate = LocalDateTime.now();

    private String orderCode;

    /**
     * 订单支付流水号
     */
    private String paySeqCode;

    /**
     * 微信订单号.
     */
    private String transactionId;

    /**
     * 商城id
     */
    private Long mallId;

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 场景方id
     */
    private Long sceneId;

    private LocalDateTime collectSceneConfirmDate;

    private Long orgId;

    private Integer orderType;

    private String itemCode;

    private String mainCode;

    private Integer hasApply = 0;

}
