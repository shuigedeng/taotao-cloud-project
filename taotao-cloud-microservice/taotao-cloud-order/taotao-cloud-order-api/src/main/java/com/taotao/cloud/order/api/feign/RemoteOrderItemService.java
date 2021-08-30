package com.taotao.cloud.order.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.order.api.dto.OrderItemDTO;
import com.taotao.cloud.order.api.feign.fallback.RemoteOrderItemFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用订单项模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteOrderService", value = ServiceName.TAOTAO_CLOUD_ORDER_CENTER, fallbackFactory = RemoteOrderItemFallbackImpl.class)
public interface RemoteOrderItemService {

	@PostMapping(value = "/orderItem/save")
	Result<Boolean> saveOrderItem(@RequestBody OrderItemDTO orderItemDTO);
}

