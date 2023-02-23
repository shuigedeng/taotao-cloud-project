package com.taotao.cloud.sys.api.model.vo.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

import lombok.*;

/**
 * 商品配置
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品配置VO")
public class GoodsSettingVO implements Serializable {

	@Serial
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
