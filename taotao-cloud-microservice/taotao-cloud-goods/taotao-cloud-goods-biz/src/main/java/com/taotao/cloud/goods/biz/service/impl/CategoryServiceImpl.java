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
import com.taotao.cloud.goods.api.vo.CategoryVO;
import com.taotao.cloud.goods.biz.entity.Category;
import com.taotao.cloud.goods.biz.mapper.CategoryMapper;
import com.taotao.cloud.goods.biz.service.CategoryBrandService;
import com.taotao.cloud.goods.biz.service.CategoryParameterGroupService;
import com.taotao.cloud.goods.biz.service.CategoryService;
import com.taotao.cloud.goods.biz.service.CategorySpecificationService;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 商品分类业务层实现
 */
@AllArgsConstructor
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements
	CategoryService {

	private static final String DELETE_FLAG_COLUMN = "delete_flag";
	/**
	 * 缓存服务
	 */
	private final RedisRepository redisRepository;
	/**
	 * 分类品牌服务
	 */
	private final CategoryBrandService categoryBrandService;
	/**
	 * 分类绑定参数服务
	 */
	private final CategoryParameterGroupService categoryParameterGroupService;
	/**
	 * 分类规格服务
	 */
	private final CategorySpecificationService categorySpecificationService;

	@Override
	public List<Category> dbList(String parentId) {
		return this.list(new LambdaQueryWrapper<Category>().eq(Category::getParentId, parentId));
	}

	@Override
	public Category getCategoryById(String id) {
		return this.getById(id);
	}

	@Override
	public List<Category> listByIdsOrderByLevel(List<String> ids) {
		return this.list(new LambdaQueryWrapper<Category>().in(Category::getId, ids)
			.orderByAsc(Category::getLevel));
	}

	@Override
	public List<CategoryVO> categoryTree() {
		List<CategoryVO> categoryVOList = (List<CategoryVO>) redisRepository.get(
			CachePrefix.CATEGORY.getPrefix());
		if (categoryVOList != null) {
			return categoryVOList;
		}

		//获取全部分类
		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Category::getDelFlag, false);
		List<Category> list = this.list(queryWrapper);

		//构造分类树
		categoryVOList = new ArrayList<>();
		for (Category category : list) {
			if ("0".equals(category.getParentId())) {
				CategoryVO categoryVO = BeanUtil.copyProperties(category, CategoryVO.class);
				categoryVO.setChildren(findChildren(list, categoryVO));
				categoryVOList.add(categoryVO);
			}
		}

		//categoryVOList.sort(Comparator.comparing(Category::getSortOrder));
		if (!categoryVOList.isEmpty()) {
			redisRepository.set(CachePrefix.CATEGORY.getPrefix(), categoryVOList);
			redisRepository.set(CachePrefix.CATEGORY_ARRAY.getPrefix(), list);
		}
		return categoryVOList;
	}

	@Override
	public List<CategoryVO> getStoreCategory(String[] categories) {
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
	public List<CategoryVO> listAllChildren(String parentId) {
		if ("0".equals(parentId)) {
			return categoryTree();
		}

		//循环代码，找到对象，把他的子分类返回
		List<CategoryVO> topCatList = categoryTree();
		for (CategoryVO item : topCatList) {
			if (item.getId().equals(parentId)) {
				return item.getChildren();
			} else {
				return getChildren(parentId, item.getChildren());
			}
		}
		return new ArrayList<>();
	}

	@Override
	public List<CategoryVO> listAllChildren() {

		//获取全部分类
		List<Category> list = this.list();

		//构造分类树
		List<CategoryVO> categoryVOList = new ArrayList<>();
		for (Category category : list) {
			if (("0").equals(category.getParentId())) {
				//CategoryVO categoryVO = new CategoryVO(category);
				CategoryVO categoryVO = new CategoryVO();
				categoryVO.setChildren(findChildren(list, categoryVO));
				categoryVOList.add(categoryVO);
			}
		}
		//categoryVOList.sort(Comparator.comparing(Category::getSortOrder));
		return categoryVOList;
	}

	@Override
	public List<String> getCategoryNameByIds(List<String> ids) {
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
			for (String id : ids) {
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
		if (category.getParentId() != null && !("0").equals(category.getParentId())) {
			Category parentCategory = this.getById(category.getParentId());
			category.setDelFlag(parentCategory.getDelFlag()
			);
		}
		this.save(category);
		removeCache();
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateCategory(Category category) {
		//判断分类佣金是否正确
		if (category.getCommissionRate().compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ResultEnum.CATEGORY_COMMISSION_RATE_ERROR);
		}

		//判断父分类与子分类的状态是否一致
		if (category.getParentId() != null && !"0".equals(category.getParentId())) {
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
	public Boolean delete(String id) {
		this.removeById(id);
		removeCache();
		//删除关联关系
		categoryBrandService.deleteByCategoryId(id);
		categoryParameterGroupService.deleteByCategoryId(id);
		categorySpecificationService.deleteByCategoryId(id);
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateCategoryStatus(String categoryId, Boolean enableOperations) {
		//禁用子分类
		Category category = this.getById(categoryId);
		CategoryVO categoryVO = BeanUtil.copy(category, CategoryVO.class);
		List<String> ids = new ArrayList<>();
		ids.add(categoryVO.getId());
		this.findAllChild(categoryVO);
		this.findAllChildIds(categoryVO, ids);
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
	 * @param categoryVO 分类VO
	 * @return 分类VO列表
	 */
	private List<CategoryVO> findChildren(List<Category> categories, CategoryVO categoryVO) {
		List<CategoryVO> children = new ArrayList<>();
		categories.forEach(item -> {
			if (item.getParentId().equals(categoryVO.getId())) {
				CategoryVO temp = BeanUtil.copyProperties(item, CategoryVO.class);
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
	private void findAllChild(CategoryVO category) {
		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Category::getParentId, category.getId());
		List<Category> categories = this.list(queryWrapper);
		List<CategoryVO> categoryVOList = new ArrayList<>();
		for (Category category1 : categories) {
			categoryVOList.add(BeanUtil.copy(category1, CategoryVO.class));
		}
		category.setChildren(categoryVOList);
		if (!categoryVOList.isEmpty()) {
			categoryVOList.forEach(this::findAllChild);
		}
	}

	/**
	 * 获取所有的子分类ID
	 *
	 * @param category 分类
	 * @param ids      ID列表
	 */
	private void findAllChildIds(CategoryVO category, List<String> ids) {
		if (category.getChildren() != null && !category.getChildren().isEmpty()) {
			for (CategoryVO child : category.getChildren()) {
				ids.add(child.getId());
				this.findAllChildIds(child, ids);
			}
		}
	}

	/**
	 * 递归自身，找到id等于parentId的对象，获取他的children 返回
	 *
	 * @param parentId       父ID
	 * @param categoryVOList 分类VO
	 * @return 子分类列表VO
	 */
	private List<CategoryVO> getChildren(String parentId, List<CategoryVO> categoryVOList) {
		for (CategoryVO item : categoryVOList) {
			if (item.getId().equals(parentId)) {
				return item.getChildren();
			}
			if (item.getChildren() != null && !item.getChildren().isEmpty()) {
				return getChildren(parentId, categoryVOList);
			}
		}
		return categoryVOList;
	}

	/**
	 * 清除缓存
	 */
	private void removeCache() {
		redisRepository.del(CachePrefix.CATEGORY.getPrefix());
	}
}
