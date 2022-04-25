package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import lombok.experimental.SuperBuilder;

/**
 * 分类VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO extends CategoryBaseVO {

	private static final long serialVersionUID = 3775766246075838410L;

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
