package com.taotao.cloud.sys.api.vo.setting;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 经验值设置
 */
@Data
public class ExperienceSettingVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4261856614779031745L;

	/**
	 * 注册
	 */
	private Integer register;

	/**
	 * 每日签到经验值
	 */
	private Integer signIn;

	/**
	 * 订单评价赠送经验值
	 */
	private Integer comment;

	/**
	 * 分享获取经验值
	 */
	private Integer share;

	/**
	 * 购物获取经验值,一元*经验值
	 */
	private BigDecimal money;

}
