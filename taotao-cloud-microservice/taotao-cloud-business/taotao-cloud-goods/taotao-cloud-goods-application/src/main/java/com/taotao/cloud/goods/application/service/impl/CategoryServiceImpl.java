/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.goods.application.service.impl;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.cloud.goods.application.command.category.dto.CategoryAddCmd;
import com.taotao.cloud.goods.application.command.category.dto.CategorySearchQry;
import com.taotao.cloud.goods.application.command.category.dto.CategoryUpdateCmd;
import com.taotao.cloud.goods.application.command.category.dto.clientobject.CategoryTreeCO;
import com.taotao.cloud.goods.application.command.category.executor.query.CategoryChildrenCmdExe;
import com.taotao.cloud.goods.application.command.category.executor.CategoryDelCmdExe;
import com.taotao.cloud.goods.application.command.category.executor.CategorySaveCmdExe;
import com.taotao.cloud.goods.application.command.category.executor.query.CategoryTreeCmdExe;
import com.taotao.cloud.goods.application.command.category.executor.CategoryUpdateCmdExe;
import com.taotao.cloud.goods.application.command.category.executor.query.CategorySearchQryExe;
import com.taotao.cloud.goods.application.service.IBrandService;
import com.taotao.cloud.goods.application.service.ICategoryService;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.ICategoryMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategoryPO;
import com.taotao.cloud.goods.infrastructure.persistent.repository.cls.CategorytRepository;
import com.taotao.cloud.goods.infrastructure.persistent.repository.inf.ICategoryRepository;
import com.taotao.boot.web.base.service.impl.BaseSuperServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * 商品分类业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:09
 */
@AllArgsConstructor
@Service
@CacheConfig(cacheNames = "{category}")
public class CategoryServiceImpl extends
	BaseSuperServiceImpl<CategoryPO, Long, ICategoryMapper, CategorytRepository, ICategoryRepository>
	implements ICategoryService {

	private final RedisRepository redisRepository;
	/**
	 * 商品品牌业务层
	 */
	private final IBrandService brandService;

	private final CategoryTreeCmdExe categoryTreeCmdExe;
	private final CategoryChildrenCmdExe categoryChildrenCmdExe;
	private final CategorySearchQryExe categorySearchQryExe;
	private final CategoryUpdateCmdExe categoryUpdateCmdExe;
	private final CategorySaveCmdExe categorySaveCmdExe;
	private final CategoryDelCmdExe categoryDelCmdExe;

	@Override
	public List<CategoryPO> childrenList(Long parentId) {
		return categoryChildrenCmdExe.childrenList(parentId);
	}

	@Override
	public CategoryPO getCategoryById(Long id) {
		return this.getById(id);
	}

	@Override
	public boolean delete(Long id) {
		return false;
	}

	@Override
	public boolean updateCategoryStatus(Long categoryId, boolean enableOperations) {
		return categoryUpdateCmdExe.updateCategoryStatus(categoryId,enableOperations);
	}

	@Override
	public List<CategoryPO> dbList(String parentId) {
		return List.of();
	}

	@Override
	public CategoryPO getCategoryById(String id) {
		return null;
	}

	@Override
	public List<CategoryPO> listByIdsOrderByLevel(List<Long> ids) {
		return categorySearchQryExe.listByIdsOrderByLevel(ids);
	}

	@Override
	public List<Map<String, Object>> listMapsByIdsOrderByLevel(List<String> ids, String columns) {
		return List.of();
	}

	@Override
	public List<CategoryTreeCO> categoryTree() {
		return categoryTreeCmdExe.categoryTree();
	}

	@Override
	public List<CategoryTreeCO> listAllChildren(Long parentId) {
		return categoryChildrenCmdExe.listAllChildren(parentId);
	}

	@Override
	public List<CategoryTreeCO> listAllChildren() {
		return categoryChildrenCmdExe.listAllChildren();
	}

	@Override
	public List<String> getCategoryNameByIds(List<Long> ids) {
		return categorySearchQryExe.getCategoryNameByIds(ids);
	}

	@Override
	public List<CategoryPO> findByAllBySortOrder(CategorySearchQry category) {
		return categorySearchQryExe.findByAllBySortOrder(categoryPO);
	}

	@Override
	public boolean saveCategory(CategoryAddCmd category) {
		return categorySaveCmdExe.saveCategory(categoryPO);
	}

	@Override
	public boolean updateCategory(CategoryUpdateCmd category) {
		return categoryUpdateCmdExe.updateCategory(categoryPO);
	}

	@Override
	public void delete(String id) {
		categoryDelCmdExe.delete(id);
	}

	@Override
	public void updateCategoryStatus(String categoryId, boolean enableOperations) {
		return categoryUpdateCmdExe.updateCategoryStatus(categoryId,enableOperations);
	}

	@Override
	public List<CategoryTreeCO> getStoreCategory(String[] categories) {
		List<String> arr = Arrays.asList(categories.clone());
		return categoryTree().stream()
			.filter(item -> arr.contains(item.getId()))
			.toList();
	}

	@Override
	public List<CategoryPO> firstCategory() {
		return categorySearchQryExe.firstCategory();
	}
}
