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
@FeignClient(name = ServiceName.TAOTAO_CLOUD_SYS, fallbackFactory = FeignLogisticsFallback.class)
public interface IFeignLogisticsService {

	/**
	 * 通过id
	 *
	 * @param logisticsId 物流id
	 * @return {@link Result }<{@link LogisticsVO }>
	 * @since 2022-04-25 16:47:29
	 */
	@GetMapping("/logistic/codes")
	Result<LogisticsVO> getById(@RequestParam String logisticsId);

	/**
	 * 得到物流
	 *
	 * @param logisticsId 物流id
	 * @param logisticsNo 物流编码
	 * @return {@link Result }<{@link TracesVO }>
	 * @since 2022-04-25 16:47:32
	 */
	@GetMapping("/logistic/info")
	Result<TracesVO> getLogistic(@RequestParam Long logisticsId, @RequestParam String logisticsNo);
}
