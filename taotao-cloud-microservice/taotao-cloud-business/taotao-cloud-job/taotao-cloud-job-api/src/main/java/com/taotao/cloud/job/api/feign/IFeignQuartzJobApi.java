package com.taotao.cloud.job.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.job.api.feign.fallback.FeignQuartzJobFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_JOB, fallbackFactory = FeignQuartzJobFallbackImpl.class)
public interface IFeignQuartzJobApi {

}

