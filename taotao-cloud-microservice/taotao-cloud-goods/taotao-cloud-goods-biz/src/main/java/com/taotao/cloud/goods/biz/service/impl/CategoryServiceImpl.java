package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.goods.api.web.vo.CategoryTreeVO;
import com.taotao.cloud.goods.biz.model.entity.Category;
import com.taotao.cloud.goods.biz.mapper.ICategoryMapper;
import com.taotao.cloud.goods.biz.mapstruct.ICategoryMapStruct;
import com.taotao.cloud.goods.biz.service.IBrandService;
import com.taotao.cloud.goods.biz.service.ICategoryBrandService;
import com.taotao.cloud.goods.biz.service.ICategoryParameterGroupService;
import com.taotao.cloud.goods.biz.service.ICategoryService;
import com.taotao.cloud.goods.biz.service.ICategorySpecificationService;
import com.taotao.cloud.redis.repository.RedisRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
public class CategoryServiceImpl extends ServiceImpl<ICategoryMapper, Category> implements
	ICategoryService {

	private static final String DELETE_FLAG_COLUMN = "delete_flag";
	/**
	 * 缓存服务
	 */
	private final RedisRepository redisRepository;
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
	public List<Category> dbList(Long parentId) {
		return this.list(new LambdaQueryWrapper<Category>().eq(Category::getParentId, parentId));
	}

	@Override
	@Cacheable(key = "#id")
	public Category getCategoryById(Long id) {
		return this.getById(id);
	}

	@Override
	public List<Category> listByIdsOrderByLevel(List<Long> ids) {
		return this.list(new LambdaQueryWrapper<Category>().in(Category::getId, ids)
			.orderByAsc(Category::getLevel));
	}

	@Override
	public List<CategoryTreeVO> categoryTree() {
		// 获取缓存数据
		List<CategoryTreeVO> categoryTreeVOList = redisRepository.lGet(CachePrefix.CATEGORY.getPrefix(), 0L, redisRepository.lGetListSize(CachePrefix.CATEGORY.getPrefix()));
		if (categoryTreeVOList != null) {
			return categoryTreeVOList;
		}

		//获取全部分类
		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Category::getDelFlag, false);
		List<Category> list = this.list(queryWrapper);

		brandService.getBrandsByCategory()

		//构造分类树
		categoryTreeVOList = new ArrayList<>();
		for (Category category : list) {
			if (Long.valueOf(0).equals(category.getParentId())) {
				CategoryTreeVO categoryTreeVO = ICategoryMapStruct.INSTANCE.categoryToCategoryVO(category);
				categoryTreeVO.setParentTitle(category.getName());
				categoryTreeVO.setChildren(findChildren(list, categoryTreeVO));
				categoryTreeVOList.add(categoryTreeVO);
			}
		}

		categoryTreeVOList.sort(Comparator.comparing(CategoryTreeVO::getSortOrder));

		if (!categoryTreeVOList.isEmpty()) {
			redisRepository.lSet(CachePrefix.CATEGORY.getPrefix(), categoryTreeVOList);
			redisRepository.lSet(CachePrefix.CATEGORY_ARRAY.getPrefix(), list);
		}
		return categoryTreeVOList;
	}

	@Override
	public List<CategoryTreeVO> getStoreCategory(String[] categories) {
		List<String> arr = Arrays.asList(categories.clone());
		return categoryTree().stream()
			.filter(item -> arr.contains(item.getId())).collect(Collectors.toList());
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

		//循环代码，找到对象，把他的子分类返回
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

		//获取全部分类
		List<Category> list = this.list();

		//构造分类树
		List<CategoryTreeVO> categoryTreeVOList = new ArrayList<>();
		for (Category category : list) {
			if (Long.valueOf(0).equals(category.getParentId())) {
				//CategoryVO categoryVO = new CategoryVO(category);
				CategoryTreeVO categoryTreeVO = new CategoryTreeVO();
				categoryTreeVO.setChildren(findChildren(list, categoryTreeVO));
				categoryTreeVOList.add(categoryTreeVO);
			}
		}
		categoryTreeVOList.sort(Comparator.comparing(CategoryTreeVO::getSortOrder));
		return categoryTreeVOList;
	}

	@Override
	public List<String> getCategoryNameByIds(List<Long> ids) {
		List<String> categoryName = new ArrayList<>();
		List<Category> categoryVOList = (List<Category>) redisRepository.get(
			CachePrefix.CATEGORY_ARRAY.getPrefix());
		//如果缓存中为空，则重新获取缓存
		if (categoryVOList == null) {
			categoryTree();
			categoryVOList = (List<Category>) redisRepository.get(
				CachePrefix.CATEGORY_ARRAY.getPrefix());
		}

		//还为空的话，直接返回
		if (categoryVOList == null) {
			return new ArrayList<>();
		}

		//循环顶级分类
		for (Category category : categoryVOList) {
			//循环查询的id匹配
			for (Long id : ids) {
				if (category.getId().equals(id)) {
					//写入商品分类
					categoryName.add(category.getName());
				}
			}
		}
		return categoryName;
	}

	@Override
	public List<Category> findByAllBySortOrder(Category category) {
		QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(category.getLevel() != null, "level", category.getLevel())
			.eq(CharSequenceUtil.isNotBlank(category.getName()), "name", category.getName())
			.eq(category.getParentId() != null, "parent_id", category.getParentId())
			.ne(category.getId() != null, "id", category.getId())
			.eq(DELETE_FLAG_COLUMN, false)
			.orderByAsc("sort_order");
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveCategory(Category category) {
		//判断分类佣金是否正确
		if (category.getCommissionRate().compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ResultEnum.CATEGORY_COMMISSION_RATE_ERROR);
		}

		//子分类与父分类的状态一致
		if (category.getParentId() != null && !Long.valueOf(0).equals(category.getParentId())) {
			Category parentCategory = this.getById(category.getParentId());
			category.setDelFlag(parentCategory.getDelFlag()
			);
		}
		this.save(category);
		removeCache();
		return true;
	}

	@Override
	@CacheEvict(key = "#category.id")
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateCategory(Category category) {
		//判断分类佣金是否正确
		if (category.getCommissionRate().compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ResultEnum.CATEGORY_COMMISSION_RATE_ERROR);
		}

		//判断父分类与子分类的状态是否一致
		if (category.getParentId() != null && !Long.valueOf(0).equals(category.getParentId())) {
			Category parentCategory = this.getById(category.getParentId());
			if (!parentCategory.getDelFlag().equals(category.getDelFlag())) {
				throw new BusinessException(ResultEnum.CATEGORY_DELETE_FLAG_ERROR);
			}
		}

		UpdateWrapper<Category> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", category.getId())
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
	@Transactional(rollbackFor = Exception.class)
	public Boolean delete(Long id) {
		this.removeById(id);
		removeCache();

		//删除关联关系
		categoryParameterGroupService.deleteByCategoryId(id);
		categorySpecificationService.deleteByCategoryId(id);
		return categoryBrandService.deleteByCategoryId(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateCategoryStatus(Long categoryId, Boolean enableOperations) {
		//禁用子分类
		Category category = this.getById(categoryId);
		CategoryTreeVO categoryTreeVO = BeanUtil.copy(category, CategoryTreeVO.class);
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
	 * @param categories 分类列表
	 * @param categoryTreeVO 分类VO
	 * @return 分类VO列表
	 */
	private List<CategoryTreeVO> findChildren(List<Category> categories, CategoryTreeVO categoryTreeVO) {
		List<CategoryTreeVO> children = new ArrayList<>();
		categories.forEach(item -> {
			if (item.getParentId().equals(categoryTreeVO.getId())) {
				CategoryTreeVO temp = ICategoryMapStruct.INSTANCE.categoryToCategoryVO(item);
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
			categoryTreeVOList.add(BeanUtil.copy(category1, CategoryTreeVO.class));
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
	 * @param parentId       父ID
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
		redisRepository.del(CachePrefix.CATEGORY.getPrefix());
	}
}
