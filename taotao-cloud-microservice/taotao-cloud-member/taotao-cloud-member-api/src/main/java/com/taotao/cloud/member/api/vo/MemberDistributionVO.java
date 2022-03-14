package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 会员分布VO
 */
@Data
@Schema(description = "会员分布VO")
public class MemberDistributionVO implements Serializable {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "客户端类型")
	private String clientEnum;

	@Schema(description = "数量")
	private Integer num;

	@Schema(description = "比例")
	private BigDecimal proportion;
}
