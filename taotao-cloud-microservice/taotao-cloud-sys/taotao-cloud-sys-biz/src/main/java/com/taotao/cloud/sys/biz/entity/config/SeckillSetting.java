package com.taotao.cloud.sys.biz.entity.config;

import java.io.Serializable;
import lombok.Data;

/**
 * 秒杀活动设置
 */
@Data
public class SeckillSetting implements Serializable {

	/**
	 * 开启几点场 例如：6,8,12
	 */
	private String hours;

	/**
	 * 秒杀规则
	 */
	private String seckillRule;
}
