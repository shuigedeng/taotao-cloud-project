package com.taotao.cloud.stock.biz.interfaces.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 租户Controller
 *
 * @author shuigedeng
 * @date 2021-02-24
 */
@Api(tags = "租户管理")
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private TenantQueryService tenantQueryService;

    @Autowired
    private TenantApplicationService tenantApplicationService;

    /**
     * 用户分页查询
     */
    @ApiOperation("租户分页查询")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:tenant:list')")
    public Result list(@RequestParam Map<String, Object> params) {
        Page page = tenantQueryService.queryPage(params);
        return Result.ok().put(PageConstant.PAGE, page);
    }

    /**
     * 禁用租户
     */
    @ApiOperation("禁用租户")
    @SysLog("禁用租户")
    @PostMapping("/disable/{id}")
    @PreAuthorize("hasAuthority('sys:tenant:disable')")
    public Result disable(@PathVariable("id") String id) {
        tenantApplicationService.disable(id);
        return Result.ok();
    }
}
