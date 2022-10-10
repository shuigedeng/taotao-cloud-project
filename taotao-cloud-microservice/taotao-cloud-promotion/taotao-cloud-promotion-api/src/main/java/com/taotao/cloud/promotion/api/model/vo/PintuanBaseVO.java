package com.taotao.cloud.promotion.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * 拼团活动实体类
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PintuanBaseVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 7814832369110695758L;

	public static final String TABLE_NAME = "tt_pintuan";

	@Schema(description = "成团人数")
	private Integer requiredNum;

	@Schema(description = "限购数量")
	private Integer limitNum;

	@Schema(description = "虚拟成团", required = true)
	private Boolean fictitious;

	@Schema(description = "拼团规则")
	private String pintuanRule;

}
