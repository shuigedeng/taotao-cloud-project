package com.taotao.cloud.generator.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.generator.api.feign.fallback.FeignGeneratorFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignGeneratorApi", value = ServiceName.TAOTAO_CLOUD_GENERATOR, fallbackFactory = FeignGeneratorFallbackImpl.class)
public interface IFeignGeneratorApi {

}

