package com.taotao.cloud.store.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.store.api.feign.fallback.FeignStoreServiceFallbackImpl;
import com.taotao.cloud.store.api.vo.FreightTemplateInfoVO;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用店铺模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignFreightTemplateService", value = ServiceName.TAOTAO_CLOUD_GOODS, fallbackFactory = FeignStoreServiceFallbackImpl.class)
public interface IFeignFreightTemplateService {

	Result<FreightTemplateInfoVO> getById(Long templateId);
}

