package com.taotao.cloud.order.biz.entity.purchase;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 报价单字内容
 *
 * 
 * @since 2020/11/26 20:43
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "供求单报价")
@TableName("li_purchase_quoted_item")
public class PurchaseQuotedItem extends BaseIdEntity {
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description =  "创建时间", hidden = true)
    private LocalDateTime createTime;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "报价单ID")
    private String PurchaseQuotedId;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "商品名称")
    private String goodsName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "规格")
    private String specs;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "数量")
    private String num;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "数量单位")
    private String goodsUnit;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "价格")
    private BigDecimal price;
}
