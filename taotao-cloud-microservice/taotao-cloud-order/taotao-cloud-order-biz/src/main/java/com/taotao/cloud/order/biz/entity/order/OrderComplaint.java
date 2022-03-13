package com.taotao.cloud.order.biz.entity.order;

import cn.lili.modules.order.aftersale.entity.enums.ComplaintStatusEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 订单交易投诉
 *
 * 
 * @since 2020/12/4
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_order_complaint")
@ApiModel(value = "订单交易投诉")
public class OrderComplaint extends BaseEntity {

    private static final long serialVersionUID = 7185050229757228184L;

	/**
	 * 应用ID
	 */
    @Schema(description =  "投诉主题")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String complainTopic;
	/**
	 * 应用ID
	 */
    @Schema(description =  "投诉内容")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String content;
	/**
	 * 应用ID
	 */
    @Schema(description =  "投诉凭证图片")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String images;

    /**
     * @see ComplaintStatusEnum
     */
    @Schema(description =  "交易投诉状态")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String complainStatus;
	/**
	 * 应用ID
	 */
    @Schema(description =  "申诉商家内容")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String appealContent;
	/**
	 * 应用ID
	 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "申诉商家时间")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Date appealTime;
	/**
	 * 应用ID
	 */
    @Schema(description =  "申诉商家上传的图片")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String appealImages;
	/**
	 * 应用ID
	 */
    @Schema(description =  "订单号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String orderSn;
	/**
	 * 应用ID
	 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description =  "下单时间")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Date orderTime;
	/**
	 * 应用ID
	 */
    @Schema(description =  "商品名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsName;
	/**
	 * 应用ID
	 */
    @Schema(description =  "商品id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsId;
	/**
	 * 应用ID
	 */
    @Schema(description =  "sku主键")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String skuId;
	/**
	 * 应用ID
	 */
    @Schema(description =  "商品价格")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double goodsPrice;
	/**
	 * 应用ID
	 */
    @Schema(description =  "商品图片")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsImage;
	/**
	 * 应用ID
	 */
    @Schema(description =  "购买的商品数量")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer num;
	/**
	 * 应用ID
	 */
    @Schema(description =  "运费")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double freightPrice;
	/**
	 * 应用ID
	 */
    @Schema(description =  "订单金额")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double orderPrice;
	/**
	 * 应用ID
	 */
    @Schema(description =  "物流单号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String logisticsNo;
	/**
	 * 应用ID
	 */
    @Schema(description =  "商家id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeId;
	/**
	 * 应用ID
	 */
    @Schema(description =  "商家名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeName;
	/**
	 * 应用ID
	 */
    @Schema(description =  "会员id")
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
    @Schema(description =  "收货人")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String consigneeName;
	/**
	 * 应用ID
	 */
    @Schema(description =  "收货地址")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String consigneeAddressPath;
	/**
	 * 应用ID
	 */
    @Schema(description =  "收货人手机")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String consigneeMobile;
	/**
	 * 应用ID
	 */
    @Schema(description =  "仲裁结果")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String arbitrationResult;


}
