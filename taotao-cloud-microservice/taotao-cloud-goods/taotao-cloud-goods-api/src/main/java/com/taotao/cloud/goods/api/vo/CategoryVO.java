package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 分类VO
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO extends CategoryBaseVO {

	private static final long serialVersionUID = 3775766246075838410L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "父节点名称")
	private String parentTitle;

	@Schema(description = "子分类列表")
	private List<CategoryVO> children;

	@Schema(description = "分类关联的品牌列表")
	private List<BrandVO> brandList;

	public List<CategoryVO> getChildren() {
		if (children != null) {
			children.sort(Comparator.comparing(CategoryBaseVO::getSortOrder));
			return children;
		}
		return null;
	}
}
