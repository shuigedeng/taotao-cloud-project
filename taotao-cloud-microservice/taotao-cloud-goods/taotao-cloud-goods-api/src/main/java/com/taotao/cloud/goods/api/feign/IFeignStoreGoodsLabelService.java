package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.fallback.FeignCategoryServiceFallback;
import com.taotao.cloud.goods.api.vo.StoreGoodsLabelVO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignCategoryServiceFallback.class)
public interface IFeignStoreGoodsLabelService {

	@GetMapping(value = "/product/info/id/{id:[0-9]*}")
	Result<List<StoreGoodsLabelVO>> listByStoreId(String id);
}
