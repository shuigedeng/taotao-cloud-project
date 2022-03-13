package com.taotao.cloud.goods.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.goods.biz.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.entity.Specification;
import com.taotao.cloud.goods.biz.mapper.CategorySpecificationMapper;
import com.taotao.cloud.goods.biz.service.CategorySpecificationService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 商品分类规格业务层实现
 */
@Service
public class CategorySpecificationServiceImpl extends
	ServiceImpl<CategorySpecificationMapper, CategorySpecification> implements
	CategorySpecificationService {

	@Override
	public List<Specification> getCategorySpecList(String categoryId) {
		return this.baseMapper.getCategorySpecList(categoryId);
	}

	@Override
	public void deleteByCategoryId(String categoryId) {
		this.baseMapper.delete(
			new LambdaQueryWrapper<CategorySpecification>().eq(CategorySpecification::getCategoryId,
				categoryId));
	}
}
