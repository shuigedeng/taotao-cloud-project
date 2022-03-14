package com.taotao.cloud.promotion.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.vo.PintuanSearchParams;
import com.taotao.cloud.promotion.api.vo.PintuanVO;
import com.taotao.cloud.promotion.api.vo.PromotionGoodsSearchParams;
import com.taotao.cloud.promotion.biz.entity.Pintuan;
import com.taotao.cloud.promotion.biz.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.PintuanService;
import com.taotao.cloud.promotion.biz.service.PromotionGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 管理端,平台拼团接口
 *
 * 
 * @since 2020/10/9
 **/
@RestController
@Api(tags = "管理端,平台拼团接口")
@RequestMapping("/manager/promotion/pintuan")
public class PintuanManagerController {
    @Autowired
    private PintuanService pintuanService;
    @Autowired
    private PromotionGoodsService promotionGoodsService;

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "通过id获取")
    public Result<PintuanVO> get(@PathVariable String id) {
        PintuanVO pintuan = pintuanService.getPintuanVO(id);
        return Result.success(pintuan);
    }

    @GetMapping
    @ApiOperation(value = "根据条件分页查询拼团活动列表")
    public Result<IPage<Pintuan>> getPintuanByPage(PintuanSearchParams queryParam, PageVO pageVo) {
        IPage<Pintuan> pintuanIPage = pintuanService.pageFindAll(queryParam, pageVo);
        return Result.success(pintuanIPage);
    }

    @GetMapping("/goods/{pintuanId}")
    @ApiOperation(value = "根据条件分页查询拼团活动商品列表")
    public Result<IPage<PromotionGoods>> getPintuanGoodsByPage(@PathVariable String pintuanId, PageVO pageVo) {
        PromotionGoodsSearchParams searchParams = new PromotionGoodsSearchParams();
        searchParams.setPromotionId(pintuanId);
        searchParams.setPromotionType(PromotionTypeEnum.PINTUAN.name());
        return Result.success(promotionGoodsService.pageFindAll(searchParams, pageVo));
    }

    @PutMapping("/status/{pintuanIds}")
    @ApiOperation(value = "操作拼团活动状态")
    public Result<String> openPintuan(@PathVariable String pintuanIds, Long startTime, Long endTime) {
        if (pintuanService.updateStatus(Arrays.asList(pintuanIds.split(",")), startTime, endTime)) {
            return ResultUtil.success(ResultEnum.PINTUAN_MANUAL_OPEN_SUCCESS);
        }
        throw new BusinessException(ResultEnum.PINTUAN_MANUAL_OPEN_ERROR);

    }

}
