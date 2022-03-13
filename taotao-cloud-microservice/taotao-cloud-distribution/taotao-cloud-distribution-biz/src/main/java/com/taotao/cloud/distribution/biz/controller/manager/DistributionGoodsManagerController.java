package com.taotao.cloud.distribution.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.distribution.api.dto.DistributionGoodsSearchParams;
import com.taotao.cloud.distribution.api.vo.DistributionGoodsVO;
import com.taotao.cloud.distribution.biz.service.DistributionGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端,分销商品管理接口
 *
 */
@RestController
@Api(tags = "管理端,分销商品管理接口")
@RequestMapping("/manager/distribution/goods")
public class DistributionGoodsManagerController {

    @Autowired
    private DistributionGoodsService distributionGoodsService;

    @GetMapping(value = "/getByPage")
    @ApiOperation(value = "分页获取")
    public Result<IPage<DistributionGoodsVO>> getByPage(
	    DistributionGoodsSearchParams distributionGoodsSearchParams) {
        return Result.success(distributionGoodsService.goodsPage(distributionGoodsSearchParams));
    }


    @DeleteMapping(value = "/delByIds/{ids}")
    @ApiOperation(value = "批量删除")
    public Result<Object> delAllByIds(@PathVariable List ids) {

        distributionGoodsService.removeByIds(ids);
        return ResultUtil.success();
    }
}
