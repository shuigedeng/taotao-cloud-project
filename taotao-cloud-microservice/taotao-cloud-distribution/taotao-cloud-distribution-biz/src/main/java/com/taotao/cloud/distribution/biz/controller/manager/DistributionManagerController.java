package com.taotao.cloud.distribution.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.query.DistributionPageQuery;
import com.taotao.cloud.distribution.biz.entity.Distribution;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 管理端,分销员管理接口
 */
@RestController
@Api(tags = "管理端,分销员管理接口")
@RequestMapping("/manager/distribution/distribution")
public class DistributionManagerController {

    @Autowired
    private DistributionService distributionService;

    @ApiOperation(value = "分页获取")
    @GetMapping(value = "/getByPage")
    public Result<IPage<Distribution>> getByPage(DistributionPageQuery distributionPageQuery, PageVO page) {
        return Result.success(distributionService.distributionPage(distributionPageQuery, page));
    }


    @PreventDuplicateSubmissions
    @ApiOperation(value = "清退分销商")
    @PutMapping(value = "/retreat/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分销商id", required = true, paramType = "path", dataType = "String")
    })
    public Result<Object> retreat(@PathVariable String id) {
        if (distributionService.retreat(id)) {
            return Result.success();
        } else {
            throw new BusinessException(ResultEnum.DISTRIBUTION_RETREAT_ERROR);
        }

    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "恢复分销商")
    @PutMapping(value = "/resume/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分销商id", required = true, paramType = "path", dataType = "String")
    })
    public Result<Object> resume(@PathVariable String id) {
        if (distributionService.resume(id)) {
            return Result.success();
        } else {
            throw new BusinessException(ResultEnum.DISTRIBUTION_RETREAT_ERROR);
        }

    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "审核分销商")
    @PutMapping(value = "/audit/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "分销商id", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "审核结果，PASS 通过  REFUSE 拒绝", required = true, paramType = "query", dataType = "String")
    })
    public Result<Object> audit(@NotNull @PathVariable String id, @NotNull String status) {
        if (distributionService.audit(id, status)) {
            return Result.success();
        } else {
            throw new BusinessException(ResultEnum.DISTRIBUTION_AUDIT_ERROR);
        }

    }
}
