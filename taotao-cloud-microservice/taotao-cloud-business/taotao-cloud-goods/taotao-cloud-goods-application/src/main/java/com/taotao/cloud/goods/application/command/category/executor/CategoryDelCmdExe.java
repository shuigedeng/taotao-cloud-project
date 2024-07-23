

package com.taotao.cloud.goods.application.command.category.executor;

import static com.taotao.cloud.common.enums.CachePrefixEnum.CATEGORY;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.goods.application.command.category.dto.clientobject.CategoryTreeCO;
import com.taotao.cloud.goods.application.service.ICategoryBrandService;
import com.taotao.cloud.goods.application.service.ICategoryParameterGroupService;
import com.taotao.cloud.goods.application.service.ICategorySpecificationService;
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
public class CategoryDelCmdExe {

	private final RedisRepository redisRepository;
	private final ICategoryMapper categoryMapper;
	private final CategoryCacheDelCmdExe categoryCacheDelCmdExe;
	/**
	 * 分类绑定参数服务
	 */
	private final ICategoryParameterGroupService categoryParameterGroupService;
	/**
	 * 分类规格服务
	 */
	private final ICategorySpecificationService categorySpecificationService;
	/**
	 * 分类品牌服务
	 */
	private final ICategoryBrandService categoryBrandService;

	public boolean delete(Long id) {
		this.categoryMapper.deleteById(id);

		categoryCacheDelCmdExe.removeCache();

		// 删除关联关系
		categoryParameterGroupService.deleteByCategoryId(id);
		categorySpecificationService.deleteByCategoryId(id);
		return categoryBrandService.deleteByCategoryId(id);
	}
}
