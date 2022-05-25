package com.taotao.cloud.order.api.vo.aftersale;

import com.taotao.cloud.common.enums.UserEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 售后日志
 */
@Data
@Builder
@Schema(description = "售后日志VO")
public class AfterSaleLogVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "id")
	private Long id;

	/**
	 * 售后服务单号
	 */
	@Schema(description = "售后服务单号")
	private String sn;

	/**
	 * 操作者id(可以是卖家)
	 */
	@Schema(description = "操作者id(可以是卖家)")
	private String operatorId;

	/**
	 * 操作者类型
	 *
	 * @see UserEnum
	 */
	@Schema(description = "操作者类型")
	private String operatorType;

	/**
	 * 操作者名称
	 */
	@Schema(description = "操作者名称")
	private String operatorName;

	/**
	 * 日志信息
	 */
	@Schema(description = "日志信息")
	private String message;
}
