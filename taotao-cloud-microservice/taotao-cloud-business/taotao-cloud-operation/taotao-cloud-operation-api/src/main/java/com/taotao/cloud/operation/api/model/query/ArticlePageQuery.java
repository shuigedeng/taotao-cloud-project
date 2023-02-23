package com.taotao.cloud.operation.api.model.query;

import com.taotao.cloud.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 商品查询条件
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePageQuery extends PageQuery {

	@Schema(description = "文章分类ID")
	private String categoryId;

	@Schema(description = "标题")
	private String title;

	@Schema(description = "分类类型")
	private String type;

}
