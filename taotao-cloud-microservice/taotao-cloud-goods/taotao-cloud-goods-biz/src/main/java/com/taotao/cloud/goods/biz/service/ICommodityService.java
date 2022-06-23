package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.goods.api.web.vo.CommoditySkuVO;
import com.taotao.cloud.goods.biz.model.entity.Commodity;

import java.util.List;

/**
 * 直播商品业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:47
 */
public interface ICommodityService extends IService<Commodity> {

	/**
	 * 添加直播商品
	 *
	 * @param commodity 直播商品列表
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:47
	 */
	Boolean addCommodity(List<Commodity> commodity);

	/**
	 * 删除直播商品
	 *
	 * @param goodsId 直播商品ID
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:47
	 */
	Boolean deleteCommodity(Long goodsId);

	/**
	 * 查询微信小程序直播商品审核状态
	 *
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:59:47
	 */
	Boolean getGoodsWareHouse();

	/**
	 * 查看直播商品分页
	 *
	 * @param pageParam   分页
	 * @param name        商品名称
	 * @param auditStatus 审核状态
	 * @return {@link IPage }<{@link CommoditySkuVO }>
	 * @since 2022-04-27 16:59:47
	 */
	IPage<CommoditySkuVO> commodityList(PageParam pageParam, String name, String auditStatus);
}
