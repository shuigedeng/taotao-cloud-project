package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规格值
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecValueVO implements Serializable {

	private static final long serialVersionUID = -4433579132929428572L;

	@Schema(description = "规格项名字")
	private String specName;

	@Schema(description = "规格值")
	private String specValue;

	@Schema(description = "该规格是否有图片，1 有 0 没有")
	private Integer specType;

	@Schema(description = "规格的图片")
	private List<SpecImages> specImage;

	@Data
	public static class SpecImages implements Serializable {

		private static final long serialVersionUID = 1816357809660916086L;

		private String url;

		private String name;

		private String status;

	}
}
