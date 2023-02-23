package com.taotao.cloud.goods.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import lombok.experimental.SuperBuilder;

/**
 * 分类VO
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeVO extends CategoryVO {

	private static final long serialVersionUID = 3775766246075838410L;

	@Schema(description = "父节点名称")
	private String parentTitle;

	@Schema(description = "子分类列表")
	private List<CategoryTreeVO> children;

	@Schema(description = "分类关联的品牌列表")
	private List<BrandVO> brandList;

	public List<CategoryTreeVO> getChildren() {
		if (children != null) {
			children.sort(Comparator.comparing(CategoryVO::getSortOrder));
			return children;
		}
		return null;
	}
}
