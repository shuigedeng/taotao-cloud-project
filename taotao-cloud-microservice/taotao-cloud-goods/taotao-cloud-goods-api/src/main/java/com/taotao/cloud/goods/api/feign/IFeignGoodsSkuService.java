package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.goods.api.feign.fallback.FeignCategoryServiceFallback;
import com.taotao.cloud.goods.api.vo.GoodsSkuVO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignGoodsSkuService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignCategoryServiceFallback.class)
public interface IFeignGoodsSkuService {

	void updateGoodsStuck(List<GoodsSkuVO> goodsSkus);

	void updateBatchById(List<GoodsSkuVO> goodsSkus);

	List<GoodsSkuVO> getGoodsSkuByIdFromCache(List<Long> skuIds);

	GoodsSkuVO getGoodsSkuByIdFromCache(Long skuId);

	void getStock(String skuId);
}

