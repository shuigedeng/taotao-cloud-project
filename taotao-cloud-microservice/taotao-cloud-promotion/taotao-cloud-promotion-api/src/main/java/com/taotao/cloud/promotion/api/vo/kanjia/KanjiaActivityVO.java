package com.taotao.cloud.promotion.api.vo.kanjia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 砍价活动参与实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@Schema(description = "砍价活动VO")
public class KanjiaActivityVO extends KanjiaActivityBaseVO {

	@Schema(description = "是否可以砍价")
	private Boolean help;

	@Schema(description = "是否已发起砍价")
	private Boolean launch;

	@Schema(description = "是否可购买")
	private Boolean pass;

	public KanjiaActivityVO() {
		this.setHelp(false);
		this.setLaunch(false);
		this.setPass(false);
	}

}
