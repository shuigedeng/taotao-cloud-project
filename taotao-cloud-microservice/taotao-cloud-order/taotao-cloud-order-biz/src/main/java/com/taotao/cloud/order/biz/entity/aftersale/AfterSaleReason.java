package com.taotao.cloud.order.biz.entity.aftersale;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 售后原因
 *
 * @since 2021/7/9 1:39 上午
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AfterSaleReason.TABLE_NAME)
@TableName(AfterSaleReason.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = AfterSaleReason.TABLE_NAME, comment = "售后原因")
public class AfterSaleReason extends BaseSuperEntity<AfterSaleReason, Long> {

	public static final String TABLE_NAME = "li_after_sale_reason";

	/**
	 * 售后原因
	 */
	@Column(name = "reason", nullable = false, columnDefinition = "varchar(1024) not null comment '售后原因'")
	private String reason;

	/**
	 * 售后类型
	 *
	 * @see AfterSaleTypeEnum
	 */
	@Column(name = "service_type", nullable = false, columnDefinition = "varchar(64) not null comment '售后类型'")
	private String serviceType;

}
