package com.taotao.cloud.promotion.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.vo.SeckillApplyVO;
import com.taotao.cloud.promotion.api.vo.SeckillSearchParams;
import com.taotao.cloud.promotion.biz.entity.Seckill;
import com.taotao.cloud.promotion.biz.entity.SeckillApply;
import com.taotao.cloud.promotion.biz.service.SeckillApplyService;
import com.taotao.cloud.promotion.biz.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 店铺端,秒杀活动接口
 *
 * 
 * @since 2020/8/26
 **/
@RestController
@Api(tags = "店铺端,秒杀活动接口")
@RequestMapping("/store/promotion/seckill")
public class SeckillStoreController {
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private SeckillApplyService seckillApplyService;

    @GetMapping
    @ApiOperation(value = "获取秒杀活动列表")
    public Result<IPage<Seckill>> getSeckillPage(SeckillSearchParams queryParam, PageVO pageVo) {
        IPage<Seckill> seckillPage = seckillService.pageFindAll(queryParam, pageVo);
        return Result.success(seckillPage);
    }

    @GetMapping("/apply")
    @ApiOperation(value = "获取秒杀活动申请列表")
    public Result<IPage<SeckillApply>> getSeckillApplyPage(SeckillSearchParams queryParam, PageVO pageVo) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        queryParam.setStoreId(storeId);
        IPage<SeckillApply> seckillPage = seckillApplyService.getSeckillApply(queryParam, pageVo);
        return Result.success(seckillPage);
    }

    @GetMapping("/{seckillId}")
    @ApiOperation(value = "获取秒杀活动信息")
    public Result<Seckill> getSeckill(@PathVariable String seckillId) {
        return Result.success(seckillService.getById(seckillId));
    }

    @GetMapping("/apply/{seckillApplyId}")
    @ApiOperation(value = "获取秒杀活动申请")
    public Result<SeckillApply> getSeckillApply(@PathVariable String seckillApplyId) {
        SeckillApply seckillApply = OperationalJudgment.judgment(seckillApplyService.getById(seckillApplyId));
        return Result.success(seckillApply);
    }

    @PostMapping(path = "/apply/{seckillId}", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "添加秒杀活动申请")
    public Result<String> addSeckillApply(@PathVariable String seckillId, @RequestBody List<SeckillApplyVO> applyVos) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        seckillApplyService.addSeckillApply(seckillId, storeId, applyVos);
        return Result.success();
    }

    @DeleteMapping("/apply/{seckillId}/{id}")
    @ApiOperation(value = "删除秒杀活动商品")
    public Result<String> deleteSeckillApply(@PathVariable String seckillId, @PathVariable String id) {
        OperationalJudgment.judgment(seckillApplyService.getById(id));
        seckillApplyService.removeSeckillApply(seckillId, id);
        return Result.success();
    }


}
