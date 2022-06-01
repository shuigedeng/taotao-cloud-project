package com.taotao.cloud.promotion.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.promotion.biz.entity.PointsGoodsCategory;

/**
 * 积分商品分类业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:44:02
 */
public interface PointsGoodsCategoryService extends IService<PointsGoodsCategory> {

	/**
	 * 添加积分商品分类
	 *
	 * @param pointsGoodsCategory 积分商品分类信息
	 * @return boolean
	 * @since 2022-04-27 16:44:02
	 */
	boolean addCategory(PointsGoodsCategory pointsGoodsCategory);

	/**
	 * 更新积分商品分类
	 *
	 * @param pointsGoodsCategory 积分商品分类信息
	 * @return boolean
	 * @since 2022-04-27 16:44:02
	 */
	boolean updateCategory(PointsGoodsCategory pointsGoodsCategory);

	/**
	 * 删除积分商品类型
	 *
	 * @param id 积分商品分类id
	 * @return boolean
	 * @since 2022-04-27 16:44:02
	 */
	boolean deleteCategory(String id);

	/**
	 * 分页获取积分商品类型
	 *
	 * @param name 类型名称
	 * @param page 分页参数
	 * @return {@link IPage }<{@link PointsGoodsCategory }>
	 * @since 2022-04-27 16:44:02
	 */
	IPage<PointsGoodsCategory> getCategoryByPage(String name, PageParam page);

	/**
	 * 获取积分商品类型详情
	 *
	 * @param id 积分商品类型id
	 * @return {@link PointsGoodsCategory }
	 * @since 2022-04-27 16:44:02
	 */
	PointsGoodsCategory getCategoryDetail(String id);

}
