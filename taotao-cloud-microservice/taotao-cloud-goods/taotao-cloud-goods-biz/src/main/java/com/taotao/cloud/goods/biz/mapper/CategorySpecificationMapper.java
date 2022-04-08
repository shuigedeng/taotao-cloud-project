package com.taotao.cloud.goods.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.goods.biz.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.entity.Specification;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品分类规格数据处理层
 */
public interface CategorySpecificationMapper extends BaseMapper<CategorySpecification> {

	/**
	 * 根据分类id查分类绑定规格
	 *
	 * @param categoryId 分类id
	 * @return 分类绑定规格列表
	 */
	@Select("""
		select s.*
		from  tt_specification s
		INNER join tt_category_specification cs on s.id = cs.specification_id and cs.category_id = #{categoryId}
		""")
	List<Specification> getCategorySpecList(@Param("categoryId") Long categoryId);
}
