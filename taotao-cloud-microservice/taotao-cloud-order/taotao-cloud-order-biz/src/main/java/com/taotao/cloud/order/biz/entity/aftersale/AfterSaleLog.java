package com.taotao.cloud.order.biz.entity.aftersale;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 售后日志
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AfterSaleLog.TABLE_NAME)
@TableName(AfterSaleLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = AfterSaleLog.TABLE_NAME, comment = "售后日志")
public class AfterSaleLog extends BaseSuperEntity<AfterSaleLog, Long> {

	public static final String TABLE_NAME = "tt_after_sale_log";

	/**
	 * 售后服务单号
	 */
	@Column(name = "sn", columnDefinition = "varchar(64) not null comment '售后服务单号'")
	private String sn;

	/**
	 * 操作者id(可以是卖家)
	 */
	@Column(name = "operator_id", columnDefinition = "varchar(64) not null comment '操作者id(可以是卖家)'")
	private Long operatorId;

	/**
	 * 操作者类型
	 *
	 * @see UserEnum
	 */
	@Column(name = "operator_type", columnDefinition = "varchar(64) not null comment '操作者类型'")
	private String operatorType;

	/**
	 * 操作者名称
	 */
	@Column(name = "operator_name", columnDefinition = "varchar(64) not null comment '操作者名称'")
	private String operatorName;

	/**
	 * 日志信息
	 */
	@Column(name = "message", columnDefinition = "varchar(1024) not null comment '日志信息'")
	private String message;
}
