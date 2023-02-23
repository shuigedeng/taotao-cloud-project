package com.taotao.cloud.goods.api.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索热词
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotWordsDTO {

	@NotBlank(message = "搜索热词不能为空")
	@Size(max = 20, min = 1, message = "搜索热词长度限制在1-20")
	private String keywords;

	@NotNull(message = "分数不能为空")
	@Max(value = 9999999999L, message = "分数不能大于9999999999")
	@Min(value = -9999999999L, message = "分数不能小于9999999999")
	private Integer point;
}

