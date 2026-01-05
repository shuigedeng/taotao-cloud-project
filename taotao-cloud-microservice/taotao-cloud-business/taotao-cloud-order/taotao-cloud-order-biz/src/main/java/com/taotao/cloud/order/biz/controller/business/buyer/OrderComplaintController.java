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

package com.taotao.cloud.order.biz.controller.business.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.data.mybatis.mybatisplus.utils.MpUtils;
import com.taotao.cloud.order.api.enums.order.CommunicationOwnerEnum;
import com.taotao.cloud.order.sys.model.dto.order.OrderComplaintCommunicationDTO;
import com.taotao.cloud.order.sys.model.dto.order.OrderComplaintDTO;
import com.taotao.cloud.order.sys.model.page.order.OrderComplaintPageQuery;
import com.taotao.cloud.order.sys.model.vo.order.OrderComplaintBaseVO;
import com.taotao.cloud.order.sys.model.vo.order.OrderComplaintVO;
import com.taotao.cloud.order.biz.model.entity.order.OrderComplaint;
import com.taotao.cloud.order.biz.model.entity.order.OrderComplaintCommunication;
import com.taotao.cloud.order.biz.service.business.order.OrderComplaintCommunicationService;
import com.taotao.cloud.order.biz.service.business.order.OrderComplaintService;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.web.request.annotation.RequestLogger;
import com.taotao.boot.web.utils.OperationalJudgment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,交易投诉API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:56:57
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-交易投诉API", description = "买家端-交易投诉API")
@RequestMapping("/order/buyer/order/complain")
public class OrderComplaintController {

    /** 交易投诉 */
    private final OrderComplaintService orderComplaintService;

    /** 交易投诉沟通 */
    private final OrderComplaintCommunicationService orderComplaintCommunicationService;

    @Operation(summary = "通过id获取", description = "通过id获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{id}")
    public Result<OrderComplaintVO> get(@PathVariable Long id) {
        OrderComplaintVO orderComplaintVO =
                OperationalJudgment.judgment(orderComplaintService.getOrderComplainById(id));
        return Result.success(orderComplaintVO);
    }

    @Operation(summary = "分页获取", description = "分页获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<OrderComplaintBaseVO>> get(@Validated OrderComplaintPageQuery orderComplaintPageQuery) {
        IPage<OrderComplaint> orderComplainByPage = orderComplaintService.pageQuery(orderComplaintPageQuery);
        return Result.success(MpUtils.convertMybatisPage(orderComplainByPage, OrderComplaintBaseVO.class));
    }

    @Operation(summary = "添加交易投诉", description = "添加交易投诉")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping
    public Result<OrderComplaint> add(@Valid OrderComplaintDTO orderComplaintDTO) {
        return Result.success(orderComplaintService.addOrderComplain(orderComplaintDTO));
    }

    @Operation(summary = "添加交易投诉对话", description = "添加交易投诉对话")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping("/communication/{complainId}")
    public Result<Boolean> addCommunication(
            @PathVariable("complainId") Long complainId,
            @Validated @RequestBody OrderComplaintCommunicationDTO orderComplaintCommunicationDTO) {
        SecurityUser user = SecurityUtils.getCurrentUser();
        OrderComplaintCommunication orderComplaintCommunication = OrderComplaintCommunication.builder()
                .complainId(complainId)
                .content(orderComplaintCommunicationDTO.content())
                .owner(CommunicationOwnerEnum.BUYER.name())
                .ownerName(user.getNickname())
                .ownerId(user.getUserId())
                .build();

        return Result.success(orderComplaintCommunicationService.addCommunication(orderComplaintCommunication));
    }

    @Operation(summary = "取消售后", description = "取消售后")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/status/{id}")
    public Result<Boolean> cancel(@PathVariable Long id) {
        return Result.success(orderComplaintService.cancel(id));
    }
}
