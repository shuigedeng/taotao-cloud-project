package com.taotao.cloud.order.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.order.api.feign.fallback.FeignOrderApiFallback;
import com.taotao.cloud.order.api.model.dto.order_info.OrderSaveDTO;
import com.taotao.cloud.order.api.model.vo.order.OrderDetailVO;
import com.taotao.cloud.order.api.model.vo.order.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_ORDER, fallbackFactory = FeignOrderApiFallback.class)
public interface IFeignOrderApi {

	@GetMapping(value = "/order/info/{code}")
	OrderVO findOrderInfoByCode(@PathVariable("code") String code);

	@PostMapping(value = "/order")
	OrderVO saveOrder(@RequestBody OrderSaveDTO orderDTO);

	@PostMapping(value = "/order/item")
	OrderDetailVO queryDetail(String sn);

	@PostMapping(value = "/order/item")
	Boolean payOrder(String sn, String paymentMethod, String receivableNo);

	@PostMapping(value = "/order/item")
	OrderVO getBySn(String sn);

	@PostMapping(value = "/order/item")
	List<OrderVO> getByTradeSn(String sn);
}

