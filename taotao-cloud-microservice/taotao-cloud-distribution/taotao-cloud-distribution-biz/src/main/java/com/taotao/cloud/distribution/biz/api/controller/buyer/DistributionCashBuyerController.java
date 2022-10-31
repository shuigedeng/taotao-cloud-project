package com.taotao.cloud.distribution.biz.api.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.biz.model.entity.DistributionCash;
import com.taotao.cloud.distribution.biz.service.DistributionCashService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 买家端,分销商品佣金提现接口
 *
 */
@Validated
@RestController
@Tag(name = "买家端-分销商品佣金提现接口", description = "买家端-分销商品佣金提现接口")
@RequestMapping("/buyer/distribution/cash")
public class DistributionCashBuyerController {

    /**
     * 分销佣金
     */
    @Autowired
    private DistributionCashService distributionCashService;
    /**
     * 分销员提现
     */
    @Autowired
    private DistributionCashService distributorCashService;

	@Operation(summary = "分销员提现", description = "分销员提现")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
    //@PreventDuplicateSubmissions
    @PostMapping
    public Result<Object> cash(@Validated @Max(value = 9999, message = "提现金额单次最多允许提现9999元")
                                          @Min(value = 1, message = "提现金额单次最少提现金额为1元")
                                          @NotNull @ApiIgnore BigDecimal price) {
        if (Boolean.TRUE.equals(distributionCashService.cash(price))) {
            return Result.success();
        }
        throw new BusinessException(ResultEnum.ERROR);
    }

	@Operation(summary = "分销员提现历史", description = "分销员提现历史")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
    @GetMapping
    public Result<IPage<DistributionCash>> casHistory(PageVO page) {
        return Result.success(distributorCashService.getDistributionCash(page));
    }


}
