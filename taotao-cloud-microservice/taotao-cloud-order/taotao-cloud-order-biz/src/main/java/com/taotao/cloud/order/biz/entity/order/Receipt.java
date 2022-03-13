package com.taotao.cloud.order.biz.entity.order;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import lombok.Data;


/**
 * 发票
 *
 * 
 * @since 2020/11/28 11:38
 */
@Data
@TableName("li_receipt")
@ApiModel(value = "发票")
public class Receipt extends BaseEntity {

    private static final long serialVersionUID = -8210927482915675995L;
	/**
	 * 应用ID
	 */
    @Schema(description =  "订单编号")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String orderSn;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "发票抬头")
    private String receiptTitle;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "纳税人识别号")
    private String taxpayerId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "发票内容")
    private String receiptContent;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "发票金额")
    private Double receiptPrice;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "会员ID")
    private String memberId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "会员名称")
    private String memberName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "商家ID")
    private String storeId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "商家名称")
    private String storeName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "发票状态 0未开 1已开")
    private Integer receiptStatus;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "发票详情")
    private String receiptDetail;

}
