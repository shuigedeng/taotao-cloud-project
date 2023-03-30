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

package com.taotao.cloud.order.biz.controller.business.mall; // /*
//  * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  *      https://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
// package com.taotao.cloud.order.biz.controller;
//
// import com.taotao.cloud.common.model.BaseQuery;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.logger.annotation.RequestLogger;
// import com.taotao.cloud.order.api.bo.order_info.OrderBO;
// import com.taotao.cloud.order.api.dto.order_info.OrderSaveDTO;
// import com.taotao.cloud.order.api.dto.order_info.OrderUpdateDTO;
// import com.taotao.cloud.order.api.vo.order_info.OrderVO;
// import com.taotao.cloud.order.biz.entity.order.OrderInfo;
// import com.taotao.cloud.order.biz.service.IOrderInfoService;
// import com.taotao.cloud.web.base.controller.SuperController;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import jakarta.validation.constraints.NotNull;
// import java.util.List;
//
// /**
//  * 订单管理API
//  *
//  * @author shuigedeng
//  * @version 2021.10
//  * @since 2021-10-09 16:23:29
//  */
// @Validated
// @RestController
// @RequestMapping("/order")
// @Tag(name = "订单管理API", description = "订单管理API")
// public class OrderInfoController extends
// 	SuperController<IOrderInfoService, OrderInfo, Long, BaseQuery, OrderSaveDTO, OrderUpdateDTO,
// OrderVO> {
//
// 	@Operation(summary = "根据父id查询地区数据", description = "根据父id查询地区数据")
// 	@RequestLogger
// 	@GetMapping("/parentId/{parentId}")
// 	//@PreAuthorize("hasAuthority('sys:region:info:parentId')")
// 	public Result<List<OrderBO>> queryRegionByParentId(
// 		@Parameter(description = "父id") @NotNull(message = "父id不能为空")
// 		@PathVariable(name = "parentId") Long parentId) {
// 		List<OrderBO> result = service().queryRegionByParentId(parentId);
// 		return Result.success(result);
// 	}
// }
//
