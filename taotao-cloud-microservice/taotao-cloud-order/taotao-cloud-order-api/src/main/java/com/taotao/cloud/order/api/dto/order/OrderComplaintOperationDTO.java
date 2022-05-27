package com.taotao.cloud.order.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * 交易投诉 参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Schema(description = "交易投诉 参数")
public record OrderComplaintOperationDTO(
	@Schema(description = "要更改的状态状态")
	String complainStatus,

	@Schema(description = "交易投诉主键")
	Long complainId,

	@Schema(description = "商家申诉内容")
	String appealContent,

	@Schema(description = "商家申诉上传的图片")
	List<String> images,

	@Schema(description = "仲裁结果")
	String arbitrationResult
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


}
