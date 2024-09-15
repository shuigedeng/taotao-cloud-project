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

package com.taotao.cloud.goods.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.data.mybatis.mybatisplus.MpUtils;
import com.taotao.cloud.goods.application.convert.BrandConvert;
import com.taotao.cloud.goods.application.service.IBrandService;
import com.taotao.cloud.goods.application.service.ICategoryBrandService;
import com.taotao.cloud.goods.application.service.ICategoryService;
import com.taotao.cloud.goods.application.service.IGoodsService;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.IBrandMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.BrandPO;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategoryBrandPO;
import com.taotao.cloud.goods.infrastructure.persistent.repository.cls.BrandRepository;
import com.taotao.cloud.goods.infrastructure.persistent.repository.inf.IBrandRepository;
import com.taotao.boot.web.base.service.impl.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

/**
 * 商品品牌业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:01:19
 */
@Service
@AllArgsConstructor
public class BrandServiceImpl extends
	BaseSuperServiceImpl<BrandPO, Long, IBrandMapper, BrandRepository, IBrandRepository>
	implements IBrandService {

	/**
	 * 分类品牌绑定服务
	 */
	private final ICategoryBrandService categoryBrandService;
	/**
	 * 分类服务
	 */
	private final ICategoryService categoryService;
	/**
	 * 商品服务
	 */
	private final IGoodsService goodsService;

	@Override
	public IPage<BrandPO> brandsQueryPage(BrandPageQuery page) {
		LambdaQueryWrapper<BrandPO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(page.getName()), BrandPO::getName, page.getName());

		return this.page(MpUtils.buildMpPage(page), queryWrapper);
	}

	@Override
	public List<BrandPO> getBrandsByCategory(Long categoryId) {
		QueryWrapper<CategoryBrandPO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("category_id", categoryId);
		List<CategoryBrandPO> list = categoryBrandService.list(queryWrapper);
		if (list != null && !list.isEmpty()) {
			List<Long> collect = list.stream().map(CategoryBrand::getBrandId).toList();
			return this.list(new LambdaQueryWrapper<BrandPO>().in(BrandPO::getId, collect));
		}
		return new ArrayList<>();
	}

	@Override
	public List<Map<String, Object>> getBrandsMapsByCategory(List<Long> categoryIds,
		String columns) {
		return null;
	}

	@Override
	public List<BrandPO> getBrandsByCategorys(Long categoryIds) {
		// Map<String,  List<Brand>> map = this.baseMapper.selectBrandsByCategorysAsMap(categoryIds)

		QueryWrapper<CategoryBrand> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("category_id", categoryIds);
		List<CategoryBrand> list = categoryBrandService.list(queryWrapper);
		if (list != null && !list.isEmpty()) {
			List<Long> collect = list.stream().map(CategoryBrand::getBrandId).toList();
			return this.list(new LambdaQueryWrapper<BrandPO>().in(BrandPO::getId, collect));
		}
		return new ArrayList<>();
	}

	@Override
	public boolean addBrand(BrandDTO brandDTO) {
		LambdaQueryWrapper<BrandPO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(BrandPO::getName, brandDTO.getName());
		if (getOne(lambdaQueryWrapper) != null) {
			throw new BusinessException(ResultEnum.BRAND_NAME_EXIST_ERROR);
		}
		return this.save(BrandConvert.INSTANCE.convert(brandDTO));
	}

	@Override
	public boolean updateBrand(BrandDTO brandDTO) {
		this.checkExist(brandDTO.getId());

		if (getOne(new LambdaQueryWrapper<BrandPO>()
			.eq(BrandPO::getName, brandDTO.getName())
			.ne(BrandPO::getId, brandDTO.getId()))
			!= null) {
			throw new BusinessException(ResultEnum.BRAND_NAME_EXIST_ERROR);
		}

		return this.updateById(BeanUtils.copy(brandDTO, BrandPO.class));
	}

	@Override
	public boolean brandDisable(Long brandId, boolean disable) {
		BrandPO brand = this.checkExist(brandId);
		// 如果是要禁用，则需要先判定绑定关系
		if (disable) {
			List<Long> ids = new ArrayList<>();
			ids.add(brandId);
			checkBind(ids);
		}
		brand.setDelFlag(disable);
		return updateById(brand);
	}

	@Override
	public List<BrandPO> getAllAvailable() {
		LambdaQueryWrapper<BrandPO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(BrandPO::getDelFlag, 0);
		return this.list(lambdaQueryWrapper);
	}

	@Override
	public IPage<BrandPO> getBrandsByPage(BrandPageQuery page) {
		return null;
	}

	@Override
	public boolean deleteBrands(List<Long> ids) {
		checkBind(ids);
		return this.removeByIds(ids);
	}

	/**
	 * 校验绑定关系
	 *
	 * @param brandIds 品牌Ids
	 */
	private void checkBind(List<Long> brandIds) {
		// 分了绑定关系查询
		List<CategoryBrand> categoryBrands = categoryBrandService.getCategoryBrandListByBrandId(
			brandIds);

		if (!categoryBrands.isEmpty()) {
			List<Long> categoryIds =
				categoryBrands.stream().map(CategoryBrand::getCategoryId).toList();
			throw new BusinessException(
				ResultEnum.BRAND_USE_DISABLE_ERROR.getCode(),
				JSONUtil.toJsonStr(categoryService.getCategoryNameByIds(categoryIds)));
		}

		// 分了商品绑定关系查询
		List<Goods> goods = goodsService.getByBrandIds(brandIds);
		if (!goods.isEmpty()) {
			List<String> goodsNames = goods.stream().map(Goods::getGoodsName).toList();
			throw new BusinessException(ResultEnum.BRAND_BIND_GOODS_ERROR.getCode(),
				JSONUtil.toJsonStr(goodsNames));
		}
	}

	/**
	 * 校验是否存在
	 *
	 * @param brandId 品牌ID
	 * @return 品牌
	 */
	private BrandPO checkExist(Long brandId) {
		BrandPO brand = getById(brandId);
		if (brand == null) {
			LogUtils.error("品牌ID为" + brandId + "的品牌不存在");
			throw new BusinessException(ResultEnum.BRAND_NOT_EXIST);
		}
		return brand;
	}
}
