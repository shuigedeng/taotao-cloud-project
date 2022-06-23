package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.api.web.vo.ParameterGroupVO;
import com.taotao.cloud.goods.biz.model.entity.CategoryParameterGroup;

import java.util.List;

/**
 * 分类绑定参数组业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:23
 */
public interface ICategoryParameterGroupService extends IService<CategoryParameterGroup> {

	/**
	 * 查询分类绑定参数集合
	 *
	 * @param categoryId 分类Id
	 * @return {@link List }<{@link ParameterGroupVO }>
	 * @since 2022-04-27 16:59:23
	 */
	List<ParameterGroupVO> getCategoryParams(Long categoryId);

	/**
	 * 查询分类绑定参数组信息
	 *
	 * @param categoryId 分类id
	 * @return {@link List }<{@link CategoryParameterGroup }>
	 * @since 2022-04-27 16:59:23
	 */
	List<CategoryParameterGroup> getCategoryGroup(Long categoryId);

	/**
	 * 更新分类参数组绑定信息
	 *
	 * @param categoryParameterGroup 分类参数组信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:23
	 */
	Boolean updateCategoryGroup(CategoryParameterGroup categoryParameterGroup);

	/**
	 * 通过分类ID删除关联品牌
	 *
	 * @param categoryId 品牌ID
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:23
	 */
	Boolean deleteByCategoryId(Long categoryId);

}
