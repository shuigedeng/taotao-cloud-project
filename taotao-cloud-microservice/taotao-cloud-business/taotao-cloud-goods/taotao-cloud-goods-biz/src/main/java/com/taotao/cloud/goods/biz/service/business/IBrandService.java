package com.taotao.cloud.goods.biz.service.business;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.goods.api.model.dto.BrandDTO;
import com.taotao.cloud.goods.api.model.page.BrandPageQuery;
import com.taotao.cloud.goods.biz.model.entity.Brand;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;

/**
 * 商品品牌业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:15
 */
public interface IBrandService extends BaseSuperService<Brand, Long> {

	/**
	 * 根据条件分页获取品牌列表
	 *
	 * @param brandPageQuery 条件参数
	 * @return {@link IPage }<{@link Brand }>
	 * @since 2022-04-27 16:59:15
	 */
	IPage<Brand> brandsQueryPage(BrandPageQuery brandPageQuery);

	/**
	 * 删除品牌
	 *
	 * @param ids 品牌id
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:15
	 */
	Boolean deleteBrands(List<Long> ids);

	/**
	 * 根据分类ID获取品牌列表
	 *
	 * @param categoryId 分类ID
	 * @return {@link List }<{@link Brand }>
	 * @since 2022-04-27 16:59:15
	 */
	List<Brand> getBrandsByCategory(Long categoryId);

	/**
	 * 根据分类ID获取品牌列表
	 *
	 * @param categoryIds 分类ID
	 * @return {@link List }<{@link Brand }>
	 * @since 2022-04-27 16:59:15
	 */
	List<Brand> getBrandsByCategorys(Long categoryIds);

	/**
	 * 添加品牌
	 *
	 * @param brandDTO 品牌信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:15
	 */
	Boolean addBrand(BrandDTO brandDTO);

	/**
	 * 更新品牌
	 *
	 * @param brandDTO 品牌信息
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:15
	 */
	Boolean updateBrand(BrandDTO brandDTO);

	/**
	 * 更新品牌是否可用
	 *
	 * @param brandId 品牌ID
	 * @param disable 是否不可用
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:15
	 */
	Boolean brandDisable(Long brandId, boolean disable);

	/**
	 * 获取所有可用品牌
	 *
	 * @return {@link List }<{@link Brand }>
	 * @since 2022-04-27 16:59:15
	 */
	List<Brand> getAllAvailable();

}
