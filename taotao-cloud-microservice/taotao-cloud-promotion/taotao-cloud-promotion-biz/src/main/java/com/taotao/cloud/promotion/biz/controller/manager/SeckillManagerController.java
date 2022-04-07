package com.taotao.cloud.promotion.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.vo.SeckillSearchParams;
import com.taotao.cloud.promotion.api.vo.SeckillVO;
import com.taotao.cloud.promotion.biz.entity.Seckill;
import com.taotao.cloud.promotion.biz.service.SeckillApplyService;
import com.taotao.cloud.promotion.biz.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 管理端,秒杀活动接口
 *
 * 
 * @since 2020/8/20
 **/
@RestController
@Api(tags = "管理端,秒杀活动接口")
@RequestMapping("/manager/promotion/seckill")
public class SeckillManagerController {
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private SeckillApplyService seckillApplyService;


    @ApiOperation(value = "初始化秒杀活动(初始化方法，默认初始化30天内的活动）")
    @GetMapping("/init")
    public void addSeckill() {
        seckillService.init();
    }


    @ApiOperation(value = "修改秒杀活动")
    @PutMapping(consumes = "application/json", produces = "application/json")
    public Result<Seckill> updateSeckill(@RequestBody SeckillVO seckillVO) {
        seckillService.updatePromotions(seckillVO);
        return Result.success(seckillVO);
    }

    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "秒杀活动ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{id}")
    public Result<Seckill> get(@PathVariable String id) {
        Seckill seckill = seckillService.getById(id);
        return Result.success(seckill);
    }

    @ApiOperation(value = "分页查询秒杀活动列表")
    @GetMapping
    public Result<IPage<Seckill>> getAll(SeckillSearchParams param, PageVO pageVo) {
        return Result.success(seckillService.pageFindAll(param, pageVo));
    }

    @ApiOperation(value = "删除一个秒杀活动")
    @ApiImplicitParam(name = "id", value = "秒杀活动ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/{id}")
    public Result<Object> deleteSeckill(@PathVariable String id) {
        seckillService.removePromotions(Collections.singletonList(id));
        return Result.success();
    }

    @ApiOperation(value = "操作秒杀活动状态")
    @ApiImplicitParam(name = "id", value = "秒杀活动ID", required = true, dataType = "String", paramType = "path")
    @PutMapping("/status/{id}")
    public Result<Object> updateSeckillStatus(@PathVariable String id, Long startTime, Long endTime) {
        seckillService.updateStatus(Collections.singletonList(id), startTime, endTime);
        return Result.success();
    }

    @ApiOperation(value = "获取秒杀活动申请列表")
    @GetMapping("/apply")
    public Result<IPage<SeckillApply>> getSeckillApply(SeckillSearchParams param, PageVO pageVo) {
        IPage<SeckillApply> seckillApply = seckillApplyService.getSeckillApply(param, pageVo);
        return Result.success(seckillApply);
    }

    @DeleteMapping("/apply/{seckillId}/{id}")
    @ApiOperation(value = "删除秒杀活动申请")
    public Result<String> deleteSeckillApply(@PathVariable String seckillId, @PathVariable String id) {
        seckillApplyService.removeSeckillApply(seckillId, id);
        return Result.success();
    }


}
