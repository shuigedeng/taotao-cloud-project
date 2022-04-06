package com.taotao.cloud.goods.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.goods.api.vo.CategoryBrandVO;
import com.taotao.cloud.goods.biz.entity.CategoryBrand;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品分类品牌数据处理层
 */
public interface CategoryBrandMapper extends BaseMapper<CategoryBrand> {

	/**
	 * 根据分类id查分类绑定品牌
	 *
	 * @param categoryId 分类id
	 * @return 分类绑定的品牌列表
	 */
	@Select("""
		SELECT b.id,b.name 
		FROM li_brand b INNER join li_category_brand cb on b.id = cb.brand_id and cb.category_id = #{categoryId}
		where b.delete_flag = 0
		""")
	List<CategoryBrandVO> getCategoryBrandList(@Param(value = "categoryId") String categoryId);
}
