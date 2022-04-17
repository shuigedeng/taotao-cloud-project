package com.taotao.cloud.promotion.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.vo.SeckillApplyVO;
import com.taotao.cloud.promotion.api.vo.SeckillSearchParams;
import com.taotao.cloud.promotion.biz.entity.Seckill;
import com.taotao.cloud.promotion.biz.entity.SeckillApply;
import com.taotao.cloud.promotion.biz.service.SeckillApplyService;
import com.taotao.cloud.promotion.biz.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "店铺端,秒杀活动接口")
@RequestMapping("/store/promotion/seckill")
public class SeckillStoreController {
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private SeckillApplyService seckillApplyService;
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping
    @Operation(summary = "获取秒杀活动列表")
    public Result<IPage<Seckill>> getSeckillPage(SeckillSearchParams queryParam, PageVO pageVo) {
        IPage<Seckill> seckillPage = seckillService.pageFindAll(queryParam, pageVo);
        return Result.success(seckillPage);
    }
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping("/apply")
    @Operation(summary = "获取秒杀活动申请列表")
    public Result<IPage<SeckillApply>> getSeckillApplyPage(SeckillSearchParams queryParam, PageVO pageVo) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        queryParam.setStoreId(storeId);
        IPage<SeckillApply> seckillPage = seckillApplyService.getSeckillApply(queryParam, pageVo);
        return Result.success(seckillPage);
    }
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping("/{seckillId}")
    @Operation(summary = "获取秒杀活动信息")
    public Result<Seckill> getSeckill(@PathVariable String seckillId) {
        return Result.success(seckillService.getById(seckillId));
    }
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @GetMapping("/apply/{seckillApplyId}")
    @Operation(summary = "获取秒杀活动申请")
    public Result<SeckillApply> getSeckillApply(@PathVariable String seckillApplyId) {
        SeckillApply seckillApply = OperationalJudgment.judgment(seckillApplyService.getById(seckillApplyId));
        return Result.success(seckillApply);
    }
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @PostMapping(path = "/apply/{seckillId}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "添加秒杀活动申请")
    public Result<String> addSeckillApply(@PathVariable String seckillId, @RequestBody List<SeckillApplyVO> applyVos) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        seckillApplyService.addSeckillApply(seckillId, storeId, applyVos);
        return Result.success();
    }
	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
    @DeleteMapping("/apply/{seckillId}/{id}")
    @Operation(summary = "删除秒杀活动商品")
    public Result<String> deleteSeckillApply(@PathVariable String seckillId, @PathVariable String id) {
        OperationalJudgment.judgment(seckillApplyService.getById(id));
        seckillApplyService.removeSeckillApply(seckillId, id);
        return Result.success();
    }


}
