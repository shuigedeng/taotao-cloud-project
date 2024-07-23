package com.taotao.cloud.goods.domain.goods.repository;

import com.taotao.cloud.goods.domain.goods.entity.GoodsEntity;

public interface GoodsDomainRepository {
	/**
	 * 新增部门.
	 *
	 * @param dept 部门对象
	 */
	void create(GoodsEntity dept);

	/**
	 * 修改部门.
	 *
	 * @param dept 部门对象
	 */
	void modify(GoodsEntity dept);

	/**
	 * 根据ID删除部门.
	 *
	 * @param ids IDS
	 */
	void remove(Long[] ids);
}
