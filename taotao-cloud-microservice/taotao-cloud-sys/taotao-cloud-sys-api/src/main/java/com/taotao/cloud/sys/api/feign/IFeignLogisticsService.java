package com.taotao.cloud.sys.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.api.feign.fallback.FeignLogisticsFallback;
import com.taotao.cloud.sys.api.feign.fallback.FeignUserFallback;
import com.taotao.cloud.sys.api.vo.logistics.LogisticsVO;
import com.taotao.cloud.sys.api.vo.logistics.TracesVO;
import com.taotao.cloud.sys.api.vo.menu.MenuQueryVO;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用物流公司模块
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:09:10
 */
@FeignClient(contextId = "IFeignLogisticsService", value = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignLogisticsFallback.class)
public interface IFeignLogisticsService {

	@GetMapping("/resource/info/codes")
	Result<LogisticsVO> getById(@RequestParam String logisticsId);

	@GetMapping("/resource/info/codes")
	Result<TracesVO> getLogistic(@RequestParam Long logisticsId, @RequestParam String logisticsNo);
}
