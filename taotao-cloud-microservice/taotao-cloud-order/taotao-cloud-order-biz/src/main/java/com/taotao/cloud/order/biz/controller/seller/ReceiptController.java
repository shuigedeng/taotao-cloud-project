package com.taotao.cloud.order.biz.controller.seller;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.order.entity.dos.Receipt;
import cn.lili.modules.order.order.entity.dto.OrderReceiptDTO;
import cn.lili.modules.order.order.entity.dto.ReceiptSearchParams;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.modules.order.order.service.ReceiptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 店铺端,发票接口
 *
 * 
 * @since 2020/11/28 14:09
 **/
@Validated
@RestController
@RequestMapping("/sys/manager/dept")
@Tag(name = "店铺端-部门管理API", description = "店铺端-部门管理API")

@RestController
@Api(tags = "店铺端,发票接口")
@RequestMapping("/order/seller/receipt")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private OrderService orderService;
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "分页获取")
    @GetMapping
    public ResultMessage<IPage<OrderReceiptDTO>> getByPage(PageVO page, ReceiptSearchParams receiptSearchParams) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        receiptSearchParams.setStoreId(storeId);
        return ResultUtil.data(receiptService.getReceiptData(receiptSearchParams, page));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "发票ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/get/{id}")
    public ResultMessage<Receipt> get(@PathVariable String id) {
        return ResultUtil.data(OperationalJudgment.judgment(receiptService.getById(id)));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "开发票")
    @ApiImplicitParam(name = "id", value = "发票ID", required = true, dataType = "String", paramType = "path")
    @PostMapping(value = "/{id}/invoicing")
    public ResultMessage<Receipt> invoicing(@PathVariable String id) {
        OperationalJudgment.judgment(receiptService.getById(id));
        return ResultUtil.data(receiptService.invoicing(id));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "通过订单编号获取")
    @ApiImplicitParam(name = "orderSn", value = "订单编号", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/get/orderSn/{orderSn}")
    public ResultMessage<Receipt> getByOrderSn(@PathVariable String orderSn) {
        OperationalJudgment.judgment(orderService.getBySn(orderSn));
        return ResultUtil.data(receiptService.getByOrderSn(orderSn));
    }

}
