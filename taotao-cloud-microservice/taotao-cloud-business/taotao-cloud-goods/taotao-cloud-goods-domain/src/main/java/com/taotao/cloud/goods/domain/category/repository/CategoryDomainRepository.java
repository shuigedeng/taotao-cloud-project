package com.taotao.cloud.goods.domain.category.repository;

import com.taotao.cloud.goods.domain.category.entity.CategoryEntity;

public interface CategoryDomainRepository {

	/**
	 * 新增部门.
	 *
	 * @param categoryEntity 部门对象
	 */
	void create(CategoryEntity categoryEntity);

	/**
	 * 修改部门.
	 *
	 * @param categoryEntity 部门对象
	 */
	void modify(CategoryEntity categoryEntity);

	/**
	 * 根据ID删除部门.
	 *
	 * @param ids IDS
	 */
	void remove(Long[] ids);
}
