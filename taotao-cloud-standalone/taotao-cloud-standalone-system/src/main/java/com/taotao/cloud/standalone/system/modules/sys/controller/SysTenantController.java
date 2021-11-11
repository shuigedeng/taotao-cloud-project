package com.taotao.cloud.standalone.system.modules.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.log.annotation.SysOperaLog;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysTenant;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 租户表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/tenant")
public class SysTenantController {

    @Autowired
    private ISysTenantService sysTenantService;

    /**
     * 分页查询
     *
     * @param page      分页对象
     * @param sysTenant 租户对象
     * @return
     */
    @GetMapping("/page")
    public R getSysTenantPage(Page page, SysTenant sysTenant) {
        return R.ok(sysTenantService.page(page, Wrappers.query(sysTenant)));
    }

    /**
     * 查询全部有效的租户
     *
     * @return
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(sysTenantService.getNormalTenant());
    }


    /**
     * 新增租户
     *
     * @param sysTenant 租户
     * @return R
     */
    @SysOperaLog(descrption = "新增租户")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:tenant:add')")
    public R save(@RequestBody SysTenant sysTenant) {
        return R.ok(sysTenantService.saveTenant(sysTenant));
    }

    /**
     * 修改租户
     *
     * @param sysTenant 租户
     * @return R
     */
    @SysOperaLog(descrption = "修改租户")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:tenant:update')")
    public R updateById(@RequestBody SysTenant sysTenant) {
        return R.ok(sysTenantService.updateById(sysTenant));
    }


    /**
     * 通过id删除租户
     *
     * @param id id
     * @return R
     */
    @SysOperaLog(descrption = "删除租户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:tenant:del')")
    public R removeById(@PathVariable Integer id) {
        return R.ok(sysTenantService.removeById(id));
    }

    /**
     * 设置租户id -- 主要是第三方登录使用 目的需要发送请求 看看后面有没有办法解决
     *
     * @param tenantId
     */
    @PostMapping("/setting/{tenantId}")
    public R setting(@PathVariable Integer tenantId) {
        return R.ok();
    }


}

