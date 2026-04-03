package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.tenant.biz.application.dto.SysDictTypeDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysDictTypeQuery;
import com.mdframe.forge.plugin.system.entity.SysDictType;
import com.taotao.cloud.tenant.biz.application.service.service.ISysDictTypeService;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;
import com.mdframe.forge.starter.core.domain.PageQuery;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典类型Controller
 */
@RestController
@RequestMapping("/system/dict/type")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysDictTypeController {

    private final ISysDictTypeService dictTypeService;

    /**
     * 分页查询字典类型列表
     */
    @GetMapping("/page")
    public Result<Page<SysDictType>> page(PageQuery pageQuery, SysDictTypeQuery query) {
        Page<SysDictType> page = dictTypeService.selectDictTypePage(pageQuery, query);
        return Result.success(page);
    }

    /**
     * 查询字典类型列表
     */
    @GetMapping("/list")
    public Result<List<SysDictType>> list(SysDictTypeQuery query) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(query);
        return Result.success(list);
    }

    /**
     * 根据ID查询字典类型详情
     */
    @PostMapping("/getById")
    public Result<SysDictType> getById(@RequestParam Long dictId) {
        SysDictType dictType = dictTypeService.selectDictTypeById(dictId);
        return Result.success(dictType);
    }

    /**
     * 新增字典类型
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SysDictTypeDTO dto) {
        boolean result = dictTypeService.insertDictType(dto);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    public Result<Void> edit(@RequestBody SysDictTypeDTO dto) {
        boolean result = dictTypeService.updateDictType(dto);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除字典类型
     */
    @PostMapping("/remove")
    public Result<Void> remove(@RequestParam Long dictId) {
        boolean result = dictTypeService.deleteDictTypeById(dictId);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除字典类型
     */
    @PostMapping("/removeBatch")
    public Result<Void> removeBatch(@RequestBody Long[] dictIds) {
        boolean result = dictTypeService.deleteDictTypeByIds(dictIds);
        return result ? Result.success() : Result.error("批量删除失败");
    }
}
