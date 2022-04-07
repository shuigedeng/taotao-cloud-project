package com.taotao.cloud.promotion.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.vo.PointsGoodsCategoryVO;
import com.taotao.cloud.promotion.biz.entity.PointsGoodsCategory;
import com.taotao.cloud.promotion.biz.service.PointsGoodsCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端,积分商品分类接口
 *
 * 
 * @since 2021/1/14
 **/
@RestController
@Api(tags = "管理端,积分商品分类接口")
@RequestMapping("/manager/promotion/pointsGoodsCategory")
public class PointsGoodsCategoryManagerController {
    @Autowired
    private PointsGoodsCategoryService pointsGoodsCategoryService;

    @PostMapping
    @ApiOperation(value = "添加积分商品分类")
    public Result<Object> add(PointsGoodsCategoryVO pointsGoodsCategory) {
        pointsGoodsCategoryService.addCategory(pointsGoodsCategory);
        return Result.success();
    }

    @PutMapping
    @ApiOperation(value = "修改积分商品分类")
    public Result<Object> update(PointsGoodsCategoryVO pointsGoodsCategory) {
        pointsGoodsCategoryService.updateCategory(pointsGoodsCategory);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除积分商品分类")
    public Result<Object> delete(@PathVariable String id) {
        pointsGoodsCategoryService.deleteCategory(id);
        return Result.success();
    }

    @GetMapping
    @ApiOperation(value = "获取积分商品分类分页")
    public Result<IPage<PointsGoodsCategory>> page(String name, PageVO page) {
        return Result.success(pointsGoodsCategoryService.getCategoryByPage(name, page));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "修改积分商品分类")
    public Result<Object> getById(@PathVariable String id) {
        return Result.success(pointsGoodsCategoryService.getCategoryDetail(id));
    }

}
