package com.taotao.cloud.goods.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taotao.cloud.goods.api.model.vo.CategoryBrandVO;
import com.taotao.cloud.goods.biz.mapper.ICategoryBrandMapper;
import com.taotao.cloud.goods.biz.model.entity.CategoryBrand;
import com.taotao.cloud.goods.biz.repository.cls.CategoryBrandRepository;
import com.taotao.cloud.goods.biz.repository.inf.ICategoryBrandRepository;
import com.taotao.cloud.goods.biz.service.business.ICategoryBrandService;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 规格项业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:01:32
 */
@Service
public class CategoryBrandServiceImpl extends
	BaseSuperServiceImpl<ICategoryBrandMapper, CategoryBrand, CategoryBrandRepository, ICategoryBrandRepository, Long>
	implements ICategoryBrandService {

	@Override
	public List<CategoryBrandVO> getCategoryBrandList(Long categoryId) {
		return this.baseMapper.getCategoryBrandList(categoryId);
	}

	@Override
	public Boolean deleteByCategoryId(Long categoryId) {
		LambdaQueryWrapper<CategoryBrand> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(CategoryBrand::getCategoryId, categoryId);
		return this.baseMapper.delete(wrapper) > 0;
	}

	@Override
	public List<CategoryBrand> getCategoryBrandListByBrandId(List<Long> brandId) {
		LambdaQueryWrapper<CategoryBrand> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(CategoryBrand::getBrandId, brandId);
		return this.list(wrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveCategoryBrandList(Long categoryId, List<Long> brandIds) {
		//删除分类品牌绑定信息
		this.deleteByCategoryId(categoryId);

		//绑定品牌信息
		if (!brandIds.isEmpty()) {
			List<CategoryBrand> categoryBrands = new ArrayList<>();
			for (Long brandId : brandIds) {
				categoryBrands.add(new CategoryBrand(categoryId, brandId));
			}
			this.saveBatch(categoryBrands);
		}

		return true;
	}
}
