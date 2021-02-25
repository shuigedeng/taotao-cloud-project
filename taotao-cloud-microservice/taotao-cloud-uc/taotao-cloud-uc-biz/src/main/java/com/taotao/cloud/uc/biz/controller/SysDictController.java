package com.taotao.cloud.uc.biz.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.taotao.cloud.core.model.PageResult;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.uc.api.dto.dict.DictDTO;
import com.taotao.cloud.uc.api.query.dict.DictPageQuery;
import com.taotao.cloud.uc.api.vo.dict.DictVO;
import com.taotao.cloud.uc.biz.entity.SysDict;
import com.taotao.cloud.uc.biz.service.ISysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 字典管理API
 *
 * @author dengtao
 * @date 2020/4/30 11:13
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/dict")
@Api(value = "字典管理API", tags = {"字典管理API"})
public class SysDictController {

    private final ISysDictService dictService;

    @ApiOperation("添加字典信息")
    @RequestOperateLog(description = "添加字典信息")
    @PreAuthorize("hasAuthority('sys:dict:save')")
    @PostMapping
    public Result<Boolean> save(@Validated @RequestBody DictDTO dictDTO) {
        SysDict dict = new SysDict();
        BeanUtil.copyProperties(dictDTO, dict, CopyOptions.create().ignoreNullValue().ignoreError());
        SysDict sysDict = dictService.save(dict);
        return Result.succeed(Objects.nonNull(sysDict));
    }

    @ApiOperation("根据id更新字典信息")
    @RequestOperateLog(description = "根据id更新字典信息")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    @PutMapping("/{id:[0-9]*}")
    public Result<Boolean> updateById(@PathVariable(value = "id") Long id,
                                      @Validated @RequestBody DictDTO dictDTO) {
        SysDict dict = dictService.findById(id);
        BeanUtil.copyProperties(dictDTO, dict, CopyOptions.create().ignoreNullValue().ignoreError());
        SysDict sysDict = dictService.update(dict);
        return Result.succeed(Objects.nonNull(sysDict));
    }

    @ApiOperation("根据code更新字典信息")
    @RequestOperateLog(description = "根据code更新字典信息")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    @PutMapping("/code/{code}")
    public Result<Boolean> updateByCode(@PathVariable(value = "code") String code,
                                        @Validated @RequestBody DictDTO dictDTO) {
        SysDict dict = dictService.findByCode(code);
        BeanUtil.copyProperties(dictDTO, dict, CopyOptions.create().ignoreNullValue().ignoreError());
        SysDict sysDict = dictService.update(dict);
        return Result.succeed(Objects.nonNull(sysDict));
    }

    @ApiOperation("查询所有字典集合")
    @RequestOperateLog(description = "查询所有字典集合")
    @PreAuthorize("hasAuthority('sys:dipt:view')")
    @GetMapping
    public Result<List<DictVO>> getAll() {
        List<SysDict> sysDicts = dictService.getAll();
        List<DictVO> dictList = sysDicts.stream().filter(Objects::nonNull)
                .map(tuple -> {
                    DictVO vo = DictVO.builder().build();
                    BeanUtil.copyProperties(tuple, vo, CopyOptions.create().ignoreNullValue().ignoreError());
                    return vo;
                }).collect(Collectors.toList());
        return Result.succeed(dictList);
    }

    @ApiOperation("分页查询字典集合")
    @RequestOperateLog(description = "分页查询字典集合")
    @PreAuthorize("hasAuthority('sys:dict:view')")
    @GetMapping("/page")
    public PageResult<DictVO> getPage(@Validated DictPageQuery dictPageQuery) {
        Pageable pageable = PageRequest.of(dictPageQuery.getCurrentPage(), dictPageQuery.getPageSize());
        Page<SysDict> page = dictService.getPage(pageable, dictPageQuery);
        List<DictVO> collect = page.stream().filter(Objects::nonNull)
                .map(tuple -> {
                    DictVO vo = DictVO.builder().build();
                    BeanUtil.copyProperties(tuple, vo, CopyOptions.create().ignoreNullValue().ignoreError());
                    return vo;
                }).collect(Collectors.toList());
        Page<DictVO> result = new PageImpl<>(collect, pageable, page.getTotalElements());
        return PageResult.succeed(result);
    }

    @ApiOperation("根据id删除字典")
    @RequestOperateLog(description = "根据id删除字典")
    @PreAuthorize("hasAuthority('sys:dict:del')")
    @DeleteMapping("/{id:[0-9]*}")
    public Result<Boolean> deleteById(@PathVariable(value = "id") Long id) {
        Boolean result = dictService.removeById(id);
        return Result.succeed(result);
    }

    @ApiOperation("根据code删除字典")
    @RequestOperateLog(description = "根据code删除字典")
    @PreAuthorize("hasAuthority('sys:dict:del')")
    @DeleteMapping("/code/{code}")
    public Result<Boolean> deleteByCode(@PathVariable(value = "code") String code) {
        Boolean result = dictService.deleteByCode(code);
        return Result.succeed(result);
    }
}

