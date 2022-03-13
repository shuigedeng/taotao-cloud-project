package com.taotao.cloud.order.biz.entity.aftersale;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 售后原因
 *
 * @since 2021/7/9 1:39 上午
 */
@Entity
@Table(name = AfterSaleReason.TABLE_NAME)
@TableName(AfterSaleReason.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = AfterSaleReason.TABLE_NAME, comment = "售后原因")
public class AfterSaleReason extends BaseSuperEntity<AfterSaleReason, Long> {

	public static final String TABLE_NAME = "li_after_sale_reason";
	/**
	 * 应用ID
	 */
	@NotNull
	@Schema(description =  "售后原因")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String reason;

	/**
	 * @see AfterSaleTypeEnum
	 */
	@Schema(description =  "原因类型", allowableValues = "CANCEL,RETURN_GOODS,RETURN_MONEY,COMPLAIN")
	@NotNull
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String serviceType;

}
