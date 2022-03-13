package com.taotao.cloud.order.biz.entity.purchase;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import lombok.Data;

import java.util.Date;

/**
 * 供求单
 *
 * 
 * @since 2020-03-14 23:04:56
 */
@Data
@ApiModel(value = "供求单")
@TableName("li_purchase_order")
public class PurchaseOrder extends BaseEntity {
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "标题")
    private String title;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "收货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiptTime;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "价格类型", notes = "可议价、不可议价、面议")
    private String priceMethod;
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
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "是否需要发票")
    private Boolean needReceipt;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "补充说明")
    private String supplement;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "联系类型", notes = "联系方式什么时候可见 公开后、公开")
    private String contactType;

	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "联系人")
    private String contacts;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "联系电话")
    private String contactNumber;

	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "供求人")
    private String memberId;

	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "状态，开启：OPEN，关闭：CLOSE")
    private String status;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "分类ID")
    private String categoryId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "分类名称")
    private String categoryName;

}
