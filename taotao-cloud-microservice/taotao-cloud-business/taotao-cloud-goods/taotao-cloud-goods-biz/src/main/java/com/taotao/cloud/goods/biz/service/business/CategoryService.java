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

package com.taotao.cloud.goods.biz.service.business;

import com.taotao.cloud.goods.biz.model.vo.CategoryTreeVO;
import com.taotao.cloud.goods.biz.model.entity.Category;
import com.taotao.boot.webagg.service.BaseSuperService;

import java.util.List;
import java.util.Map;

/**
 * 商品分类业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:28
 */
public interface CategoryService extends BaseSuperService<Category, Long> {

	List<Category> childrenList(Long parentId);

	Category getCategoryById(Long id);

	boolean delete(Long id);

	boolean updateCategoryStatus(Long categoryId, boolean enableOperations);

	/**
	 * 管理端获取所有分类
	 * 即获取的对象不管是否删除，都要展示，而且不从缓存获取，保证内容是最新的
	 *
	 * @param parentId 分类父ID
	 * @return 商品分类列表
	 */
	List<Category> dbList(String parentId);

	/**
	 * 获取分类
	 *
	 * @param id 分类id
	 * @return com.taotao.cloud.goods.biz.model.entity.Category
	 * @author shuigedeng
	 * @since 2023-12-05 15:19
	 */
	Category getCategoryById(String id);

	/**
	 * 根据分类id集合获取所有分类根据层级排序
	 *
	 * @param ids 分类ID集合
	 * @return 商品分类列表
	 */
	List<Category> listByIdsOrderByLevel(List<Long> ids);

	/**
	 * 根据分类id集合获取所有分类根据层级排序
	 *
	 * @param ids 分类ID集合
	 * @return 商品分类列表
	 */
	List<Map<String, Object>> listMapsByIdsOrderByLevel(List<String> ids, String columns);

	/**
	 * 获取分类树
	 *
	 * @return 分类树
	 */
	List<CategoryTreeVO> categoryTree();

	/**
	 * 查询所有的分类，父子关系
	 *
	 * @param parentId 分类父ID
	 * @return 所有的分类，父子关系
	 */
	List<CategoryTreeVO> listAllChildren(Long parentId);

	/**
	 * 查询所有的分类，父子关系
	 * 数据库获取
	 *
	 * @return 所有的分类，父子关系
	 */
	List<CategoryTreeVO> listAllChildren();

	/**
	 * 获取指定分类的分类名称
	 *
	 * @param ids 指定分类id集合
	 * @return 分类名称集合
	 */
	List<String> getCategoryNameByIds(List<Long> ids);

	/**
	 * 获取商品分类list
	 *
	 * @param category 分类
	 * @return 商品分类list
	 */
	List<Category> findByAllBySortOrder(Category category);

	/**
	 * 添加商品分类
	 *
	 * @param category 商品分类信息
	 * @return 添加结果
	 */
	boolean saveCategory(Category category);

	/**
	 * 修改商品分类
	 *
	 * @param category 商品分类信息
	 * @return 修改结果
	 */
	boolean updateCategory(Category category);

	/**
	 * 批量删除分类
	 *
	 * @param id 分类ID
	 */
	void delete(String id);

	/**
	 * 分类状态的更改
	 *
	 * @param categoryId       商品分类ID
	 * @param enableOperations 是否可用
	 */
	void updateCategoryStatus(String categoryId, boolean enableOperations);

	/**
	 * 获取商家经营类目
	 *
	 * @param categories 经营范围
	 * @return 分类VO列表
	 */
	List<CategoryTreeVO> getStoreCategory(String[] categories);

	/**
	 * 获取一级分类列表
	 * 用于商家入驻选择
	 *
	 * @return 分类列表
	 */
	List<Category> firstCategory();

}
