package com.taotao.cloud.operation.api.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 文章分类VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCategoryVO extends ArticleCategoryBaseVO {

	@Builder.Default
	@Schema(description = "子菜单")
	private List<ArticleCategoryVO> children = new ArrayList<>();

	public List<ArticleCategoryVO> getChildren() {
		if (children != null) {
			children.sort(Comparator.comparing(ArticleCategoryBaseVO::getSortNum));
			return children;
		}
		return null;
	}
}
