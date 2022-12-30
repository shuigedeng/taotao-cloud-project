package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员分布VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员分布VO")
public class MemberDistributionVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "客户端类型")
	private String clientEnum;

	@Schema(description = "数量")
	private Integer num;

	@Schema(description = "比例")
	private BigDecimal proportion;
}
