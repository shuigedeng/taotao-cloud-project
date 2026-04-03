package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.tenant.biz.application.dto.SysOrgDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysOrgQuery;
import com.mdframe.forge.plugin.system.entity.SysOrg;
import com.taotao.cloud.tenant.biz.application.service.service.ISysOrgService;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织管理Controller
 */
@RestController
@RequestMapping("/system/org")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysOrgController {

    private final ISysOrgService orgService;

    /**
     * 分页查询组织列表
     */
    @GetMapping("/page")
    public Result<IPage<SysOrg>> page(SysOrgQuery query) {
        IPage<SysOrg> page = orgService.selectOrgPage(query);
        return Result.success(page);
    }

    /**
     * 查询组织树形列表
     */
    @GetMapping("/tree")
    public Result<List<SysOrg>> tree(SysOrgQuery query) {
        List<SysOrg> list = orgService.selectOrgTree(query);
        return Result.success(list);
    }

    /**
     * 根据ID查询组织详情
     */
    @PostMapping("/getById")
    public Result<SysOrg> getById(@RequestParam Long id) {
        SysOrg org = orgService.selectOrgById(id);
        return Result.success(org);
    }

    /**
     * 新增组织
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SysOrgDTO dto) {
        boolean result = orgService.insertOrg(dto);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改组织
     */
    @PostMapping("/edit")
    public Result<Void> edit(@RequestBody SysOrgDTO dto) {
        boolean result = orgService.updateOrg(dto);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除组织
     */
    @PostMapping("/remove")
    public Result<Void> remove(@RequestParam Long id) {
        boolean result = orgService.deleteOrgById(id);
        return result ? Result.success() : Result.error("删除失败");
    }
}
