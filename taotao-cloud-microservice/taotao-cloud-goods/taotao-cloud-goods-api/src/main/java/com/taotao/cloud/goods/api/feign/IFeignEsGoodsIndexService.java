package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.fallback.FeignCategoryServiceFallback;
import com.taotao.cloud.goods.api.vo.EsGoodsIndexVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignEsGoodsIndexService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignCategoryServiceFallback.class)
public interface IFeignEsGoodsIndexService {


	@GetMapping(value = "/product/info/id/{id:[0-9]*}")
	List<EsGoodsIndexVO> getEsGoodsBySkuIds(List<String> skuIdList);

	@PostMapping(value = "/product/info/id/{id:[0-9]*}")
	Result<Boolean> cleanInvalidPromotion();

}

