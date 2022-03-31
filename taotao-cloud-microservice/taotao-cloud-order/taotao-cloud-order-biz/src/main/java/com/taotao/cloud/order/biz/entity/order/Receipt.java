package com.taotao.cloud.order.biz.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 发票表
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Receipt.TABLE_NAME)
@TableName(Receipt.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Receipt.TABLE_NAME, comment = "发票表")
public class Receipt extends BaseSuperEntity<Receipt, Long> {

	public static final String TABLE_NAME = "tt_receipt";

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
    private BigDecimal receiptPrice;
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
