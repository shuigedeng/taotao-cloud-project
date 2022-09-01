package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.fallback.FeignCategoryServiceFallback;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignGoodsSkuService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignCategoryServiceFallback.class)
public interface IFeignGoodsSkuService {

	@PostMapping(value = "/product/info/id/{id:[0-9]*}")
	Result<Boolean> updateGoodsStuck(List<GoodsSkuSpecGalleryVO> goodsSkus);

	@PostMapping(value = "/product/info/id/{id:[0-9]*}")
	Result<Boolean> updateBatchById(List<GoodsSkuSpecGalleryVO> goodsSkus);

	@GetMapping(value = "/product/info/id/{id:[0-9]*}")
	Result<List<GoodsSkuSpecGalleryVO>> getGoodsSkuByIdFromCache(List<Long> skuIds);

	@GetMapping(value = "/product/info/id/{id:[0-9]*}")
	Result<GoodsSkuSpecGalleryVO> getGoodsSkuByIdFromCache(Long skuId);

	@GetMapping(value = "/product/info/id/{id:[0-9]*}")
	Result<Integer> getStock(String skuId);
}

