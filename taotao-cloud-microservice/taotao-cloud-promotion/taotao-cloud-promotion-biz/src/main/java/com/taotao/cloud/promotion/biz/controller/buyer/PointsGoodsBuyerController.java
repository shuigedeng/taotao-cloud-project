package com.taotao.cloud.promotion.biz.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.vo.PointsGoodsSearchParams;
import com.taotao.cloud.promotion.api.vo.PointsGoodsVO;
import com.taotao.cloud.promotion.biz.entity.PointsGoods;
import com.taotao.cloud.promotion.biz.entity.PointsGoodsCategory;
import com.taotao.cloud.promotion.biz.service.PointsGoodsCategoryService;
import com.taotao.cloud.promotion.biz.service.PointsGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,积分商品接口
 *
 * 
 * @since 2021/1/19
 **/
@RestController
@Api(tags = "买家端,积分商品接口")
@RequestMapping("/buyer/promotion/pointsGoods")
public class PointsGoodsBuyerController {
    @Autowired
    private PointsGoodsService pointsGoodsService;
    @Autowired
    private PointsGoodsCategoryService pointsGoodsCategoryService;

    @GetMapping
    @ApiOperation(value = "分页获取积分商品")
    public ResultMessage<IPage<PointsGoods>> getPointsGoodsPage(PointsGoodsSearchParams searchParams, PageVO page) {
        IPage<PointsGoods> pointsGoodsByPage = pointsGoodsService.pageFindAll(searchParams, page);
        return ResultUtil.data(pointsGoodsByPage);
    }

    @GetMapping("/category")
    @ApiOperation(value = "获取积分商品分类分页")
    public ResultMessage<IPage<PointsGoodsCategory>> page(String name, PageVO page) {
        return ResultUtil.data(pointsGoodsCategoryService.getCategoryByPage(name, page));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取积分活动商品")
    @ApiImplicitParam(name = "id", value = "积分商品ID", required = true, paramType = "path")
    public ResultMessage<PointsGoodsVO> getPointsGoodsPage(@PathVariable String id) {
        return ResultUtil.data(pointsGoodsService.getPointsGoodsDetail(id));
    }

}
