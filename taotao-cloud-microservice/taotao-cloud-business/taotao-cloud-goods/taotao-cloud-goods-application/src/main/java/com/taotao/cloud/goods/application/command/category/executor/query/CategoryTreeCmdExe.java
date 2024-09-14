

package com.taotao.cloud.goods.application.command.category.executor.query;

import static com.taotao.boot.common.enums.CachePrefixEnum.CATEGORY;
import static com.taotao.boot.common.enums.CachePrefixEnum.CATEGORY_ARRAY;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.cloud.goods.application.command.category.dto.clientobject.CategoryTreeCO;
import com.taotao.cloud.goods.application.convert.CategoryConvert;
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
public class CategoryTreeCmdExe {

	private final RedisRepository redisRepository;
	private final ICategoryMapper categoryMapper;

	public List<CategoryTreeCO> categoryTree() {
		// 获取缓存数据
		List<CategoryTreeCO> categoryTreeCoList = redisRepository.lGet(
			CATEGORY.getPrefix(), 0L, redisRepository.lGetListSize(CATEGORY.getPrefix()));
		if (categoryTreeCoList != null) {
			return categoryTreeCoList;
		}

		// 获取全部分类
		LambdaQueryWrapper<CategoryPO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(CategoryPO::getDelFlag, false);
		List<CategoryPO> list = categoryMapper.selectList(queryWrapper);

		// 构造分类树
		categoryTreeCoList = new ArrayList<>();
		for (CategoryPO categoryPo : list) {
			if (Long.valueOf(0).equals(categoryPo.getParentId())) {
				CategoryTreeCO categoryTreeCo = CategoryConvert.INSTANCE.convert(categoryPo);
				categoryTreeCo.setParentTitle(categoryPo.getName());
				categoryTreeCo.setChildren(findChildren(list, categoryTreeCo));
				categoryTreeCoList.add(categoryTreeCo);
			}
		}

		categoryTreeCoList.sort(Comparator.comparing(CategoryTreeCO::getSortOrder));

		if (!categoryTreeCoList.isEmpty()) {
			redisRepository.lSet(CATEGORY.getPrefix(), categoryTreeCoList);
			redisRepository.lSet(CATEGORY_ARRAY.getPrefix(), list);
		}
		return categoryTreeCoList;
	}

	/**
	 * 递归树形CO
	 *
	 * @param categories     分类列表
	 * @param categoryTreeCo 分类CO
	 * @return 分类CO列表
	 */
	public List<CategoryTreeCO> findChildren(List<CategoryPO> categories,
		CategoryTreeCO categoryTreeCo) {
		List<CategoryTreeCO> children = new ArrayList<>();
		categories.forEach(item -> {
			if (item.getParentId().equals(categoryTreeCo.getId())) {
				CategoryTreeCO temp = CategoryConvert.INSTANCE.convert(item);
				temp.setParentTitle(item.getName());
				temp.setChildren(findChildren(categories, temp));
				children.add(temp);
			}
		});

		return children;
	}
}
