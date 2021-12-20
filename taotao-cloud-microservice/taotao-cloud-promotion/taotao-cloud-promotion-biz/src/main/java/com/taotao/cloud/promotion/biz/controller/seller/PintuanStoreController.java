package com.taotao.cloud.promotion.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.vo.PintuanSearchParams;
import com.taotao.cloud.promotion.api.vo.PintuanVO;
import com.taotao.cloud.promotion.biz.entity.Pintuan;
import com.taotao.cloud.promotion.biz.service.PintuanService;
import com.taotao.cloud.promotion.biz.service.PromotionGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Objects;

/**
 * 店铺端,拼团管理接口
 *
 * @author paulG
 * @since 2020/10/9
 **/
@RestController
@Api(tags = "店铺端,拼团管理接口")
@RequestMapping("/store/promotion/pintuan")
public class PintuanStoreController {

    @Autowired
    private PintuanService pintuanService;
    @Autowired
    private PromotionGoodsService promotionGoodsService;


    @GetMapping
    @ApiOperation(value = "根据条件分页查询拼团活动列表")
    public ResultMessage<IPage<Pintuan>> getPintuanByPage(PintuanSearchParams queryParam, PageVO pageVo) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        queryParam.setStoreId(currentUser.getStoreId());
        return ResultUtil.data(pintuanService.pageFindAll(queryParam, pageVo));
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "通过id获取")
    public ResultMessage<PintuanVO> get(@PathVariable String id) {
        PintuanVO pintuan = OperationalJudgment.judgment(pintuanService.getPintuanVO(id));
        return ResultUtil.data(pintuan);
    }

    @GetMapping("/goods/{pintuanId}")
    @ApiOperation(value = "根据条件分页查询拼团活动商品列表")
    public ResultMessage<IPage<PromotionGoods>> getPintuanGoodsByPage(@PathVariable String pintuanId, PageVO pageVo) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        PromotionGoodsSearchParams searchParams = new PromotionGoodsSearchParams();
        searchParams.setStoreId(currentUser.getStoreId());
        searchParams.setPromotionId(pintuanId);
        searchParams.setPromotionType(PromotionTypeEnum.PINTUAN.name());
        return ResultUtil.data(promotionGoodsService.pageFindAll(searchParams, pageVo));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "添加拼团活动")
    public ResultMessage<String> addPintuan(@RequestBody @Validated PintuanVO pintuan) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        pintuan.setStoreId(currentUser.getStoreId());
        pintuan.setStoreName(currentUser.getStoreName());
        if (pintuanService.savePromotions(pintuan)) {
            return ResultUtil.success(ResultCode.PINTUAN_ADD_SUCCESS);
        }
        throw new ServiceException(ResultCode.PINTUAN_ADD_ERROR);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "修改拼团活动")
    public ResultMessage<String> editPintuan(@RequestBody @Validated PintuanVO pintuan) {
        OperationalJudgment.judgment(pintuanService.getById(pintuan.getId()));
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        pintuan.setStoreId(currentUser.getStoreId());
        pintuan.setStoreName(currentUser.getStoreName());
        if (pintuanService.updatePromotions(pintuan)) {
            return ResultUtil.success(ResultCode.PINTUAN_EDIT_SUCCESS);
        }
        throw new ServiceException(ResultCode.PINTUAN_EDIT_ERROR);
    }

    @PutMapping("/status/{pintuanId}")
    @ApiOperation(value = "操作拼团活动状态")
    public ResultMessage<String> openPintuan(@PathVariable String pintuanId, Long startTime, Long endTime) {
        OperationalJudgment.judgment(pintuanService.getById(pintuanId));
        if (pintuanService.updateStatus(Collections.singletonList(pintuanId), startTime, endTime)) {
            return ResultUtil.success(ResultCode.PINTUAN_MANUAL_OPEN_SUCCESS);
        }
        throw new ServiceException(ResultCode.PINTUAN_MANUAL_OPEN_ERROR);

    }

    @DeleteMapping("/{pintuanId}")
    @ApiOperation(value = "手动删除拼团活动")
    public ResultMessage<String> deletePintuan(@PathVariable String pintuanId) {
        OperationalJudgment.judgment(pintuanService.getById(pintuanId));
        if (pintuanService.removePromotions(Collections.singletonList(pintuanId))) {
            return ResultUtil.success(ResultCode.PINTUAN_DELETE_SUCCESS);
        }
        throw new ServiceException(ResultCode.PINTUAN_DELETE_ERROR);
    }

}
