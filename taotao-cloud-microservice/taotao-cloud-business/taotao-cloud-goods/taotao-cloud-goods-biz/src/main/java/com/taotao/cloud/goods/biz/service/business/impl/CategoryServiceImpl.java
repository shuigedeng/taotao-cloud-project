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

package com.taotao.cloud.goods.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.cloud.goods.biz.model.vo.CategoryTreeVO;
import com.taotao.cloud.goods.biz.mapper.ICategoryMapper;
import com.taotao.cloud.goods.biz.model.convert.CategoryConvert;
import com.taotao.cloud.goods.biz.model.entity.Category;
import com.taotao.cloud.goods.biz.repository.CategorytRepository;
import com.taotao.cloud.goods.biz.repository.ICategoryRepository;
import com.taotao.cloud.goods.biz.service.business.*;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import lombok.*;
import org.dromara.hutool.core.text.CharSequenceUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.taotao.boot.common.enums.CachePrefixEnum.CATEGORY;
import static com.taotao.boot.common.enums.CachePrefixEnum.CATEGORY_ARRAY;

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
public class CategoryServiceImpl extends BaseSuperServiceImpl<Category, Long, ICategoryMapper, CategorytRepository, ICategoryRepository>
	implements ICategoryService {

	private static final String DELETE_FLAG_COLUMN = "delete_flag";

	private final RedisRepository redisRepository;
	/**
	 * 商品品牌业务层
	 */
	private final IBrandService brandService;
	/**
	 * 分类品牌服务
	 */
	private final ICategoryBrandService categoryBrandService;
	/**
	 * 分类绑定参数服务
	 */
	private final ICategoryParameterGroupService categoryParameterGroupService;
	/**
	 * 分类规格服务
	 */
	private final ICategorySpecificationService categorySpecificationService;

	@Override
	public List<Category> childrenList(Long parentId) {
		LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Category::getParentId, parentId);
		return this.list(wrapper);
	}

	@Override
	@Cacheable(key = "#id")
	public Category getCategoryById(Long id) {
		return this.getById(id);
	}

	@Override
	public List<Category> listByIdsOrderByLevel(List<Long> ids) {
		LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(Category::getId, ids);
		wrapper.orderByAsc(Category::getLevel);
		return this.list(wrapper);
	}

	@Override
	public List<Category> dbList(String parentId) {
		return null;
	}

	@Override
	public Category getCategoryById(String id) {
		return null;
	}

	@Override
	public List<Map<String, Object>> listMapsByIdsOrderByLevel(List<String> ids, String columns) {
		return null;
	}

	@Override
	public List<CategoryTreeVO> categoryTree() {
		// 获取缓存数据
		List<CategoryTreeVO> categoryTreeVOList = redisRepository.lGet(
			CATEGORY.getPrefix(), 0L, redisRepository.lGetListSize(CATEGORY.getPrefix()));
		if (categoryTreeVOList != null) {
			return categoryTreeVOList;
		}

		// 获取全部分类
		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Category::getDelFlag, false);
		List<Category> list = this.list(queryWrapper);

		// 构造分类树
		categoryTreeVOList = new ArrayList<>();
		for (Category category : list) {
			if (Long.valueOf(0).equals(category.getParentId())) {
				CategoryTreeVO categoryTreeVO = CategoryConvert.INSTANCE.convert(category);
				categoryTreeVO.setParentTitle(category.getName());
				categoryTreeVO.setChildren(findChildren(list, categoryTreeVO));
				categoryTreeVOList.add(categoryTreeVO);
			}
		}

		categoryTreeVOList.sort(Comparator.comparing(CategoryTreeVO::getSortOrder));

		if (!categoryTreeVOList.isEmpty()) {
			redisRepository.lSet(CATEGORY.getPrefix(), categoryTreeVOList);
			redisRepository.lSet(CATEGORY_ARRAY.getPrefix(), list);
		}
		return categoryTreeVOList;
	}

	@Override
	public List<CategoryTreeVO> getStoreCategory(String[] categories) {
		List<String> arr = Arrays.asList(categories.clone());
		return categoryTree().stream()
			.filter(item -> arr.contains(item.getId()))
			.toList();
	}

	@Override
	public List<Category> firstCategory() {
		QueryWrapper<Category> queryWrapper = Wrappers.query();
		queryWrapper.eq("level", 0);
		return list(queryWrapper);
	}

	@Override
	public List<CategoryTreeVO> listAllChildren(Long parentId) {
		if (Long.valueOf(0).equals(parentId)) {
			return categoryTree();
		}

		// 循环代码，找到对象，把他的子分类返回
		List<CategoryTreeVO> topCatList = categoryTree();
		for (CategoryTreeVO item : topCatList) {
			if (item.getId().equals(parentId)) {
				return item.getChildren();
			} else {
				return getChildren(parentId, item.getChildren());
			}
		}
		return new ArrayList<>();
	}

	@Override
	public List<CategoryTreeVO> listAllChildren() {
		// 获取全部分类
		List<Category> list = this.list();

		// 构造分类树
		List<CategoryTreeVO> categoryTreeVOList = new ArrayList<>();
		for (Category category : list) {
			if (Long.valueOf(0).equals(category.getParentId())) {
				// CategoryVO categoryVO = new CategoryVO(category);
				CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
				categoryTreeVO.setChildren(findChildren(list, categoryTreeVO));
				categoryTreeVOList.add(categoryTreeVO);
			}
		}
		categoryTreeVOList.sort(Comparator.comparing(CategoryTreeVO::getSortOrder));
		return categoryTreeVOList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getCategoryNameByIds(List<Long> ids) {
		List<String> categoryName = new ArrayList<>();
		List<Category> categoryVOList = (List<Category>) redisRepository.get(CATEGORY_ARRAY.getPrefix());
		// 如果缓存中为空，则重新获取缓存
		if (categoryVOList == null) {
			categoryTree();
			categoryVOList = (List<Category>) redisRepository.get(CATEGORY_ARRAY.getPrefix());
		}

		// 还为空的话，直接返回
		if (categoryVOList == null) {
			return new ArrayList<>();
		}

		// 循环顶级分类
		for (Category category : categoryVOList) {
			// 循环查询的id匹配
			for (Long id : ids) {
				if (category.getId().equals(id)) {
					// 写入商品分类
					categoryName.add(category.getName());
				}
			}
		}
		return categoryName;
	}

	@Override
	public List<Category> findByAllBySortOrder(Category category) {
		QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
		queryWrapper
			.eq(category.getLevel() != null, "level", category.getLevel())
			.eq(CharSequenceUtil.isNotBlank(category.getName()), "name", category.getName())
			.eq(category.getParentId() != null, "parent_id", category.getParentId())
			.ne(category.getId() != null, "id", category.getId())
			.eq(DELETE_FLAG_COLUMN, false)
			.orderByAsc("sort_order");
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveCategory(Category category) {
		// 判断分类佣金是否正确
		if (category.getCommissionRate().compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ResultEnum.CATEGORY_COMMISSION_RATE_ERROR);
		}

		// 子分类与父分类的状态一致
		if (category.getParentId() != null && !Long.valueOf(0).equals(category.getParentId())) {
			Category parentCategory = this.getById(category.getParentId());
			category.setDelFlag(parentCategory.getDelFlag());
		}
		this.save(category);
		removeCache();
		return true;
	}

	@Override
	@CacheEvict(key = "#category.id")
	@Transactional(rollbackFor = Exception.class)
	public boolean updateCategory(Category category) {
		// 判断分类佣金是否正确
		if (category.getCommissionRate().compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ResultEnum.CATEGORY_COMMISSION_RATE_ERROR);
		}

		// 判断父分类与子分类的状态是否一致
		if (category.getParentId() != null && !Long.valueOf(0).equals(category.getParentId())) {
			Category parentCategory = this.getById(category.getParentId());
			if (!parentCategory.getDelFlag().equals(category.getDelFlag())) {
				throw new BusinessException(ResultEnum.CATEGORY_DELETE_FLAG_ERROR);
			}
		}

		UpdateWrapper<Category> updateWrapper = new UpdateWrapper<>();
		updateWrapper
			.eq("id", category.getId())
			.set("name", category.getName())
			.set("image", category.getImage())
			.set("sort_order", category.getSortOrder())
			.set(DELETE_FLAG_COLUMN, category.getDelFlag())
			.set("commission_rate", category.getCommissionRate());
		this.baseMapper.update(category, updateWrapper);
		removeCache();
		return true;
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public void updateCategoryStatus(String categoryId, boolean enableOperations) {

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delete(Long id) {
		this.removeById(id);
		removeCache();

		// 删除关联关系
		categoryParameterGroupService.deleteByCategoryId(id);
		categorySpecificationService.deleteByCategoryId(id);
		return categoryBrandService.deleteByCategoryId(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateCategoryStatus(Long categoryId, boolean enableOperations) {
		// 禁用子分类
		Category category = this.getById(categoryId);
		CategoryTreeVO categoryTreeVO = BeanUtils.copy(category, CategoryTreeVO.class);
		List<Long> ids = new ArrayList<>();

		assert categoryTreeVO != null;

		ids.add(categoryTreeVO.getId());
		this.findAllChild(categoryTreeVO);
		this.findAllChildIds(categoryTreeVO, ids);
		LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.in(Category::getId, ids);
		updateWrapper.set(Category::getDelFlag, enableOperations);
		this.update(updateWrapper);
		removeCache();

		return true;
	}

	/**
	 * 递归树形VO
	 *
	 * @param categories     分类列表
	 * @param categoryTreeVO 分类VO
	 * @return 分类VO列表
	 */
	private List<CategoryTreeVO> findChildren(List<Category> categories, CategoryTreeVO categoryTreeVO) {
		List<CategoryTreeVO> children = new ArrayList<>();
		categories.forEach(item -> {
			if (item.getParentId().equals(categoryTreeVO.getId())) {
				CategoryTreeVO temp = CategoryConvert.INSTANCE.convert(item);
				temp.setParentTitle(item.getName());
				temp.setChildren(findChildren(categories, temp));
				children.add(temp);
			}
		});

		return children;
	}

	/**
	 * 条件查询分类
	 *
	 * @param category 分类VO
	 */
	private void findAllChild(CategoryTreeVO category) {
		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Category::getParentId, category.getId());
		List<Category> categories = this.list(queryWrapper);
		List<CategoryTreeVO> categoryTreeVOList = new ArrayList<>();
		for (Category category1 : categories) {
			categoryTreeVOList.add(BeanUtils.copy(category1, CategoryTreeVO.class));
		}
		category.setChildren(categoryTreeVOList);
		if (!categoryTreeVOList.isEmpty()) {
			categoryTreeVOList.forEach(this::findAllChild);
		}
	}

	/**
	 * 获取所有的子分类ID
	 *
	 * @param category 分类
	 * @param ids      ID列表
	 */
	private void findAllChildIds(CategoryTreeVO category, List<Long> ids) {
		if (category.getChildren() != null && !category.getChildren().isEmpty()) {
			for (CategoryTreeVO child : category.getChildren()) {
				ids.add(child.getId());
				this.findAllChildIds(child, ids);
			}
		}
	}

	/**
	 * 递归自身，找到id等于parentId的对象，获取他的children 返回
	 *
	 * @param parentId           父ID
	 * @param categoryTreeVOList 分类VO
	 * @return 子分类列表VO
	 */
	private List<CategoryTreeVO> getChildren(Long parentId, List<CategoryTreeVO> categoryTreeVOList) {
		for (CategoryTreeVO item : categoryTreeVOList) {
			if (item.getId().equals(parentId)) {
				return item.getChildren();
			}

			if (item.getChildren() != null && !item.getChildren().isEmpty()) {
				return getChildren(parentId, categoryTreeVOList);
			}
		}
		return categoryTreeVOList;
	}

	/**
	 * 清除缓存
	 */
	private void removeCache() {
		redisRepository.del(CATEGORY.getPrefix());
	}
}
