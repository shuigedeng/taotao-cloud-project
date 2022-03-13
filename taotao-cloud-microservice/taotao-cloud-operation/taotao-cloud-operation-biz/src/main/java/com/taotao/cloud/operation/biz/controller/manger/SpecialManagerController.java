package com.taotao.cloud.operation.biz.controller.manger;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.Result;
import cn.lili.modules.page.entity.dos.Special;
import cn.lili.modules.page.service.SpecialService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理端,专题活动接口
 *
 * @author Bulbasaur
 * @since 2020/12/7 11:33
 */
@RestController
@Api(tags = "管理端,专题活动接口")
@RequestMapping("/manager/order/special")
public class SpecialManagerController {

    @Autowired
    private SpecialService specialService;

    @ApiOperation(value = "添加专题活动")
    @PostMapping("/addSpecial")
    public Result<Special> addSpecial(@Valid Special special) {
        return Result.success(specialService.addSpecial(special));
    }

    @ApiOperation(value = "修改专题活动")
    @ApiImplicitParam(name = "id", value = "专题ID", required = true, dataType = "String", paramType = "path")
    @PutMapping("/updateSpecial")
    public Result<Special> updateSpecial(@PathVariable String id, @Valid Special special) {
        special.setId(id);
        specialService.updateById(special);
        return Result.success(special);
    }

    @ApiOperation(value = "删除专题活动")
    @ApiImplicitParam(name = "id", value = "专题ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/{id}")
    public Result<Object> deleteSpecial(@PathVariable String id) {
        specialService.removeSpecial(id);
        return ResultUtil.success();
    }

    @ApiOperation(value = "分页获取专题活动")
    @GetMapping
    public Result<IPage<Special>> getSpecials(PageVO pageVo) {
        return Result.success(specialService.page(PageUtil.initPage(pageVo)));
    }

    @ApiOperation(value = "获取专题活动列表")
    @GetMapping("/getSpecialsList")
    public Result<List<Special>> getSpecialsList() {
        return Result.success(specialService.list());
    }


    @ApiOperation(value = "获取专题活动")
    @ApiImplicitParam(name = "id", value = "专题ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{id}")
    public Result<Special> getSpecialsList(@PathVariable String id) {
        return Result.success(specialService.getById(id));
    }

}
