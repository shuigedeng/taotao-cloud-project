package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.goods.api.feign.fallback.FeignCategoryServiceFallback;
import com.taotao.cloud.goods.api.vo.StoreGoodsLabelVO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(contextId = "IFeignGoodsSkuService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignCategoryServiceFallback.class)
public interface IFeignStoreGoodsLabelService {

	List<StoreGoodsLabelVO> listByStoreId(String id);
}
