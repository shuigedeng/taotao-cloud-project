

package com.taotao.cloud.goods.application.command.category.executor.query;


import static com.taotao.boot.common.enums.CachePrefixEnum.CATEGORY_ARRAY;
import static com.taotao.cloud.goods.common.constant.GoodsConstants.DELETE_FLAG_COLUMN;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.ICategoryMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategoryPO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.text.CharSequenceUtil;
import org.springframework.stereotype.Component;

/**
 * 查看部门执行器.
 */
@Component
@RequiredArgsConstructor
public class CategorySearchQryExe {



	private final RedisRepository redisRepository;
	private final ICategoryMapper categoryMapper;

	public List<CategoryPO> listByIdsOrderByLevel(List<Long> ids) {
		LambdaQueryWrapper<CategoryPO> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(CategoryPO::getId, ids);
		wrapper.orderByAsc(CategoryPO::getLevel);
		return categoryMapper.selectList(wrapper);
	}

	public List<CategoryPO> firstCategory() {
		QueryWrapper<CategoryPO> queryWrapper = Wrappers.query();
		queryWrapper.eq("level", 0);
		return categoryMapper.selectList(queryWrapper);
	}

	public List<String> getCategoryNameByIds(List<Long> ids) {
		List<String> categoryName = new ArrayList<>();
		List<CategoryPO> categoryPoList = (List<CategoryPO>) redisRepository.get(
			CATEGORY_ARRAY.getPrefix());
		// 如果缓存中为空，则重新获取缓存
		if (categoryPoList == null) {
			categoryTree();
			categoryPoList = (List<CategoryPO>) redisRepository.get(CATEGORY_ARRAY.getPrefix());
		}

		// 还为空的话，直接返回
		if (categoryPoList == null) {
			return new ArrayList<>();
		}

		// 循环顶级分类
		for (CategoryPO categoryPo : categoryPoList) {
			// 循环查询的id匹配
			for (Long id : ids) {
				if (categoryPo.getId().equals(id)) {
					// 写入商品分类
					categoryName.add(categoryPo.getName());
				}
			}
		}
		return categoryName;
	}

	public List<CategoryPO> findByAllBySortOrder(CategoryPO categoryPo) {
		QueryWrapper<CategoryPO> queryWrapper = new QueryWrapper<>();
		queryWrapper
			.eq(categoryPo.getLevel() != null, "level", categoryPo.getLevel())
			.eq(CharSequenceUtil.isNotBlank(categoryPo.getName()), "name", categoryPo.getName())
			.eq(categoryPo.getParentId() != null, "parent_id", categoryPo.getParentId())
			.ne(categoryPo.getId() != null, "id", categoryPo.getId())
			.eq(DELETE_FLAG_COLUMN, false)
			.orderByAsc("sort_order");
		return this.categoryMapper.selectList(queryWrapper);
	}
}
