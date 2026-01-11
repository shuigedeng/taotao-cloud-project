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
import com.taotao.boot.web.utils.OperationalJudgment;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.common.utils.servlet.RequestUtils;
import com.taotao.cloud.member.api.model.dto.MemberAddressDTO;
import com.taotao.cloud.order.sys.model.page.order.OrderPageQuery;
import com.taotao.cloud.order.sys.model.vo.cart.OrderExportVO;
import com.taotao.cloud.order.sys.model.vo.order.OrderDetailVO;
import com.taotao.cloud.order.sys.model.vo.order.OrderSimpleVO;
import com.taotao.cloud.order.biz.model.entity.order.Order;
import com.taotao.cloud.order.biz.service.business.order.OrderPriceService;
import com.taotao.cloud.order.biz.service.business.order.OrderService;
import com.taotao.cloud.store.api.feign.IFeignStoreLogisticsApi;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 店铺端,订单API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-订单API", description = "店铺端-订单API")
@RequestMapping("/order/seller/order")
public class OrderController {

    /** 订单 */
    private final OrderService orderService;
    /** 订单价格 */
    private final OrderPriceService orderPriceService;
    /** 物流公司 */
    private final IFeignStoreLogisticsApi storeLogisticsService;

    @Operation(summary = "查询订单列表", description = "查询订单列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/page")
    public Result<PageResult<OrderSimpleVO>> queryMineOrder(OrderPageQuery orderPageQuery) {
        IPage<OrderSimpleVO> page = orderService.pageQuery(orderPageQuery);
        return Result.success(MpUtils.convertMybatisPage(page, OrderSimpleVO.class));
    }

    @Operation(summary = "订单明细", description = "订单明细")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/{orderSn}")
    public Result<OrderDetailVO> getBySn(@NotNull @PathVariable String orderSn) {
        OperationalJudgment.judgment(orderService.getBySn(orderSn));
        return Result.success(orderService.queryDetail(orderSn));
    }

    @Operation(summary = "修改收货人信息", description = "修改收货人信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/update/{orderSn}/consignee")
    public Result<Order> consignee(
            @NotNull(message = "参数非法") @PathVariable String orderSn, @Valid MemberAddressDTO memberAddressDTO) {
        return Result.success(orderService.updateConsignee(orderSn, memberAddressDTO));
    }

    @Operation(summary = "修改订单价格", description = "修改订单价格")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/{orderSn}/price")
    public Result<Boolean> updateOrderPrice(
            @PathVariable String orderSn, @NotNull(message = "订单价格不能为空") @RequestParam BigDecimal orderPrice) {
        return Result.success(orderPriceService.updatePrice(orderSn, orderPrice));
    }

    @Operation(summary = "订单发货", description = "订单发货")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/{orderSn}/delivery")
    public Result<Order> delivery(
            @NotNull(message = "参数非法") @PathVariable String orderSn,
            @NotNull(message = "发货单号不能为空") String logisticsNo,
            @NotNull(message = "请选择物流公司") Long logisticsId) {
        return Result.success(orderService.delivery(orderSn, logisticsNo, logisticsId));
    }

    @Operation(summary = "取消订单", description = "取消订单")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PostMapping(value = "/{orderSn}/cancel")
    public Result<Order> cancel(@PathVariable String orderSn, @RequestParam String reason) {
        return Result.success(orderService.cancel(orderSn, reason));
    }

    @Operation(summary = "根据核验码获取订单信息", description = "根据核验码获取订单信息")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/verificationCode/{verificationCode}")
    public Result<Order> getOrderByVerificationCode(@PathVariable String verificationCode) {
        return Result.success(orderService.getOrderByVerificationCode(verificationCode));
    }

    @Operation(summary = "订单核验", description = "订单核验")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @PutMapping(value = "/take/{orderSn}/{verificationCode}")
    public Result<Order> take(@PathVariable String orderSn, @PathVariable String verificationCode) {
        return Result.success(orderService.take(orderSn, verificationCode));
    }

    @Operation(summary = "查询物流踪迹", description = "查询物流踪迹")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/traces/{orderSn}")
    public Result<Traces> getTraces(@NotBlank(message = "订单编号不能为空") @PathVariable String orderSn) {
        OperationalJudgment.judgment(orderService.getBySn(orderSn));
        return Result.success(orderService.getTraces(orderSn));
    }

    @Operation(summary = "下载待发货的订单列表", description = "下载待发货的订单列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping(value = "/downLoadDeliverExcel")
    public void downLoadDeliverExcel() {
        // 获取店铺已经选择物流公司列表
        List<String> logisticsName = storeLogisticsService.getStoreSelectedLogisticsName(
                SecurityUtils.getCurrentUser().getStoreId());
        // 下载订单批量发货Excel
        this.orderService.downLoadDeliver(RequestUtils.getResponse(), logisticsName);
    }

    @Operation(summary = "上传文件进行订单批量发货", description = "上传文件进行订单批量发货")
    @RequestLogger
    @PostMapping(value = "/batchDeliver", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Boolean> batchDeliver(@RequestPart("files") MultipartFile files) {
        return Result.success(orderService.batchDeliver(files));
    }

    @Operation(summary = "查询订单导出列表", description = "查询订单导出列表")
    @RequestLogger
    @PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping("/queryExportOrder")
    public Result<List<OrderExportVO>> queryExportOrder(OrderPageQuery orderPageQuery) {
        return Result.success(orderService.queryExportOrder(orderPageQuery));
    }
}
