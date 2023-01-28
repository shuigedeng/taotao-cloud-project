package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.fallback.FeignCategoryApiFallback;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
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
@FeignClient(contextId = "IFeignGoodsSkuService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignCategoryApiFallback.class)
public interface IFeignGoodsSkuApi {

	@PostMapping(value = "/product/updateGoodsStuck")
	Boolean updateGoodsStuck(List<GoodsSkuSpecGalleryVO> goodsSkus);

	@PostMapping(value = "/product/updateBatchById}")
	Boolean updateBatchById(List<GoodsSkuSpecGalleryVO> goodsSkus);

	@GetMapping(value = "/product/getGoodsSkuByIdFromCache")
	List<GoodsSkuSpecGalleryVO> getGoodsSkuByIdFromCache(List<Long> skuIds);

	@GetMapping(value = "/product/getGoodsSkuByIdFromCache}")
	GoodsSkuSpecGalleryVO getGoodsSkuByIdFromCache(Long skuId);

	@GetMapping(value = "/product/getStock")
	Integer getStock(String skuId);
}

