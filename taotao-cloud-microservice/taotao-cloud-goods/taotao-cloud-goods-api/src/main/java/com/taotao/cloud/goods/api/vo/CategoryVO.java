package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * 分类VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:40
 */
@RecordBuilder
public record CategoryVO(

	@Schema(description = "父节点名称")
	String parentTitle,

	@Schema(description = "子分类列表")
	List<CategoryVO> children,

	@Schema(description = "分类关联的品牌列表")
	List<BrandVO> brandList,

	@Schema(description = "商品分类VO")
	CategoryBaseVO categoryBase

) implements Serializable {

	@Serial
	private static final long serialVersionUID = 3775766246075838410L;

	public List<CategoryVO> getChildren() {
		if (children != null) {
			children.sort(Comparator.comparing((t) -> t.categoryBase.sortOrder()));
			return children;
		}
		return null;
	}
}
