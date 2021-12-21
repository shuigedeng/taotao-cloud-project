package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 评价数量VO
 *
 * 
 * @since 2021/1/27 10:41 上午
 */
@Schema(description = "评价数量VO")
public class EvaluationNumberVO {

	@Schema(description = "全部商品")
	private Integer all;

	@Schema(description = "好评数量")
	private Integer good;

	@Schema(description = "中评数量")
	private Integer moderate;

	@Schema(description = "差评数量")
	private Integer worse;

	@Schema(description = "有图数量")
	private Long haveImage;

	public Integer getAll() {
		return all;
	}

	public void setAll(Integer all) {
		this.all = all;
	}

	public Integer getGood() {
		return good;
	}

	public void setGood(Integer good) {
		this.good = good;
	}

	public Integer getModerate() {
		return moderate;
	}

	public void setModerate(Integer moderate) {
		this.moderate = moderate;
	}

	public Integer getWorse() {
		return worse;
	}

	public void setWorse(Integer worse) {
		this.worse = worse;
	}

	public Long getHaveImage() {
		return haveImage;
	}

	public void setHaveImage(Long haveImage) {
		this.haveImage = haveImage;
	}
}
