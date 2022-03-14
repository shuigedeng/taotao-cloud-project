package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类VO
 **/
@Data
public class CategoryVO extends CategoryBaseVO {

	private static final long serialVersionUID = 3775766246075838410L;

	@Schema(description = "id")
	private String id;

	@Schema(description = "父节点名称")
	private String parentTitle;

	@Schema(description = "子分类列表")
	private List<CategoryVO> children;

	@Schema(description = "分类关联的品牌列表")
	private List<BrandVO> brandList;

	//public CategoryVO(Category category) {
	//	BeanUtil.copyProperties(category, this);
	//}
	//
	//public CategoryVO(String id, String createBy, Date createTime, String updateBy, Date updateTime,
	//	Boolean deleteFlag, String name, String parentId, Integer level, BigDecimal sortOrder,
	//	Double commissionRate, String image, Boolean supportChannel) {
	//	super(id, createBy, createTime, updateBy, updateTime, deleteFlag, name, parentId, level,
	//		sortOrder, commissionRate, image, supportChannel);
	//}

	public List<CategoryVO> getChildren() {
		if (children != null) {
			children.sort(Comparator.comparing(CategoryBaseVO::getSortOrder));
			return children;
		}
		return null;
	}
}
