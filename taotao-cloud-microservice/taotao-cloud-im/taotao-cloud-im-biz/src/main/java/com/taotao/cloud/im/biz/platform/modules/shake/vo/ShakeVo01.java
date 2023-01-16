package com.taotao.cloud.im.biz.platform.modules.shake.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShakeVo01 {

	/**
	 * 经度
	 */
	@NotNull(message = "经度不能为空")
	private Double longitude;
	/**
	 * 纬度
	 */
	@NotNull(message = "纬度不能为空")
	private Double latitude;

}
