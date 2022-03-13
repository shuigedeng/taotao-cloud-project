package com.taotao.cloud.order.biz.controller.buyer;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.order.entity.dos.Receipt;
import cn.lili.modules.order.order.entity.dto.OrderReceiptDTO;
import cn.lili.modules.order.order.entity.dto.ReceiptSearchParams;
import cn.lili.modules.order.order.service.ReceiptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 买家端,发票接口
 **/
@Validated
@RestController
@RequestMapping("/sys/manager/dept")
@Tag(name = "买家端-部门管理API", description = "买家端-部门管理API")

@RestController
@Api(tags = "买家端,发票接口")
@RequestMapping("/order/buyer/trade/receipt")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "获取发票详情")
    @GetMapping("/{id}")
    public ResultMessage<Receipt> getDetail(@PathVariable String id) {
        return ResultUtil.data(this.receiptService.getDetail(id));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "获取发票分页信息")
    @GetMapping
    public ResultMessage<IPage<OrderReceiptDTO>> getPage(ReceiptSearchParams searchParams, PageVO pageVO) {
        return ResultUtil.data(this.receiptService.getReceiptData(searchParams, pageVO));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "保存发票信息")
    @PostMapping
    public ResultMessage<Receipt> save(@Valid Receipt receipt) {
        return ResultUtil.data(receiptService.saveReceipt(receipt));
    }

}
