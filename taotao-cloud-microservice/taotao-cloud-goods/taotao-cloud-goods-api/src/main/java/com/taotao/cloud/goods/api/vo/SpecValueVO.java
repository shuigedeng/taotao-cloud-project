package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 规格值
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
public record SpecValueVO(

	@Schema(description = "规格项名字")
	String specName,

	@Schema(description = "规格值")
	String specValue,

	@Schema(description = "该规格是否有图片，1 有 0 没有")
	Integer specType,

	@Schema(description = "规格的图片")
	List<SpecImages> specImage
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -4433579132929428572L;

	@Data
	public static class SpecImages implements Serializable {

		static final long serialVersionUID = 1816357809660916086L;

		String url;

		String name;

		String status;

	}
}
