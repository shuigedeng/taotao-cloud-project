package com.taotao.cloud.goods.api.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.soabase.recordbuilder.core.RecordBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 搜索热词
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:39
 */
@RecordBuilder
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotWordsDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@NotBlank(message = "搜索热词不能为空")
	@Size(max = 20, min = 1, message = "搜索热词长度限制在1-20")
	private String keywords;

	@NotNull(message = "分数不能为空")
	@Max(value = 999999L, message = "分数不能大于9999999999")
	@Min(value = -99999L, message = "分数不能小于9999999999")
	private Integer point;
}

