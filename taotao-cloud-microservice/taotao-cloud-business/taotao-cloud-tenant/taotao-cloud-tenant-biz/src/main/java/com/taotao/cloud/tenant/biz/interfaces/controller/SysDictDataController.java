package com.taotao.cloud.tenant.biz.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.tenant.biz.application.dto.SysDictDataDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysDictDataQuery;
import com.mdframe.forge.plugin.system.entity.SysDictData;
import com.taotao.cloud.tenant.biz.application.service.service.ISysDictDataService;
import com.mdframe.forge.starter.core.annotation.api.ApiPermissionIgnore;
import com.mdframe.forge.starter.core.domain.PageQuery;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典数据Controller
 */
@RestController
@RequestMapping("/system/dict/data")
@RequiredArgsConstructor
//@ApiDecrypt
//@ApiEncrypt
//@ApiPermissionIgnore
public class SysDictDataController {

    private final ISysDictDataService dictDataService;

    /**
     * 分页查询字典数据列表
     */
    @GetMapping("/page")
    public Result<Page<SysDictData>> page(PageQuery pageQuery, SysDictDataQuery query) {
        Page<SysDictData> page = dictDataService.selectDictDataPage(pageQuery, query);
        return Result.success(page);
    }

    /**
     * 查询字典数据列表
     */
    @GetMapping("/list")
    public Result<List<SysDictData>> list(SysDictDataQuery query) {
        List<SysDictData> list = dictDataService.selectDictDataList(query);
        return Result.success(list);
    }

    /**
     * 根据字典类型查询字典数据
     */
    @GetMapping("/type/{dictType}")
    public Result<List<SysDictData>> getByType(@PathVariable String dictType) {
        List<SysDictData> list = dictDataService.selectDictDataByType(dictType);
        return Result.success(list);
    }

    /**
     * 根据ID查询字典数据详情
     */
    @PostMapping("/getById")
    public Result<SysDictData> getById(@RequestParam Long dictCode) {
        SysDictData dictData = dictDataService.selectDictDataById(dictCode);
        return Result.success(dictData);
    }

    /**
     * 新增字典数据
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody SysDictDataDTO dto) {
        boolean result = dictDataService.insertDictData(dto);
        return result ? Result.success() : Result.error("新增失败");
    }

    /**
     * 修改字典数据
     */
    @PostMapping("/edit")
    public Result<Void> edit(@RequestBody SysDictDataDTO dto) {
        boolean result = dictDataService.updateDictData(dto);
        return result ? Result.success() : Result.error("修改失败");
    }

    /**
     * 删除字典数据
     */
    @PostMapping("/remove")
    public Result<Void> remove(@RequestParam Long dictCode) {
        boolean result = dictDataService.deleteDictDataById(dictCode);
        return result ? Result.success() : Result.error("删除失败");
    }

    /**
     * 批量删除字典数据
     */
    @PostMapping("/removeBatch")
    public Result<Void> removeBatch(@RequestBody Long[] dictCodes) {
        boolean result = dictDataService.deleteDictDataByIds(dictCodes);
        return result ? Result.success() : Result.error("批量删除失败");
    }
}
