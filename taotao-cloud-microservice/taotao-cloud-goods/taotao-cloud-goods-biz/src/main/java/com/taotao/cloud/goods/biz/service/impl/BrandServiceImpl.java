package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.goods.api.dto.BrandDTO;
import com.taotao.cloud.goods.api.dto.BrandPageQuery;
import com.taotao.cloud.goods.biz.entity.Brand;
import com.taotao.cloud.goods.biz.entity.CategoryBrand;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.mapper.BrandMapper;
import com.taotao.cloud.goods.biz.mapstruct.BrandMapStruct;
import com.taotao.cloud.goods.biz.service.BrandService;
import com.taotao.cloud.goods.biz.service.CategoryBrandService;
import com.taotao.cloud.goods.biz.service.CategoryService;
import com.taotao.cloud.goods.biz.service.GoodsService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 商品品牌业务层实现
 */
@AllArgsConstructor
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

	/**
	 * 分类品牌绑定服务
	 */
	private final CategoryBrandService categoryBrandService;
	/**
	 * 分类服务
	 */
	private final CategoryService categoryService;
	/**
	 * 商品服务
	 */
	private final GoodsService goodsService;

	@Override
	public IPage<Brand> getBrandsByPage(BrandPageQuery page) {
		LambdaQueryWrapper<Brand> queryWrapper = new LambdaQueryWrapper<>();
		if (StringUtil.isNotBlank(page.getName())) {
			queryWrapper.like(Brand::getName, page.getName());
		}

		return this.page(page.buildMpPage(), queryWrapper);
	}

	@Override
	public List<Brand> getBrandsByCategory(Long categoryId) {
		QueryWrapper<CategoryBrand> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("category_id", categoryId);
		List<CategoryBrand> list = categoryBrandService.list(queryWrapper);
		if (list != null && !list.isEmpty()) {
			List<Long> collect = list.stream().map(CategoryBrand::getBrandId).toList();
			return this.list(new LambdaQueryWrapper<Brand>().in(Brand::getId, collect));
		}
		return new ArrayList<>();
	}

	@Override
	public Boolean addBrand(BrandDTO brandDTO) {
		if (getOne(new LambdaQueryWrapper<Brand>().eq(Brand::getName, brandDTO.getName()))
			!= null) {
			throw new BusinessException(ResultEnum.BRAND_NAME_EXIST_ERROR);
		}
		return this.save(BrandMapStruct.INSTANCE.brandDTOToBrand(brandDTO));
	}

	@Override
	public Boolean updateBrand(BrandDTO brandDTO) {
		this.checkExist(brandDTO.getId());

		if (getOne(new LambdaQueryWrapper<Brand>().eq(Brand::getName, brandDTO.getName())
			.ne(Brand::getId, brandDTO.getId())) != null) {
			throw new BusinessException(ResultEnum.BRAND_NAME_EXIST_ERROR);
		}

		return this.updateById(BeanUtil.copy(brandDTO, Brand.class));
	}

	@Override
	public Boolean brandDisable(Long brandId, boolean disable) {
		Brand brand = this.checkExist(brandId);
		//如果是要禁用，则需要先判定绑定关系
		if (Boolean.TRUE.equals(disable)) {
			List<Long> ids = new ArrayList<>();
			ids.add(brandId);
			checkBind(ids);
		}
		brand.setDelFlag(disable);
		return updateById(brand);
	}

	@Override
	public List<Brand> getAllAvailable() {
		return this.list(new LambdaQueryWrapper<Brand>().eq(Brand::getDelFlag, 0));
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
		//分了绑定关系查询
		List<CategoryBrand> categoryBrands = categoryBrandService.getCategoryBrandListByBrandId(
			brandIds);

		if (!categoryBrands.isEmpty()) {
			List<Long> categoryIds = categoryBrands.stream().map(CategoryBrand::getCategoryId)
				.toList();
			throw new BusinessException(ResultEnum.BRAND_USE_DISABLE_ERROR.getCode(),
				JSONUtil.toJsonStr(categoryService.getCategoryNameByIds(categoryIds)));
		}

		//分了商品绑定关系查询
		List<Goods> goods = goodsService.getByBrandIds(brandIds);
		if (!goods.isEmpty()) {
			List<String> goodsNames = goods.stream().map(Goods::getGoodsName)
				.collect(Collectors.toList());
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
	private Brand checkExist(Long brandId) {
		Brand brand = getById(brandId);
		if (brand == null) {
			LogUtil.error("品牌ID为" + brandId + "的品牌不存在");
			throw new BusinessException(ResultEnum.BRAND_NOT_EXIST);
		}
		return brand;
	}

}
