/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.order.api.feign;


import static com.taotao.boot.common.support.info.ApiVersionEnum.V2022_07;
import static com.taotao.boot.common.support.info.ApiVersionEnum.V2022_08;

import com.taotao.boot.common.constant.ServiceName;
import com.taotao.boot.common.support.info.ApiInfo;
import com.taotao.boot.common.support.info.Create;
import com.taotao.boot.common.support.info.Update;
import com.taotao.cloud.order.api.feign.fallback.FeignOrderApiFallback;
import com.taotao.cloud.order.api.feign.request.OrderSaveApiRequest;
import com.taotao.cloud.order.api.feign.response.OrderDetailApiResponse;
import com.taotao.cloud.order.api.feign.response.OrderApiResponse;
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
public interface OrderApi {

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		})
	@GetMapping(value = "/order/feign/info/{code}")
	OrderApiResponse findOrderInfoByCode(@PathVariable("code") String code);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		})
	@PostMapping(value = "/order/feign/saveOrder")
	OrderApiResponse saveOrder(@RequestBody OrderSaveApiRequest orderDTO);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		})
	@GetMapping(value = "/order/feign/queryDetail")
	OrderDetailApiResponse queryDetail(String sn);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		})
	@PostMapping(value = "/order/feign/payOrder")
	Boolean payOrder(String sn, String paymentMethod, String receivableNo);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		})
	@GetMapping(value = "/order/feign/getBySn")
	OrderApiResponse getBySn(String sn);

	@ApiInfo(
		create = @Create(version = V2022_07, date = "2022-07-01 17:11:55"),
		update = {
			@Update(version = V2022_07, content = "主要修改了配置信息的接口查询", date = "2022-07-01 17:11:55"),
			@Update(version = V2022_08, content = "主要修改了配置信息的接口查询08", date = "2022-07-01 17:11:55")
		})
	@GetMapping(value = "/order/feign/getByTradeSn")
	List<OrderApiResponse> getByTradeSn(String sn);
}
