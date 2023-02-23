package com.taotao.cloud.store.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.store.api.feign.fallback.FeignStoreApiFallback;
import com.taotao.cloud.store.api.model.vo.FreightTemplateInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用店铺模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignFreightTemplateService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignStoreApiFallback.class)
public interface IFeignFreightTemplateApi {
	@GetMapping(value = "/getById")
	FreightTemplateInfoVO getById(Long templateId);
}

