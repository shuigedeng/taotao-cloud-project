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

package com.taotao.cloud.member.biz.controller.business.seller;
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.taotao.boot.common.model.Result;
// import com.taotao.boot.webmvc.utils.OperationalJudgment;
// import com.taotao.boot.logger.annotation.RequestLogger;
// import com.taotao.cloud.store.api.query.BillPageQuery;
// import com.taotao.cloud.store.api.vo.BillListVO;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import java.util.Objects;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.*;
// import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// /**
//  * 店铺端,结算单API
//  *
//  * @since 2020/11/17 4:29 下午
//  */
// @AllArgsConstructor
// @Validated
// @RestController
// @Tag(name = "店铺端-结算单API", description = "店铺端-结算单API")
// @RequestMapping("/member/seller/bill/store")
// public class BillStoreController {
//
// 	private final BillService billService;
// 	private final StoreFlowService storeFlowService;
//
// 	@Operation(summary = "获取结算单分页", description = "获取结算单分页")
// 	@RequestLogger
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping(value = "/page")
// 	public Result<IPage<BillListVO>> getByPage(BillPageQuery billPageQuery) {
// 		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
// 		billPageQuery.setStoreId(storeId);
// 		return Result.success(billService.billPage(billPageQuery));
// 	}
//
// 	@Operation(summary = "通过id获取结算单", description = "通过id获取结算单")
// 	@RequestLogger
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping(value = "/{id}")
// 	public Result<Bill> get(@PathVariable String id) {
// 		return Result.success(OperationalJudgment.judgment(billService.getById(id)));
// 	}
//
// 	@Operation(summary = "获取商家结算单流水分页", description = "获取商家结算单流水分页")
// 	@RequestLogger
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping(value = "/flow/{id}")
// 	public Result<IPage<StoreFlow>> getStoreFlow(@PathVariable String id,
// 		@Parameter(description = "流水类型:PAY、REFUND", required = true) String flowType,
// 		PageVO pageVO) {
// 		OperationalJudgment.judgment(billService.getById(id));
// 		return Result.success(storeFlowService.getStoreFlow(id, flowType, pageVO));
// 	}
//
// 	@Operation(summary = "获取商家分销订单流水分页", description = "获取商家分销订单流水分页")
// 	@RequestLogger
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping(value = "/distribution/flow/{id}")
// 	public Result<IPage<StoreFlow>> getDistributionFlow(@PathVariable String id, PageVO pageVO) {
// 		OperationalJudgment.judgment(billService.getById(id));
// 		return Result.success(storeFlowService.getDistributionFlow(id, pageVO));
// 	}
//
// 	@Operation(summary = "核对结算单", description = "核对结算单")
// 	@RequestLogger
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@PutMapping(value = "/check/{id}")
// 	public Result<Object> examine(@PathVariable String id) {
// 		OperationalJudgment.judgment(billService.getById(id));
// 		billService.check(id);
// 		return Result.success();
// 	}
//
// 	@Operation(summary = "下载结算单", description = "下载结算单")
// 	@RequestLogger
// 	@PreAuthorize("@el.check('admin','timing:list')")
// 	@GetMapping(value = "/download/{id}")
// 	public void downLoadDeliverExcel(@PathVariable String id) {
// 		OperationalJudgment.judgment(billService.getById(id));
// 		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
// 		billService.download(response, id);
// 	}
//
// }
