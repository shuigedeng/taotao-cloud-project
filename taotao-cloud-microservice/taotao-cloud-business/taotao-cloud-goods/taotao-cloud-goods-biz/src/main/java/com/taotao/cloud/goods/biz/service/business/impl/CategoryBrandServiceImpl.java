/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.goods.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.goods.biz.model.vo.CategoryBrandVO;
import com.taotao.cloud.goods.biz.mapper.ICategoryBrandMapper;
import com.taotao.cloud.goods.biz.model.entity.CategoryBrand;
import com.taotao.cloud.goods.biz.repository.CategoryBrandRepository;
import com.taotao.cloud.goods.biz.repository.ICategoryBrandRepository;
import com.taotao.cloud.goods.biz.service.business.ICategoryBrandService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
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
public class CategoryBrandServiceImpl extends BaseSuperServiceImpl<
	CategoryBrand,
	Long,
	ICategoryBrandMapper,
	CategoryBrandRepository,
	ICategoryBrandRepository> implements ICategoryBrandService {

	@Override
	public List<CategoryBrandVO> getCategoryBrandList(Long categoryId) {
		return im().getCategoryBrandList(categoryId);
	}

	@Override
	public boolean deleteByCategoryId(Long categoryId) {
		LambdaQueryWrapper<CategoryBrand> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(CategoryBrand::getCategoryId, categoryId);
		return im().delete(wrapper) > 0;
	}

	@Override
	public List<CategoryBrand> getCategoryBrandListByBrandId(List<Long> brandId) {
		LambdaQueryWrapper<CategoryBrand> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(CategoryBrand::getBrandId, brandId);
		return list(wrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveCategoryBrandList(Long categoryId, List<Long> brandIds) {
		// 删除分类品牌绑定信息
		deleteByCategoryId(categoryId);

		// 绑定品牌信息
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
