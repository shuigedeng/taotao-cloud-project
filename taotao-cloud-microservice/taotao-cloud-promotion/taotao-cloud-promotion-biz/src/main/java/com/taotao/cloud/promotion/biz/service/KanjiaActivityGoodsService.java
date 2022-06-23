package com.taotao.cloud.promotion.biz.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.promotion.api.web.dto.KanjiaActivityGoodsDTO;
import com.taotao.cloud.promotion.api.web.dto.KanjiaActivityGoodsOperationDTO;
import com.taotao.cloud.promotion.api.web.vo.kanjia.KanjiaActivityGoodsListVO;
import com.taotao.cloud.promotion.api.web.vo.kanjia.KanjiaActivityGoodsParams;
import com.taotao.cloud.promotion.api.web.vo.kanjia.KanjiaActivityGoodsVO;
import com.taotao.cloud.promotion.biz.model.entity.KanjiaActivityGoods;

import java.util.List;


/**
 * 砍价业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:43:36
 */
public interface KanjiaActivityGoodsService extends IService<KanjiaActivityGoods> {


	/**
	 * 添加砍价活动商品
	 *
	 * @param kanJiaActivityGoodsDTOS 砍价商品
	 * @return {@link Boolean }
	 * @since 2022-04-27 16:43:36
	 */
	Boolean add(KanjiaActivityGoodsOperationDTO kanJiaActivityGoodsDTOS);

	/**
	 * 查询砍价活动商品分页信息
	 *
	 * @param kanJiaActivityGoodsParams 砍价活动商品
	 * @param pageVO                    分页信息
	 * @return {@link IPage }<{@link KanjiaActivityGoods }>
	 * @since 2022-04-27 16:43:36
	 */
	IPage<KanjiaActivityGoods> getForPage(KanjiaActivityGoodsParams kanJiaActivityGoodsParams, PageParam pageVO);

	/**
	 * 查询砍价活动商品分页信息
	 *
	 * @param kanJiaActivityGoodsParams 砍价活动商品
	 * @param pageVO                    分页信息
	 * @return {@link IPage }<{@link KanjiaActivityGoodsListVO }>
	 * @since 2022-04-27 16:43:36
	 */
	IPage<KanjiaActivityGoodsListVO> kanjiaGoodsVOPage(KanjiaActivityGoodsParams kanJiaActivityGoodsParams, PageParam pageVO);

	/**
	 * 查询砍价活动商品
	 *
	 * @param goodsId 砍价活动商品id
	 * @return {@link KanjiaActivityGoodsDTO }
	 * @since 2022-04-27 16:43:36
	 */
	KanjiaActivityGoodsDTO getKanjiaGoodsDetail(String goodsId);

	/**
	 * 根据SkuId获取正在进行中的砍价商品
	 *
	 * @param skuId 商品规格Id
	 * @return {@link KanjiaActivityGoods }
	 * @since 2022-04-27 16:43:36
	 */
	KanjiaActivityGoods getKanjiaGoodsBySkuId(String skuId);

	/**
	 * 查询砍价活动商品VO
	 *
	 * @param id 砍价活动商品ID
	 * @return {@link KanjiaActivityGoodsVO }
	 * @since 2022-04-27 16:43:36
	 */
	KanjiaActivityGoodsVO getKanJiaGoodsVO(String id);

	/**
	 * 修改看见商品信息
	 *
	 * @param kanjiaActivityGoodsDTO 砍价商品信息
	 * @return boolean
	 * @since 2022-04-27 16:43:36
	 */
	boolean updateKanjiaActivityGoods(KanjiaActivityGoodsDTO kanjiaActivityGoodsDTO);

	/**
	 * 删除砍价商品
	 *
	 * @param ids 砍价商品ids
	 * @return boolean
	 * @since 2022-04-27 16:43:36
	 */
	boolean deleteKanJiaGoods(List<String> ids);

}
