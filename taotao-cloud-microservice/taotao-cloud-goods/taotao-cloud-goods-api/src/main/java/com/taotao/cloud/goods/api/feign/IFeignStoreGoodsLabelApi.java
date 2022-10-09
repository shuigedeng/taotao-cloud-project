package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.feign.fallback.FeignCategoryApiFallback;
import com.taotao.cloud.goods.api.model.vo.StoreGoodsLabelVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignCategoryApiFallback.class)
public interface IFeignStoreGoodsLabelApi {

	@GetMapping(value = "/product/info/id/{id:[0-9]*}")
	Result<List<StoreGoodsLabelVO>> listByStoreId(String id);
}
