package com.taotao.cloud.goods.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.goods.biz.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.entity.Specification;
import com.taotao.cloud.goods.biz.mapper.CategorySpecificationMapper;
import com.taotao.cloud.goods.biz.service.CategorySpecificationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类规格业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:15
 */
@Service
public class CategorySpecificationServiceImpl extends
	ServiceImpl<CategorySpecificationMapper, CategorySpecification> implements
	CategorySpecificationService {

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
