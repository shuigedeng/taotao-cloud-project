package com.taotao.cloud.standalone.system.modules.sys.controller;


import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.log.annotation.SysOperaLog;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysDept;
import com.taotao.cloud.standalone.system.modules.sys.dto.DeptDTO;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysDeptService;
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
 * 部门管理 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService deptService;

    /**
     * 保存部门信息
     *
     * @param sysDept
     * @return
     */
    @SysOperaLog(descrption = "保存部门信息")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:dept:add')")
    public R save(@RequestBody SysDept sysDept) {
        return R.ok(deptService.save(sysDept));
    }

    /**
     * 获取部门信息
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('sys:dept:view')")
    public R getDeptList() {
        return R.ok(deptService.selectDeptList());
    }

    /**
     * 获取部门树
     * @return
     */
    @GetMapping("/tree")
    public R getDeptTree() {
        return R.ok(deptService.getDeptTree());
    }


    /**
     * 更新部门信息
     *
     * @return
     */
    @SysOperaLog(descrption = "更新部门信息")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:dept:update')")
    public R update(@RequestBody DeptDTO deptDto) {
        return R.ok(deptService.updateDeptById(deptDto));
    }

    /**
     * 根据id删除部门信息
     *
     * @return
     */
    @SysOperaLog(descrption = "根据id删除部门信息")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:dept:delete')")
    public R delete(@PathVariable("id") Integer id) {
        return R.ok(deptService.removeById(id));
    }


}

