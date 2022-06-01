package com.taotao.cloud.sys.api.vo.setting;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 秒杀活动设置
 */
@Data
public class SeckillSetting implements Serializable {

	@NotNull(message = "活动时间段不能为空 例如：6,8,12")
	private String hours;

	@NotNull(message = "秒杀规则不能为空")
	private String seckillRule;
}
