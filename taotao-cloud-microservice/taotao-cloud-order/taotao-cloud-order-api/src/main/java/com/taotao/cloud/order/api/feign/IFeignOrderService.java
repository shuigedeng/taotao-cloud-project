package com.taotao.cloud.order.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.order.api.dto.order_info.OrderSaveDTO;
import com.taotao.cloud.order.api.feign.fallback.FeignOrderFallbackImpl;
import com.taotao.cloud.order.api.vo.order_info.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "RemoteOrderService", value = ServiceName.TAOTAO_CLOUD_ORDER, fallbackFactory = FeignOrderFallbackImpl.class)
public interface IFeignOrderService {

    @GetMapping(value = "/order/info/{code}")
    Result<OrderVO> findOrderInfoByCode(@PathVariable("code") String code);

    @PostMapping(value = "/order")
    Result<OrderVO> saveOrder(@RequestBody OrderSaveDTO orderDTO);

}

