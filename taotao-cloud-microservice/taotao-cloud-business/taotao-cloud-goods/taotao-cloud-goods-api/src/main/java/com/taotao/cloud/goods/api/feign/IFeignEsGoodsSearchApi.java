package com.taotao.cloud.goods.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.goods.api.feign.fallback.FeignCategoryApiFallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignEsGoodsSearchService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignCategoryApiFallback.class)
public interface IFeignEsGoodsSearchApi {

}

