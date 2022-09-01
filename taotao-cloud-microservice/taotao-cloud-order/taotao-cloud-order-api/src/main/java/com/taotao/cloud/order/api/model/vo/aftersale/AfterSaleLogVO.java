package com.taotao.cloud.order.api.model.vo.aftersale;

import com.taotao.cloud.common.enums.UserEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 售后日志
 */
@RecordBuilder
@Schema(description = "售后日志VO")
public record AfterSaleLogVO(

	@Schema(description = "id")
	Long id,

	/**
	 * 售后服务单号
	 */
	@Schema(description = "售后服务单号")
	String sn,

	/**
	 * 操作者id(可以是卖家)
	 */
	@Schema(description = "操作者id(可以是卖家)")
	String operatorId,

	/**
	 * 操作者类型
	 *
	 * @see UserEnum
	 */
	@Schema(description = "操作者类型")
	String operatorType,

	/**
	 * 操作者名称
	 */
	@Schema(description = "操作者名称")
	String operatorName,

	/**
	 * 日志信息
	 */
	@Schema(description = "日志信息")
	String message
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

}
