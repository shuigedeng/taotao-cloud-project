package com.taotao.cloud.order.biz.service.business.cart.render.impl;

import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.common.utils.number.CurrencyUtils;
import com.taotao.cloud.order.api.enums.cart.RenderStepEnum;
import com.taotao.cloud.order.api.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.model.vo.cart.CartSkuVO;
import com.taotao.cloud.order.biz.service.business.cart.render.ICartRenderStep;
import com.taotao.cloud.redis.repository.RedisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分销佣金计算
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:47
 */
@AllArgsConstructor
@Service
public class DistributionPriceRender implements ICartRenderStep {

	/**
	 * 缓存
	 */
	private final RedisRepository redisRepository;

	private final DistributionGoodsService distributionGoodsService;

	@Override
	public RenderStepEnum step() {
		return RenderStepEnum.DISTRIBUTION;
	}

	@Override
	public void render(TradeDTO tradeDTO) {
		this.renderDistribution(tradeDTO);
	}

	/**
	 * 渲染分销佣金
	 *
	 * @param tradeDTO tradeDTO
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
					CurrencyUtils.mul(cartSkuVO.getNum(),
						cartSkuVO.getDistributionGoods().getCommission()));
			}
		}

	}
}
