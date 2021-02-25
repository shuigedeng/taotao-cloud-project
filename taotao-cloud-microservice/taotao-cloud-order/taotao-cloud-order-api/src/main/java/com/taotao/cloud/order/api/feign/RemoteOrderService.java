package com.taotao.cloud.order.api.feign;

import com.taotao.cloud.common.constant.ServiceNameConstant;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.order.api.dto.OrderDTO;
import com.taotao.cloud.order.api.feign.fallback.RemoteOrderFallbackImpl;
import com.taotao.cloud.order.api.vo.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用订单模块
 *
 * @author dengtao
 * @date 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteOrderService", value = ServiceNameConstant.TAOTAO_CLOUD_ORDER_CENTER, fallbackFactory = RemoteOrderFallbackImpl.class)
public interface RemoteOrderService {

    @GetMapping(value = "/order/info/{code}")
    Result<OrderVO> findOrderInfoByCode(@PathVariable("code") String code);

    @PostMapping(value = "/order")
    Result<OrderVO> saveOrder(@RequestBody OrderDTO orderDTO);

}

