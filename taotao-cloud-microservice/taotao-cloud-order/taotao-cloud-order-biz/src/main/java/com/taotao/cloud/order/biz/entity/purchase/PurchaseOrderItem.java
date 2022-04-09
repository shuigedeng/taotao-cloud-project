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
 * 采购单子内容
 *
 * 
 * @since 2020/11/26 19:32
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("li_purchase_order_item")
@ApiModel(value = "采购单子内容")
public class PurchaseOrderItem extends BaseIdEntity {
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
    @Schema(description =  "采购ID")
    private String purchaseOrderId;
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
    @Schema(description =  "数量")
    private String num;
	/**
	 * 应用ID
	 */
    @Schema(description =  "数量单位")
    private String goodsUnit;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "价格")
    private BigDecimal price;
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
    @Schema(description =  "图片")
    private String images;


}
