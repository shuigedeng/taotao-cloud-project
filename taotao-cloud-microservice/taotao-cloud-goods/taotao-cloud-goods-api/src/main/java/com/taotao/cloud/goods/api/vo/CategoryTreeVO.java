package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 分类VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:40
 */
@RecordBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeVO extends CategoryVO {

	@Serial
	private static final long serialVersionUID = 3775766246075838410L;

	@Schema(description = "父节点名称")
	private String parentTitle;

	@Schema(description = "子分类列表")
	private List<CategoryTreeVO> children;

	@Schema(description = "分类关联的品牌列表")
	private List<BrandVO> brands;

	public List<CategoryTreeVO> getChildren() {
		if (children != null) {
			children.sort(Comparator.comparing(CategoryVO::getSortOrder));
			return children;
		}
		return null;
	}
}
