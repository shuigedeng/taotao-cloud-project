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

package com.taotao.cloud.order.biz.controller.business.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.order.api.enums.order.CommunicationOwnerEnum;
import com.taotao.cloud.order.sys.model.dto.order.OrderComplaintCommunicationDTO;
import com.taotao.cloud.order.sys.model.dto.order.OrderComplaintDTO;
import com.taotao.cloud.order.sys.model.dto.order.OrderComplaintOperationDTO;
import com.taotao.cloud.order.sys.model.dto.order.StoreAppealDTO;
import com.taotao.cloud.order.sys.model.page.order.OrderComplaintPageQuery;
import com.taotao.cloud.order.sys.model.vo.order.OrderComplaintBaseVO;
import com.taotao.cloud.order.sys.model.vo.order.OrderComplaintVO;
import com.taotao.cloud.order.biz.model.convert.OrderComplainConvert;
import com.taotao.cloud.order.biz.model.entity.order.OrderComplaint;
import com.taotao.cloud.order.biz.model.entity.order.OrderComplaintCommunication;
import com.taotao.cloud.order.biz.service.business.order.IOrderComplaintCommunicationService;
import com.taotao.cloud.order.biz.service.business.order.IOrderComplaintService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import com.taotao.boot.web.utils.OperationalJudgment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * 店铺端,交易投诉API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:35
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-交易投诉API", description = "店铺端-交易投诉API")
@RequestMapping("/order/seller/order/complain")
public class OrderComplaintController {

    /** 交易投诉 */
    private final IOrderComplaintService orderComplaintService;

    /** 投诉沟通 */
    private final IOrderComplaintCommunicationService orderComplaintCommunicationService;

    @Operation(summary = "通过id获取", description = "通过id获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{id}")
    public Result<OrderComplaintVO> getOrderComplainById(@PathVariable Long id) {
        return Result.success(OperationalJudgment.judgment(orderComplaintService.getOrderComplainById(id)));
    }

    @Operation(summary = "分页获取", description = "分页获取")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<OrderComplaintBaseVO>> get(OrderComplaintPageQuery orderComplaintPageQuery) {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        orderComplaintPageQuery.setStoreId(storeId);
        IPage<OrderComplaint> page = orderComplaintService.pageQuery(orderComplaintPageQuery);
        return Result.success(MpUtils.convertMybatisPage(page, OrderComplaintBaseVO.class));
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
                .owner(CommunicationOwnerEnum.STORE.name())
                .ownerName(user.getUsername())
                .ownerId(user.getStoreId())
                .build();
        return Result.success(orderComplaintCommunicationService.addCommunication(orderComplaintCommunication));
    }

    @Operation(summary = "修改申诉信息", description = "修改申诉信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @Validated @RequestBody OrderComplaintDTO orderComplaintDTO) {
        Long storeId = SecurityUtils.getCurrentUser().getStoreId();
        OrderComplaint orderComplaint = OrderComplainConvert.INSTANCE.convert(orderComplaintDTO);
        orderComplaint.setStoreId(storeId);
        return Result.success(orderComplaintService.updateOrderComplain(orderComplaint));
    }

    @Operation(summary = "申诉", description = "申诉")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping("/appeal")
    public Result<OrderComplaintVO> appeal(@Validated @RequestBody StoreAppealDTO storeAppealDTO) {
        orderComplaintService.appeal(storeAppealDTO);
        return Result.success(orderComplaintService.getOrderComplainById(storeAppealDTO.orderComplaintId()));
    }

    @Operation(summary = "修改状态", description = "修改状态")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/status")
    public Result<Boolean> updateStatus(@Validated @RequestBody OrderComplaintOperationDTO orderComplaintOperationDTO) {
        return Result.success(orderComplaintService.updateOrderComplainByStatus(orderComplaintOperationDTO));
    }
}
