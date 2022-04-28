package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.biz.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.entity.Specification;

import java.util.List;

/**
 * 商品分类规格业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:40
 */
public interface CategorySpecificationService extends IService<CategorySpecification> {

	/**
	 * 根据分类id查询规格信息
	 *
	 * @param categoryId 分类id
	 * @return {@link List }<{@link Specification }>
	 * @since 2022-04-27 16:59:40
	 */
	List<Specification> getCategorySpecList(Long categoryId);

	/**
	 * 通过分类ID删除关联规格
	 *
	 * @param categoryId 分类ID
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:40
	 */
	Boolean deleteByCategoryId(Long categoryId);
}
