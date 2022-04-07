package com.taotao.cloud.order.biz.service.cart.render.impl;

import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.common.utils.number.CurrencyUtil;
import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.enums.cart.RenderStepEnums;
import com.taotao.cloud.order.api.vo.cart.CartSkuVO;
import com.taotao.cloud.order.biz.service.cart.render.CartRenderStep;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分销佣金计算
 */
@Service
public class DistributionPriceRender implements CartRenderStep {

	/**
	 * 缓存
	 */
	@Autowired
	private Cache cache;

	@Autowired
	private DistributionGoodsService distributionGoodsService;

	@Override
	public RenderStepEnums step() {
		return RenderStepEnums.DISTRIBUTION;
	}

	@Override
	public void render(TradeDTO tradeDTO) {
		this.renderDistribution(tradeDTO);
	}

	/**
	 * 渲染分销佣金
	 *
	 * @param tradeDTO
	 */
	private void renderDistribution(TradeDTO tradeDTO) {

		//如果存在分销员
		String distributionId = (String) cache.get(
			CachePrefix.DISTRIBUTION.getPrefix() + "_" + tradeDTO.getMemberId());
		if (StringUtil.isEmpty(distributionId)) {
			return;
		}
		//循环订单商品列表，如果是分销商品则计算商品佣金
		tradeDTO.setDistributionId(distributionId);

		List<String> skuIds = tradeDTO.getCheckedSkuList().stream().map(cartSkuVO -> {
			return cartSkuVO.getGoodsSku().getId();
		}).collect(Collectors.toList());
		//是否包含分销商品
		List<DistributionGoods> distributionGoods = distributionGoodsService.distributionGoods(
			skuIds);
		if (distributionGoods != null && distributionGoods.size() > 0) {
			distributionGoods.forEach(dg -> {
				tradeDTO.getCheckedSkuList().forEach(cartSkuVO -> {
					if (cartSkuVO.getGoodsSku().getId().equals(dg.getSkuId())) {
						cartSkuVO.setDistributionGoods(dg);
					}
				});
			});
		}

		for (CartSkuVO cartSkuVO : tradeDTO.getCheckedSkuList()) {
			if (cartSkuVO.getDistributionGoods() != null) {
				cartSkuVO.getPriceDetailDTO().setDistributionCommission(
					CurrencyUtil.mul(cartSkuVO.getNum(),
						cartSkuVO.getDistributionGoods().getCommission()));
			}
		}

	}
}
