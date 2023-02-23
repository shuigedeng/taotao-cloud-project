package com.taotao.cloud.goods.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.goods.biz.mapper.ICategorySpecificationMapper;
import com.taotao.cloud.goods.biz.model.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.model.entity.Specification;
import com.taotao.cloud.goods.biz.repository.cls.CategorySpecificationRepository;
import com.taotao.cloud.goods.biz.repository.inf.ICategorySpecificationRepository;
import com.taotao.cloud.goods.biz.service.business.ICategorySpecificationService;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类规格业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:15
 */
@AllArgsConstructor
@Service
public class CategorySpecificationServiceImpl extends
	BaseSuperServiceImpl<ICategorySpecificationMapper, CategorySpecification, CategorySpecificationRepository, ICategorySpecificationRepository, Long>
	implements ICategorySpecificationService {

	@Override
	public List<Specification> getCategorySpecList(Long categoryId) {
		return this.baseMapper.getCategorySpecList(categoryId);
	}

	@Override
	public Boolean deleteByCategoryId(Long categoryId) {
		return this.baseMapper.delete(
			new LambdaQueryWrapper<CategorySpecification>().eq(CategorySpecification::getCategoryId,
				categoryId)) > 0;
	}
}
