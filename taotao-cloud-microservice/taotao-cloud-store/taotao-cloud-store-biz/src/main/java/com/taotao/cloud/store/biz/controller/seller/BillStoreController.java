package com.taotao.cloud.store.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.store.biz.service.BillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 店铺端,结算单接口
 *
 * 
 * @since 2020/11/17 4:29 下午
 */
@RestController
@Api(tags = "店铺端,结算单接口")
@RequestMapping("/store/bill")
public class BillStoreController {

    @Autowired
    private BillService billService;

    @Autowired
    private StoreFlowService storeFlowService;

    @ApiOperation(value = "获取结算单分页")
    @GetMapping(value = "/getByPage")
    public Result<IPage<BillListVO>> getByPage(BillSearchParams billSearchParams) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        billSearchParams.setStoreId(storeId);
        return Result.success(billService.billPage(billSearchParams));
    }

    @ApiOperation(value = "通过id获取结算单")
    @ApiImplicitParam(name = "id", value = "结算单ID", required = true, paramType = "path", dataType = "String")
    @GetMapping(value = "/get/{id}")
    public Result<Bill> get(@PathVariable String id) {
        return Result.success(OperationalJudgment.judgment(billService.getById(id)));
    }

    @ApiOperation(value = "获取商家结算单流水分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "结算单ID", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "flowType", value = "流水类型:PAY、REFUND", paramType = "query", dataType = "String")
    })
    @GetMapping(value = "/{id}/getStoreFlow")
    public Result<IPage<StoreFlow>> getStoreFlow(@PathVariable String id, String flowType, PageVO pageVO) {
        OperationalJudgment.judgment(billService.getById(id));
        return Result.success(storeFlowService.getStoreFlow(id, flowType, pageVO));
    }

    @ApiOperation(value = "获取商家分销订单流水分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "结算单ID", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping(value = "/{id}/getDistributionFlow")
    public Result<IPage<StoreFlow>> getDistributionFlow(@PathVariable String id, PageVO pageVO) {
        OperationalJudgment.judgment(billService.getById(id));
        return Result.success(storeFlowService.getDistributionFlow(id, pageVO));
    }

    @ApiOperation(value = "核对结算单")
    @ApiImplicitParam(name = "id", value = "结算单ID", required = true, paramType = "path", dataType = "String")
    @PutMapping(value = "/check/{id}")
    public Result<Object> examine(@PathVariable String id) {
        OperationalJudgment.judgment(billService.getById(id));
        billService.check(id);
        return Result.success();
    }

    @ApiOperation(value = "下载结算单", produces = "application/octet-stream")
    @ApiImplicitParam(name = "id", value = "结算单ID", required = true, paramType = "path", dataType = "String")
    @GetMapping(value = "/downLoad/{id}")
    public void downLoadDeliverExcel(@PathVariable String id) {
        OperationalJudgment.judgment(billService.getById(id));
        HttpServletResponse response = ThreadContextHolder.getHttpResponse();
        billService.download(response, id);

    }

}
