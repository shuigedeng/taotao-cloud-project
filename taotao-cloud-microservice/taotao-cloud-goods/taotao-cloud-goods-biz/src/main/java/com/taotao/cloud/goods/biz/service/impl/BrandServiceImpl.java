package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.goods.api.dto.BrandDTO;
import com.taotao.cloud.goods.api.query.BrandPageQuery;
import com.taotao.cloud.goods.biz.entity.Brand;
import com.taotao.cloud.goods.biz.entity.CategoryBrand;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.mapper.IBrandMapper;
import com.taotao.cloud.goods.biz.mapstruct.IBrandMapStruct;
import com.taotao.cloud.goods.biz.service.IBrandService;
import com.taotao.cloud.goods.biz.service.ICategoryBrandService;
import com.taotao.cloud.goods.biz.service.ICategoryService;
import com.taotao.cloud.goods.biz.service.IGoodsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 商品品牌业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:01:19
 */
@AllArgsConstructor
@Service
public class BrandServiceImpl extends ServiceImpl<IBrandMapper, Brand> implements IBrandService {

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
	public IPage<Brand> getBrandsByPage(BrandPageQuery page) {
		LambdaQueryWrapper<Brand> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.like(StringUtil.isNotBlank(page.getName()), Brand::getName, page.getName());
		return this.page(page.buildMpPage(), queryWrapper);
	}

	@Override
	public List<Brand> getBrandsByCategory(Long categoryId) {
		LambdaQueryWrapper<CategoryBrand> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(CategoryBrand::getCategoryId, categoryId);
		List<CategoryBrand> categoryBrands = categoryBrandService.list(queryWrapper);

		if (categoryBrands != null && !categoryBrands.isEmpty()) {
			List<Long> collect = categoryBrands.stream().map(CategoryBrand::getBrandId).toList();
			return this.list(new LambdaQueryWrapper<Brand>().in(Brand::getId, collect));
		}

		return new ArrayList<>();
	}

	@Override
	public Boolean addBrand(BrandDTO brandDTO) {
		this.checkNameRepeat(null, brandDTO.name());

		return this.save(IBrandMapStruct.INSTANCE.brandDTOToBrand(brandDTO));
	}

	@Override
	public Boolean updateBrand(BrandDTO brandDTO) {
		this.checkExist(brandDTO.id());
		Brand brand = this.checkNameRepeat(brandDTO.id(), brandDTO.name());
		IBrandMapStruct.INSTANCE.copyBrandDTOToBrand(brandDTO, brand);
		return this.updateById(brand);
	}

	@Override
	public Boolean brandDisable(Long brandId, boolean disable) {
		Brand brand = this.checkExist(brandId);

		//禁用，判定绑定关系
		if (disable) {
			checkBind(List.of(brandId));
		}

		brand.setDelFlag(disable);
		return updateById(brand);
	}

	@Override
	public List<Brand> getAllAvailable() {
		LambdaQueryWrapper<Brand> eq = new LambdaQueryWrapper<>();
		eq.eq(Brand::getDelFlag, false);
		return this.list(eq);
	}

	@Override
	public Boolean deleteBrands(List<Long> ids) {
		checkBind(ids);

		return this.removeByIds(ids);
	}

	/**
	 * 校验绑定关系
	 *
	 * @param brandIds 品牌Ids
	 */
	private void checkBind(List<Long> brandIds) {
		//绑定分类品牌关系查询
		List<CategoryBrand> categoryBrands = categoryBrandService.getCategoryBrandListByBrandId(
			brandIds);

		if (!categoryBrands.isEmpty()) {
			List<Long> categoryIds = categoryBrands.stream()
				.map(CategoryBrand::getCategoryId)
				.toList();
			throw new BusinessException(ResultEnum.BRAND_USE_DISABLE_ERROR.getCode(),
				ResultEnum.BRAND_USE_DISABLE_ERROR.getDesc() + ":" + JSONUtil.toJsonStr(
					categoryService.getCategoryNameByIds(categoryIds)));
		}

		//商品绑定关系查询
		List<Goods> goods = goodsService.getByBrandIds(brandIds);
		if (!goods.isEmpty()) {
			List<String> goodsNames = goods.stream()
				.map(Goods::getGoodsName)
				.toList();
			throw new BusinessException(ResultEnum.BRAND_BIND_GOODS_ERROR.getCode(),
				ResultEnum.BRAND_BIND_GOODS_ERROR.getDesc() + ":" + JSONUtil.toJsonStr(goodsNames));
		}
	}

	/**
	 * 校验是否存在
	 *
	 * @param brandId 品牌ID
	 */
	private Brand checkExist(Long brandId) {
		Brand brand = getById(brandId);
		if (brand == null) {
			throw new BusinessException(ResultEnum.BRAND_NOT_EXIST);
		}
		return brand;
	}

	/**
	 * 校验名称重复
	 *
	 * @param name 品牌名称
	 */
	private Brand checkNameRepeat(Long brandId, String name) {
		LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Brand::getName, name);
		wrapper.ne(Objects.nonNull(brandId), Brand::getId, brandId);

		Brand brand = getOne(wrapper);
		if (brand != null) {
			throw new BusinessException(ResultEnum.BRAND_NAME_EXIST_ERROR);
		}
		return brand;
	}
}
