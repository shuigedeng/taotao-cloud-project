package com.taotao.cloud.order.api.feign;

import static com.taotao.cloud.openfeign.api.VersionEnum.V2022_07;
import static com.taotao.cloud.openfeign.api.VersionEnum.V2022_08;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.openfeign.api.ApiInfo;
import com.taotao.cloud.openfeign.api.ApiInfo.Create;
import com.taotao.cloud.openfeign.api.ApiInfo.Update;
import com.taotao.cloud.order.api.feign.fallback.FeignOrderApiFallback;
import com.taotao.cloud.order.api.model.dto.order_info.OrderSaveDTO;
import com.taotao.cloud.order.api.model.vo.order.OrderDetailVO;
import com.taotao.cloud.order.api.model.vo.order.OrderVO;
import java.util.List;
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
@FeignClient(value = ServiceName.TAOTAO_CLOUD_ORDER, fallbackFactory = FeignOrderApiFallback.class)
public interface IFeignOrderApi {

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping(value = "/order/feign/info/{code}")
	OrderVO findOrderInfoByCode(@PathVariable("code") String code);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@PostMapping(value = "/order/feign/saveOrder")
	OrderVO saveOrder(@RequestBody OrderSaveDTO orderDTO);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping(value = "/order/feign/queryDetail")
	OrderDetailVO queryDetail(String sn);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@PostMapping(value = "/order/feign/payOrder")
	Boolean payOrder(String sn, String paymentMethod, String receivableNo);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping(value = "/order/feign/getBySn")
	OrderVO getBySn(String sn);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		}
	)
	@GetMapping(value = "/order/feign/getByTradeSn")
	List<OrderVO> getByTradeSn(String sn);
}

