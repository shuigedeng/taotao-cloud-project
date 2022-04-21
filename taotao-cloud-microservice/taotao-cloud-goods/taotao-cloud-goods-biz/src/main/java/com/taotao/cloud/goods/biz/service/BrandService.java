package com.taotao.cloud.goods.biz.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.api.dto.BrandDTO;
import com.taotao.cloud.goods.api.query.BrandPageQuery;
import com.taotao.cloud.goods.biz.entity.Brand;
import java.util.List;

/**
 * 商品品牌业务层
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
public interface BrandService extends IService<Brand> {

	/**
	 * 根据条件分页获取品牌列表
	 *
	 * @param brandPageQuery 条件参数
	 * @return 品牌列表
	 */
	IPage<Brand> getBrandsByPage(BrandPageQuery brandPageQuery);

	/**
	 * 删除品牌
	 *
	 * @param ids 品牌id
	 * @return 是否成功
	 */
	Boolean deleteBrands(List<Long> ids);

	/**
	 * 根据分类ID获取品牌列表
	 *
	 * @param categoryId 分类ID
	 * @return 品牌列表
	 */
	List<Brand> getBrandsByCategory(Long categoryId);

	/**
	 * 添加品牌
	 *
	 * @param brandDTO 品牌信息
	 * @return 添加结果
	 */
	Boolean addBrand(BrandDTO brandDTO);

	/**
	 * 更新品牌
	 *
	 * @param brandDTO 品牌信息
	 * @return 更新结果
	 */
	Boolean updateBrand(BrandDTO brandDTO);

	/**
	 * 更新品牌是否可用
	 *
	 * @param brandId 品牌ID
	 * @param disable 是否不可用
	 * @return 更新结果
	 */
	Boolean brandDisable(Long brandId, boolean disable);

	/**
	 * 获取所有可用品牌
	 *
	 * @return 品牌列表
	 * @since 2022-04-06 14:40:02
	 */
	List<Brand> getAllAvailable();

}
