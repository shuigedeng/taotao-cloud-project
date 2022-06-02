package com.taotao.cloud.operation.api.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 商品查询条件
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePageQuery extends PageParam {

	@Schema(description = "文章分类ID")
	private String categoryId;

	@Schema(description = "标题")
	private String title;

	@Schema(description = "分类类型")
	private String type;

}
