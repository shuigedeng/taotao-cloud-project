package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.api.vo.CategoryBrandVO;
import com.taotao.cloud.goods.biz.entity.CategoryBrand;
import java.util.List;

/**
 * 商品分类品牌业务层
 */
public interface CategoryBrandService extends IService<CategoryBrand> {

	/**
	 * 根据分类id查询品牌信息
	 *
	 * @param categoryId 分类id
	 * @return 分类品牌关联信息列表
	 */
	List<CategoryBrandVO> getCategoryBrandList(String categoryId);

	/**
	 * 通过分类ID删除关联品牌
	 *
	 * @param categoryId 品牌ID
	 */
	Boolean deleteByCategoryId(String categoryId);

	/**
	 * 根据品牌ID获取分类品牌关联信息
	 *
	 * @param brandId 品牌ID
	 * @return 分类品牌关联信息
	 */
	List<CategoryBrand> getCategoryBrandListByBrandId(List<String> brandId);

	/**
	 * 保存分类品牌关系
	 *
	 * @param categoryId 分类id
	 * @param brandIds   品牌ids
	 */
	Boolean saveCategoryBrandList(String categoryId, List<String> brandIds);

}
