

package com.taotao.cloud.goods.application.command.category.executor;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.ICategoryMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategoryPO;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 删除部门执行器.
 */
@Component
@RequiredArgsConstructor
public class CategorySaveCmdExe {

	private final RedisRepository redisRepository;
	private final ICategoryMapper categoryMapper;
	private final CategoryCacheDelCmdExe categoryCacheDelCmdExe;

	@Transactional(rollbackFor = Exception.class)
	public boolean saveCategory(CategoryPO categoryPo) {
		// 判断分类佣金是否正确
		if (categoryPo.getCommissionRate().compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException(ResultEnum.CATEGORY_COMMISSION_RATE_ERROR);
		}

		// 子分类与父分类的状态一致
		if (categoryPo.getParentId() != null && !Long.valueOf(0).equals(categoryPo.getParentId())) {
			CategoryPO parentCategoryPo = this.categoryMapper.selectById(categoryPo.getParentId());
			categoryPo.setDelFlag(parentCategoryPo.getDelFlag());
		}
		this.categoryMapper.insert(categoryPo);
		categoryCacheDelCmdExe.removeCache();
		return true;
	}
}
