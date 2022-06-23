package com.taotao.cloud.operation.api.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章VO
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVO {

	@Schema(description = "文章ID")
	private String id;

	@Schema(description = "文章标题")
	private String title;

	@Schema(description = "分类名称")
	private String articleCategoryName;

	@Schema(description = "文章排序")
	private Integer sort;

	@Schema(description = "开启状态")
	private Boolean openStatus;
}
