package com.taotao.cloud.sys.biz.entity.config;

import java.io.Serializable;
import lombok.Data;

/**
 * 经验值设置
 */
@Data
public class ExperienceSetting implements Serializable {

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
	private Integer money;

}
