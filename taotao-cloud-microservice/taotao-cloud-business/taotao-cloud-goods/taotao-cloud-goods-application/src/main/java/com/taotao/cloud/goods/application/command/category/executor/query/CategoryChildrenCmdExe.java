

package com.taotao.cloud.goods.application.command.category.executor.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.goods.application.command.category.dto.clientobject.CategoryTreeCO;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.ICategoryMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategoryPO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 删除部门执行器.
 */
@Component
@RequiredArgsConstructor
public class CategoryChildrenCmdExe {

	private final ICategoryMapper categoryMapper;
	private final CategoryTreeCmdExe categoryTreeCmdExe;

	public List<CategoryPO> childrenList(Long parentId) {
		LambdaQueryWrapper<CategoryPO> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(CategoryPO::getParentId, parentId);
		return categoryMapper.selectList(wrapper);
	}

	public List<CategoryTreeCO> listAllChildren(Long parentId) {
		if (Long.valueOf(0).equals(parentId)) {
			return categoryTreeCmdExe.categoryTree();
		}

		// 循环代码，找到对象，把他的子分类返回
		List<CategoryTreeCO> topCatList = categoryTreeCmdExe.categoryTree();
		for (CategoryTreeCO item : topCatList) {
			if (item.getId().equals(parentId)) {
				return item.getChildren();
			}
			else {
				return getChildren(parentId, item.getChildren());
			}
		}
		return new ArrayList<>();
	}


	public List<CategoryTreeCO> listAllChildren() {
		// 获取全部分类
		List<CategoryPO> list = categoryMapper.selectList(
			new LambdaQueryWrapper<CategoryPO>().orderByAsc(CategoryPO::getId));

		// 构造分类树
		List<CategoryTreeCO> categoryTreeCoList = new ArrayList<>();
		for (CategoryPO categoryPo : list) {
			if (Long.valueOf(0).equals(categoryPo.getParentId())) {
				// CategoryCO categoryCO = new CategoryCO(category);
				CategoryTreeCO categoryTreeCo = new CategoryTreeCO();
				categoryTreeCo.setChildren(categoryTreeCmdExe.findChildren(list, categoryTreeCo));
				categoryTreeCoList.add(categoryTreeCo);
			}
		}
		categoryTreeCoList.sort(Comparator.comparing(CategoryTreeCO::getSortOrder));
		return categoryTreeCoList;
	}

	/**
	 * 条件查询分类
	 *
	 * @param category 分类CO
	 */
	public void findAllChild(CategoryTreeCO category) {
		LambdaQueryWrapper<CategoryPO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(CategoryPO::getParentId, category.getId());
		List<CategoryPO> categories = this.categoryMapper.selectList(queryWrapper);
		List<CategoryTreeCO> categoryTreeCoList = new ArrayList<>();
		for (CategoryPO categoryPo1 : categories) {
			categoryTreeCoList.add(BeanUtils.copy(categoryPo1, CategoryTreeCO.class));
		}
		category.setChildren(categoryTreeCoList);
		if (!categoryTreeCoList.isEmpty()) {
			categoryTreeCoList.forEach(this::findAllChild);
		}
	}

	/**
	 * 获取所有的子分类ID
	 *
	 * @param category 分类
	 * @param ids      ID列表
	 */
	public void findAllChildIds(CategoryTreeCO category, List<Long> ids) {
		if (category.getChildren() != null && !category.getChildren().isEmpty()) {
			for (CategoryTreeCO child : category.getChildren()) {
				ids.add(child.getId());
				this.findAllChildIds(child, ids);
			}
		}
	}

	/**
	 * 递归自身，找到id等于parentId的对象，获取他的children 返回
	 *
	 * @param parentId           父ID
	 * @param categoryTreeCoList 分类CO
	 * @return 子分类列表CO
	 */
	private List<CategoryTreeCO> getChildren(Long parentId,
		List<CategoryTreeCO> categoryTreeCoList) {
		for (CategoryTreeCO item : categoryTreeCoList) {
			if (item.getId().equals(parentId)) {
				return item.getChildren();
			}

			if (item.getChildren() != null && !item.getChildren().isEmpty()) {
				return getChildren(parentId, categoryTreeCoList);
			}
		}
		return categoryTreeCoList;
	}
}
