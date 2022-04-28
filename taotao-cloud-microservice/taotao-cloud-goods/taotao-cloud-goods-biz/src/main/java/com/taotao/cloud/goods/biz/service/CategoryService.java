package com.taotao.cloud.goods.biz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.api.vo.CategoryVO;
import com.taotao.cloud.goods.biz.entity.Category;

import java.util.List;

/**
 * 商品分类业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:28
 */
public interface CategoryService extends IService<Category> {

	/**
	 * 管理端获取所有分类 即获取的对象不管是否删除，都要展示，而且不从缓存获取，保证内容是最新的
	 *
	 * @param parentId 分类父ID
	 * @return {@link List }<{@link Category }>
	 * @since 2022-04-27 16:59:28
	 */
	List<Category> dbList(Long parentId);

	/**
	 * 获取分类
	 *
	 * @param id 分类id
	 * @return {@link Category }
	 * @since 2022-04-27 16:59:28
	 */
	Category getCategoryById(Long id);

	/**
	 * 根据分类id集合获取所有分类根据层级排序
	 *
	 * @param ids 分类ID集合
	 * @return {@link List }<{@link Category }>
	 * @since 2022-04-27 16:59:28
	 */
	List<Category> listByIdsOrderByLevel(List<Long> ids);

	/**
	 * 获取分类树
	 *
	 * @return {@link List }<{@link CategoryVO }>
	 * @since 2022-04-27 16:59:28
	 */
	List<CategoryVO> categoryTree();

	/**
	 * 查询所有的分类，父子关系
	 *
	 * @param parentId 分类父ID
	 * @return {@link List }<{@link CategoryVO }>
	 * @since 2022-04-27 16:59:28
	 */
	List<CategoryVO> listAllChildren(Long parentId);

	/**
	 * 查询所有的分类，父子关系 数据库获取
	 *
	 * @return {@link List }<{@link CategoryVO }>
	 * @since 2022-04-27 16:59:28
	 */
	List<CategoryVO> listAllChildren();

	/**
	 * 获取指定分类的分类名称
	 *
	 * @param ids 指定分类id集合
	 * @return {@link List }<{@link String }>
	 * @since 2022-04-27 16:59:28
	 */
	List<String> getCategoryNameByIds(List<Long> ids);

	/**
	 * 获取商品分类list
	 *
	 * @param category 分类
	 * @return {@link List }<{@link Category }>
	 * @since 2022-04-27 16:59:28
	 */
	List<Category> findByAllBySortOrder(Category category);

	/**
	 * 添加商品分类
	 *
	 * @param category 商品分类信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:28
	 */
	Boolean saveCategory(Category category);

	/**
	 * 修改商品分类
	 *
	 * @param category 商品分类信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:28
	 */
	Boolean updateCategory(Category category);

	/**
	 * 批量删除分类
	 *
	 * @param id 分类ID
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:28
	 */
	Boolean delete(Long id);

	/**
	 * 分类状态的更改
	 *
	 * @param categoryId       商品分类ID
	 * @param enableOperations 是否可用
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:28
	 */
	Boolean updateCategoryStatus(Long categoryId, Boolean enableOperations);

	/**
	 * 获取商家经营类目
	 *
	 * @param categories 经营范围
	 * @return {@link List }<{@link CategoryVO }>
	 * @since 2022-04-27 16:59:28
	 */
	List<CategoryVO> getStoreCategory(String[] categories);

	/**
	 * 获取一级分类列表 用于商家入驻选择
	 *
	 * @return {@link List }<{@link Category }>
	 * @since 2022-04-27 16:59:28
	 */
	List<Category> firstCategory();

}
