package com.taotao.cloud.generator.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class MemberStatisticsDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "统计日")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date createDate;

	@Schema(description = "当前会员数量")
	private Long memberCount;

	@Schema(description = "新增会员数量")
	private Long newlyAdded;

	@Schema(description = "当日活跃数量")
	private Long activeQuantity;

}
