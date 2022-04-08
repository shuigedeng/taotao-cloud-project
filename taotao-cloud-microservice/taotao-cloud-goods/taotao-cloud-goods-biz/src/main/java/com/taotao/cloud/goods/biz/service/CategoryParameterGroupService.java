package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.api.vo.ParameterGroupVO;
import com.taotao.cloud.goods.biz.entity.CategoryParameterGroup;
import java.util.List;

/**
 * 分类绑定参数组业务层
 */
public interface CategoryParameterGroupService extends IService<CategoryParameterGroup> {

	/**
	 * 查询分类绑定参数集合
	 *
	 * @param categoryId 分类Id
	 * @return 分类参数
	 */
	List<ParameterGroupVO> getCategoryParams(Long categoryId);

	/**
	 * 查询分类绑定参数组信息
	 *
	 * @param categoryId 分类id
	 * @return 参数组列表
	 */
	List<CategoryParameterGroup> getCategoryGroup(Long categoryId);

	/**
	 * 更新分类参数组绑定信息
	 *
	 * @param categoryParameterGroup 分类参数组信息
	 * @return 是否成功
	 */
	Boolean updateCategoryGroup(CategoryParameterGroup categoryParameterGroup);

	/**
	 * 通过分类ID删除关联品牌
	 *
	 * @param categoryId 品牌ID
	 */
	Boolean deleteByCategoryId(Long categoryId);

}
