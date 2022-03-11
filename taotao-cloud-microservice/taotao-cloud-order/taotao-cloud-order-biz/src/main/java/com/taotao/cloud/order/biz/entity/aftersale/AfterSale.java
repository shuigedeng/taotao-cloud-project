package com.taotao.cloud.order.biz.entity.aftersale;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 售后
 *
 * 
 * @since 2020/11/17 7:30 下午
 */
@Entity
@Table(name = AfterSale.TABLE_NAME)
@TableName(AfterSale.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = AfterSale.TABLE_NAME, comment = "售后")
public class AfterSale extends BaseSuperEntity<AfterSale, Long> {

	public static final String TABLE_NAME = "li_after_sale";

    //基础信息

    @ApiModelProperty(value = "售后服务单号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String sn;

    @ApiModelProperty(value = "订单编号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String orderSn;

    @ApiModelProperty(value = "订单货物编号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String orderItemSn;

    @ApiModelProperty(value = "交易编号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String tradeSn;

    @ApiModelProperty(value = "会员ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String memberId;

    @ApiModelProperty(value = "会员名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String memberName;

    @ApiModelProperty(value = "商家ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeId;

    @ApiModelProperty(value = "商家名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeName;

    //商品信息

    @ApiModelProperty(value = "商品ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsId;
    @ApiModelProperty(value = "货品ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String skuId;
    @ApiModelProperty(value = "申请数量")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer num;
    @ApiModelProperty(value = "商品图片")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsImage;
    @ApiModelProperty(value = "商品名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsName;

    @ApiModelProperty(value = "规格json")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String specs;
    @ApiModelProperty(value = "实际金额")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double flowPrice;


    //交涉信息

    @ApiModelProperty(value = "申请原因")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String reason;

    @ApiModelProperty(value = "问题描述")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String problemDesc;

    @ApiModelProperty(value = "评价图片")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String afterSaleImage;

    /**
     * @see cn.lili.modules.order.trade.entity.enums.AfterSaleTypeEnum
     */
    @ApiModelProperty(value = "售后类型", allowableValues = "RETURN_GOODS,RETURN_MONEY")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String serviceType;

    /**
     * @see cn.lili.modules.order.trade.entity.enums.AfterSaleStatusEnum
     */
    @ApiModelProperty(value = "售后单状态", allowableValues = "APPLY,PASS,REFUSE,BUYER_RETURN,SELLER_RE_DELIVERY,BUYER_CONFIRM,SELLER_CONFIRM,COMPLETE")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String serviceStatus;

    //退款信息

    /**
     * @see cn.lili.modules.order.trade.entity.enums.AfterSaleRefundWayEnum
     */
    @ApiModelProperty(value = "退款方式", allowableValues = "ORIGINAL,OFFLINE")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String refundWay;

    @ApiModelProperty(value = "账号类型", allowableValues = "ALIPAY,WECHATPAY,BANKTRANSFER")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String accountType;

    @ApiModelProperty(value = "银行账户")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String bankAccountNumber;

    @ApiModelProperty(value = "银行开户名")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String bankAccountName;

    @ApiModelProperty(value = "银行开户行")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String bankDepositName;

    @ApiModelProperty(value = "商家备注")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String auditRemark;

    @ApiModelProperty(value = "订单支付方式返回的交易号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String payOrderNo;

    @ApiModelProperty(value = "申请退款金额")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double applyRefundPrice;

    @ApiModelProperty(value = "实际退款金额")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double actualRefundPrice;

    @ApiModelProperty(value = "退还积分")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer refundPoint;

    @ApiModelProperty(value = "退款时间")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Date refundTime;

    /**
     * 买家物流信息
     */
    @ApiModelProperty(value = "发货单号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String mLogisticsNo;

    @ApiModelProperty(value = "物流公司CODE")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String mLogisticsCode;

    @ApiModelProperty(value = "物流公司名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String mLogisticsName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "买家发货时间")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Date mDeliverTime;

}
