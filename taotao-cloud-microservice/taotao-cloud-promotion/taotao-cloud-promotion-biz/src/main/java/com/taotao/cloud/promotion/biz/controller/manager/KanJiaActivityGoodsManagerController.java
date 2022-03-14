package com.taotao.cloud.promotion.biz.controller.manager;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.dto.KanjiaActivityGoodsDTO;
import com.taotao.cloud.promotion.api.dto.KanjiaActivityGoodsOperationDTO;
import com.taotao.cloud.promotion.api.vo.kanjia.KanjiaActivityGoodsParams;
import com.taotao.cloud.promotion.biz.entity.KanjiaActivityGoods;
import com.taotao.cloud.promotion.biz.service.KanjiaActivityGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * 管理端,促销接口
 *
 * @author qiuqiu
 * @date 2021/7/2
 **/
@RestController
@Api(tags = "管理端,砍价促销接口")
@RequestMapping("/manager/promotion/kan-jia-goods")
public class KanJiaActivityGoodsManagerController {

    @Autowired
    private KanjiaActivityGoodsService kanJiaActivityGoodsService;

    @PostMapping
    @ApiOperation(value = "添加砍价活动")
    public Result<Object> add(@RequestBody KanjiaActivityGoodsOperationDTO kanJiaActivityGoodsOperationDTO) {
        kanJiaActivityGoodsService.add(kanJiaActivityGoodsOperationDTO);
        return ResultUtil.success();
    }


    @ApiOperation(value = "获取砍价活动分页")
    @GetMapping
    public Result<IPage<KanjiaActivityGoods>> getKanJiaActivityPage(
	    KanjiaActivityGoodsParams KanJiaActivityParams, PageVO page) {
        return Result.success(kanJiaActivityGoodsService.getForPage(KanJiaActivityParams, page));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "获取积分商品详情")
    public Result<Object> getPointsGoodsDetail(@PathVariable("id") String goodsId) {
        KanjiaActivityGoodsDTO kanJiaActivityGoodsDTO = kanJiaActivityGoodsService.getKanjiaGoodsDetail(goodsId);
        return Result.success(kanJiaActivityGoodsDTO);
    }


    @PutMapping
    @ApiOperation(value = "修改砍价商品")
    public Result<Object> updatePointsGoods(@RequestBody KanjiaActivityGoodsDTO kanJiaActivityGoodsDTO) {
        kanJiaActivityGoodsService.updateKanjiaActivityGoods(kanJiaActivityGoodsDTO);
        return ResultUtil.success();
    }


    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除砍价商品")
    public Result<Object> delete(@PathVariable String ids) {
        if (kanJiaActivityGoodsService.deleteKanJiaGoods(Arrays.asList(ids.split(",")))) {
            return ResultUtil.success();
        }
        throw new BusinessException(ResultEnum.KANJIA_GOODS_DELETE_ERROR);
    }


}
