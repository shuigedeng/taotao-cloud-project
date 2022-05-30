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
package com.taotao.cloud.payment.biz.controller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.dubbo.biz.entity.PayFlow;
import com.taotao.cloud.dubbo.biz.mapper.PayFlowMapper;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.payment.api.vo.PayFlowVO;
import com.taotao.cloud.payment.biz.service.IPayFlowService;
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
 * 支付流水管理API
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/13 09:58
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "支付流水管理API", description = "支付流水管理API")
@RequestMapping("/pay/flow")
public class PayFlowController {

	private final IPayFlowService payFlowService;

	@Operation(summary = "根据id查询支付信息", description = "根据id查询支付信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('pag:flow:info:id')")
	@GetMapping("/info/id/{id:[0-9]*}")
	public Result<PayFlowVO> findPayFlowById(@PathVariable(value = "id") Long id) {
		PayFlow payFlow = payFlowService.findPayFlowById(id);
		PayFlowVO vo = PayFlowMapper.INSTANCE.payFlowToPayFlowVO(payFlow);
		return Result.success(vo);
	}

}
