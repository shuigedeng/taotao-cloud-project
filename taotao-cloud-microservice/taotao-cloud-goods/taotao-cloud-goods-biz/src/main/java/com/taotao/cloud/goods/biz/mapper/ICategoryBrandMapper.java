package com.taotao.cloud.goods.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.goods.api.model.vo.CategoryBrandVO;
import com.taotao.cloud.goods.biz.model.entity.CategoryBrand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品分类品牌数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:55:23
 */
public interface ICategoryBrandMapper extends BaseMapper<CategoryBrand> {

	/**
	 * 根据分类id查分类绑定品牌
	 *
	 * @param categoryId 分类id
	 * @return {@link List }<{@link CategoryBrandVO }>
	 * @since 2022-04-27 16:55:23
	 */
	@Select("""
		SELECT b.id,b.name,b.logo
		FROM tt_brand b INNER join tt_category_brand cb on b.id = cb.brand_id and cb.category_id = #{categoryId}
		where b.delete_flag = 0
		""")
	List<CategoryBrandVO> getCategoryBrandList(@Param(value = "categoryId") Long categoryId);
}
