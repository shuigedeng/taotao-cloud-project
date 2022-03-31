package com.taotao.cloud.order.biz.entity.aftersale;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	 * 应用ID
	 */
	@Schema(description =  "售后服务单号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String sn;

	/**
	 * 应用ID
	 */
	@Schema(description =  "操作者id(可以是卖家)")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String operatorId;

	/**
	 * @see UserEnums
	 */
	@Schema(description =  "操作者类型")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String operatorType;

	/**
	 * 应用ID
	 */
	@Schema(description =  "操作者名称")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String operatorName;
	/**
	 * 应用ID
	 */
	@Schema(description =  "日志信息")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String message;
}
