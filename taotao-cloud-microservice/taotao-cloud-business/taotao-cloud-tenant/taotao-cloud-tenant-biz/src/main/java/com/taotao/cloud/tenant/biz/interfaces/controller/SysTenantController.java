package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.tenant.biz.application.dto.SysTenantDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysTenantQuery;
import com.mdframe.forge.plugin.system.entity.SysTenant;
import com.taotao.cloud.tenant.biz.application.service.service.ISysTenantService;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 租户管理Controller
 */
@RestController
@RequestMapping("/system/tenant")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysTenantController {

    private final ISysTenantService tenantService;

    /**
     * 分页查询租户列表
     */
    @GetMapping("/page")
    public Result<IPage<SysTenant>> page(SysTenantQuery query) {
        IPage<SysTenant> page = tenantService.selectTenantPage(query);
        return Result.success(page);
    }

    /**
     * 根据ID查询租户详情
     */
    @PostMapping("/getById")
    public Result<SysTenant> getById(@RequestParam Long id) {
        SysTenant tenant = tenantService.selectTenantById(id);
        return Result.success(tenant);
    }

    /**
     * 查询用户当前租户的配置
     */
    @PostMapping("/userTenantConfig")
    public Result<SysTenant> selectUserTenantConfig(@RequestParam(required = false) Long id) {
        SysTenant tenant = tenantService.selectUserTenantConfig(id);
        return Result.success(tenant);
    }

    /**
     * 新增租户
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SysTenantDTO dto) {
        boolean result = tenantService.insertTenant(dto);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改租户
     */
    @PostMapping("/edit")
    public Result<Void> edit(@RequestBody SysTenantDTO dto) {
        boolean result = tenantService.updateTenant(dto);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除租户
     */
    @PostMapping("/remove")
    public Result<Void> remove(@RequestParam Long id) {
        boolean result = tenantService.deleteTenantById(id);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除租户
     */
    @PostMapping("/removeBatch")
    public Result<Void> removeBatch(@RequestBody Long[] ids) {
        boolean result = tenantService.deleteTenantByIds(ids);
        return result ? Result.success() : Result.error("批量删除失败");
    }
}
