package com.taotao.cloud.goods.biz.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.api.dto.BrandDTO;
import com.taotao.cloud.goods.api.dto.BrandPageDTO;
import com.taotao.cloud.goods.biz.entity.Brand;
import java.util.List;

/**
 * 商品品牌业务层
 */
public interface BrandService extends IService<Brand> {

	/**
	 * 根据条件分页获取品牌列表
	 *
	 * @param page 条件参数
	 * @return 品牌列表
	 */
	IPage<Brand> getBrandsByPage(BrandPageDTO page);

	/**
	 * 删除品牌
	 *
	 * @param ids 品牌id
	 */
	Boolean deleteBrands(List<String> ids);

	/**
	 * 根据分类ID获取品牌列表
	 *
	 * @param categoryId 分类ID
	 * @return 品牌列表
	 */
	List<Brand> getBrandsByCategory(String categoryId);

	/**
	 * 添加品牌
	 *
	 * @param brandVO 品牌信息
	 * @return 添加结果
	 */
	Boolean addBrand(BrandDTO brandVO);

	/**
	 * 更新品牌
	 *
	 * @param brandVO 品牌信息
	 * @return 更新结果
	 */
	Boolean updateBrand(BrandDTO brandVO);

	/**
	 * 更新品牌是否可用
	 *
	 * @param brandId 品牌ID
	 * @param disable 是否不可用
	 * @return 更新结果
	 */
	Boolean brandDisable(String brandId, boolean disable);

	/**
	 * 获取所有可用品牌
	 *
	 * @return 品牌列表
	 * @since 2022-04-06 14:40:02
	 */
	List<Brand> getAllAvailable();


}
