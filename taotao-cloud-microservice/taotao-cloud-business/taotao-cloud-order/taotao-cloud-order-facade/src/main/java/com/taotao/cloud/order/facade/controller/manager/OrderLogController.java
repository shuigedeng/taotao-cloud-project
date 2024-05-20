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

package com.taotao.cloud.order.facade.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.data.mybatis.mybatisplus.utils.MpUtils;
import com.taotao.cloud.order.application.command.order.OrderLogPageQuery;
import com.taotao.cloud.order.application.command.order.OrderLogVO;
import com.taotao.cloud.order.application.service.trade.IOrderLogService;
import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderLog;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,订单日志管理API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:19
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-订单日志管理API", description = "管理端-订单日志管理API")
@RequestMapping("/order/manager/order/log")
public class OrderLogController {

	private final IOrderLogService orderLogService;

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<OrderLog> get(@PathVariable String id) {
		return Result.success(orderLogService.getById(id));
	}

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageResult<OrderLogVO>> getByPage(OrderLogPageQuery orderLogPageQuery) {
		IPage<OrderLog> orderLogPage = orderLogService.pageQuery(orderLogPageQuery);
		return Result.success(MpUtils.convertMybatisPage(orderLogPage, OrderLogVO.class));
	}
}
