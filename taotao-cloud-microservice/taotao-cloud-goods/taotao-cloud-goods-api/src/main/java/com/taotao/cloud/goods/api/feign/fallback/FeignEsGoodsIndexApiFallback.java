package com.taotao.cloud.goods.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.IFeignEsGoodsIndexApi;
import com.taotao.cloud.goods.api.model.vo.EsGoodsIndexVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * FeignEsGoodsIndexServiceFallback
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignEsGoodsIndexApiFallback implements FallbackFactory<IFeignEsGoodsIndexApi> {
	@Override
	public IFeignEsGoodsIndexApi create(Throwable throwable) {
		return new IFeignEsGoodsIndexApi() {
			@Override
			public List<EsGoodsIndexVO> getEsGoodsBySkuIds(List<String> skuIdList) {
				return null;
			}

			@Override
			public Boolean cleanInvalidPromotion() {
				return null;
			}

		};
	}
}
