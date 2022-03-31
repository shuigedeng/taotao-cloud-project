package com.taotao.cloud.order.api.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易投诉 参数
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "交易投诉 参数")
public class OrderComplaintOperationParams {


	@Schema(description = "要更改的状态状态")
	private String complainStatus;

	@ApiModelProperty("交易投诉主键")
	private String complainId;

	@ApiModelProperty("商家申诉内容")
	private String appealContent;

	@ApiModelProperty("商家申诉上传的图片")
	private List<String> images;

	@ApiModelProperty("仲裁结果")
	private String arbitrationResult;

}
