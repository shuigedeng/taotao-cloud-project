

package com.taotao.cloud.goods.application.command.category.executor;



import static com.taotao.cloud.goods.common.constant.GoodsConstants.DELETE_FLAG_COLUMN;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.cloud.goods.application.command.category.dto.clientobject.CategoryTreeCO;
import com.taotao.cloud.goods.application.command.category.executor.query.CategoryChildrenCmdExe;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.ICategoryMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategoryPO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 删除部门执行器.
 */
@Component
@RequiredArgsConstructor
public class CategoryUpdateCmdExe {

	private final RedisRepository redisRepository;
	private final ICategoryMapper categoryMapper;
	private final CategoryCacheDelCmdExe categoryCacheDelCmdExe;
	private final CategoryChildrenCmdExe categoryChildrenCmdExe;

	@Transactional(rollbackFor = Exception.class)
	public boolean updateCategoryStatus(Long categoryId, boolean enableOperations) {
		// 禁用子分类
		CategoryPO categoryPo = this.categoryMapper.selectById(categoryId);
		CategoryTreeCO categoryTreeCo = BeanUtils.copy(categoryPo, CategoryTreeCO.class);
		List<Long> ids = new ArrayList<>();

		assert categoryTreeCo != null;

		ids.add(categoryTreeCo.getId());
		categoryChildrenCmdExe.findAllChild(categoryTreeCo);
		categoryChildrenCmdExe.findAllChildIds(categoryTreeCo, ids);
		LambdaUpdateWrapper<CategoryPO> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.in(CategoryPO::getId, ids);
		updateWrapper.set(CategoryPO::getDelFlag, enableOperations);
		this.categoryMapper.update(updateWrapper);

		categoryCacheDelCmdExe.removeCache();

		return true;
	}


	@CacheEvict(key = "#categoryPO.id")
	@Transactional(rollbackFor = Exception.class)
	public boolean updateCategory(CategoryPO categoryPo) {
		// 判断分类佣金是否正确
		if (categoryPo.getCommissionRate().compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ResultEnum.CATEGORY_COMMISSION_RATE_ERROR);
		}

		// 判断父分类与子分类的状态是否一致
		if (categoryPo.getParentId() != null && !Long.valueOf(0).equals(categoryPo.getParentId())) {
			CategoryPO parentCategoryPo = this.categoryMapper.selectById(categoryPo.getParentId());
			if (!parentCategoryPo.getDelFlag().equals(categoryPo.getDelFlag())) {
				throw new BusinessException(ResultEnum.CATEGORY_DELETE_FLAG_ERROR);
			}
		}

		UpdateWrapper<CategoryPO> updateWrapper = new UpdateWrapper<>();
		updateWrapper
			.eq("id", categoryPo.getId())
			.set("name", categoryPo.getName())
			.set("image", categoryPo.getImage())
			.set("sort_order", categoryPo.getSortOrder())
			.set(DELETE_FLAG_COLUMN, categoryPo.getDelFlag())
			.set("commission_rate", categoryPo.getCommissionRate());
		this.categoryMapper.update(categoryPo, updateWrapper);
		categoryCacheDelCmdExe.removeCache();
		return true;
	}


}
