package com.taotao.cloud.distribution.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.distribution.api.vo.DistributionCashSearchParams;
import com.taotao.cloud.distribution.biz.entity.DistributionCash;
import com.taotao.cloud.distribution.biz.service.DistributionCashService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 管理端,分销佣金管理接口
 */
@RestController
@Api(tags = "管理端,分销佣金管理接口")
@RequestMapping("/manager/distribution/cash")
public class DistributionCashManagerController {

    @Autowired
    private DistributionCashService distributorCashService;

    @ApiOperation(value = "通过id获取分销佣金详情")
    @GetMapping(value = "/get/{id}")
    public Result<DistributionCash> get(@PathVariable String id) {
        return Result.success(distributorCashService.getById(id));
    }

    @ApiOperation(value = "分页获取")
    @GetMapping(value = "/getByPage")
    public Result<IPage<DistributionCash>> getByPage(
	    DistributionCashSearchParams distributionCashSearchParams) {

        return Result.success(distributorCashService.getDistributionCash(distributionCashSearchParams));
    }


    @PreventDuplicateSubmissions
    @ApiOperation(value = "审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分销佣金ID", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "result", value = "处理结果", required = true, paramType = "query", dataType = "String")
    })
    @PostMapping(value = "/audit/{id}")
    public Result<DistributionCash> audit(@PathVariable String id, @NotNull String result) {
        return Result.success(distributorCashService.audit(id, result));
    }
}

