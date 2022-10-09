package com.taotao.cloud.goods.biz.mapper;

import com.taotao.cloud.goods.biz.model.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.model.entity.Specification;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品分类规格数据处理层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:55:35
 */
public interface ICategorySpecificationMapper extends BaseSuperMapper<CategorySpecification, Long> {

	/**
	 * 根据分类id查分类绑定规格
	 *
	 * @param categoryId 分类id
	 * @return {@link List }<{@link Specification }>
	 * @since 2022-04-27 16:55:35
	 */
	@Select("""
		select s.*
		from  tt_specification s
		INNER join tt_category_specification cs on s.id = cs.specification_id and cs.category_id = #{categoryId}
		""")
	List<Specification> getCategorySpecList(@Param("categoryId") Long categoryId);
}
