package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.IFeignEsGoodsIndexService;
import com.taotao.cloud.goods.api.web.vo.EsGoodsIndexVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * FeignEsGoodsIndexServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignEsGoodsIndexServiceFallback implements FallbackFactory<IFeignEsGoodsIndexService> {
	@Override
	public IFeignEsGoodsIndexService create(Throwable throwable) {
		return new IFeignEsGoodsIndexService() {
			@Override
			public List<EsGoodsIndexVO> getEsGoodsBySkuIds(List<String> skuIdList) {
				return null;
			}

			@Override
			public Result<Boolean> cleanInvalidPromotion() {
				return null;
			}
		};
	}
}
