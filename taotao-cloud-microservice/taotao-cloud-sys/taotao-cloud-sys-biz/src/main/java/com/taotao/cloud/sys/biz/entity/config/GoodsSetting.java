package com.taotao.cloud.sys.biz.entity.config;

import java.io.Serializable;
import lombok.Data;

/**
 * 商品设置
 */
@Data
public class GoodsSetting implements Serializable {

	private static final long serialVersionUID = -4132785717179910025L;

	/**
	 * 是否开启商品审核
	 */
	private Boolean goodsCheck;
	/**
	 * 小图宽
	 */
	private Integer smallPictureWidth;
	/**
	 * 小图高
	 */
	private Integer smallPictureHeight;
	/**
	 * 缩略图宽
	 */
	private Integer abbreviationPictureWidth;
	/**
	 * 缩略图高
	 */
	private Integer abbreviationPictureHeight;
	/**
	 * 原图宽
	 */
	private Integer originalPictureWidth;
	/**
	 * 原图高
	 */
	private Integer originalPictureHeight;

}
