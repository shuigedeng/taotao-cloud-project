package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.fallback.FeignCategoryApiFallback;
import com.taotao.cloud.goods.api.model.vo.EsGoodsIndexVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignEsGoodsIndexService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignCategoryApiFallback.class)
public interface IFeignEsGoodsIndexApi {

	@GetMapping(value = "/product/info/id/{id:[0-9]*}")
	List<EsGoodsIndexVO> getEsGoodsBySkuIds(List<String> skuIdList);

	@PostMapping(value = "/product/info/id/{id:[0-9]*}")
	Boolean cleanInvalidPromotion();

}

