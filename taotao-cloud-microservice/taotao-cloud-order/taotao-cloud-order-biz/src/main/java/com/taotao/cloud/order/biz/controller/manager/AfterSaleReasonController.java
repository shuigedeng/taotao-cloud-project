package com.taotao.cloud.order.biz.controller.manager;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.aftersale.entity.dos.AfterSaleReason;
import cn.lili.modules.order.aftersale.service.AfterSaleReasonService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理端,售后原因接口
 *
 * 
 * @since 2021/1/6 14:11
 */
@Validated
@RestController
@RequestMapping("/sys/manager/dept")
@Tag(name = "平台管理端-部门管理API", description = "平台管理端-部门管理API")

@RestController
@RequestMapping("/order/manager/afterSaleReason")
@Api(tags = "管理端,售后原因接口")
public class AfterSaleReasonController {

    /**
     * 售后原因
     */
    @Autowired
    private AfterSaleReasonService afterSaleReasonService;
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "查看售后原因")
    @ApiImplicitParam(name = "id", value = "售后原因ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{id}")
    public ResultMessage<AfterSaleReason> get(@PathVariable String id) {

        return ResultUtil.data(afterSaleReasonService.getById(id));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "分页获取售后原因")
    @GetMapping(value = "/getByPage")
    @ApiImplicitParam(name = "serviceType", value = "售后类型", required = true, dataType = "String", paramType = "query")
    public ResultMessage<IPage<AfterSaleReason>> getByPage(PageVO page, @RequestParam String serviceType) {
        return ResultUtil.data(afterSaleReasonService.page(PageUtil.initPage(page),new QueryWrapper<AfterSaleReason>().eq("service_Type", serviceType)));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "添加售后原因")
    @PostMapping
    public ResultMessage<AfterSaleReason> save(@Valid AfterSaleReason afterSaleReason) {
        afterSaleReasonService.save(afterSaleReason);
        return ResultUtil.data(afterSaleReason);
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "修改售后原因")
    @ApiImplicitParam(name = "id", value = "关键词ID", required = true, dataType = "String", paramType = "path")
    @PutMapping("update/{id}")
    public ResultMessage<AfterSaleReason> update(@Valid AfterSaleReason afterSaleReason, @PathVariable("id") String id) {
        afterSaleReason.setId(id);
        return ResultUtil.data(afterSaleReasonService.editAfterSaleReason(afterSaleReason));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "删除售后原因")
    @ApiImplicitParam(name = "id", value = "售后原因ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/delByIds/{id}")
    public ResultMessage<Object> delAllByIds(@PathVariable String id) {
        afterSaleReasonService.removeById(id);
        return ResultUtil.success();
    }
}
