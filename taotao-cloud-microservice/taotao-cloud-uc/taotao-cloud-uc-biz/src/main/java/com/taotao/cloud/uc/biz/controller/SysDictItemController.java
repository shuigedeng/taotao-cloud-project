package com.taotao.cloud.uc.biz.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.taotao.cloud.core.model.PageResult;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.uc.api.dto.dictItem.DictItemDTO;
import com.taotao.cloud.uc.api.query.dictItem.DictItemPageQuery;
import com.taotao.cloud.uc.api.query.dictItem.DictItemQuery;
import com.taotao.cloud.uc.api.vo.dictItem.DictItemVO;
import com.taotao.cloud.uc.biz.entity.SysDictItem;
import com.taotao.cloud.uc.biz.service.ISysDictItemService;
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
 * 字典项管理API
 *
 * @author dengtao
 * @date 2020/4/30 11:21
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/dict/item")
@Api(value = "字典项管理API", tags = {"字典项管理API"})
public class SysDictItemController {

    private final ISysDictItemService dictItemService;

    @ApiOperation("添加字典项详情")
    @RequestOperateLog(description = "添加字典项详情")
    @PreAuthorize("hasAuthority('sys:dictItem:add')")
    @PostMapping
    public Result<Boolean> save(@Validated @RequestBody DictItemDTO dictItemDTO) {
        SysDictItem item = dictItemService.save(dictItemDTO);
        return Result.succeed(Objects.nonNull(item));
    }

    @ApiOperation("更新字典项详情")
    @RequestOperateLog(description = "更新字典项详情")
    @PreAuthorize("hasAuthority('sys:dictItem:edit')")
    @PutMapping("/{id}")
    public Result<Boolean> updateById(@PathVariable(value = "id") Long id,
                                      @Validated @RequestBody DictItemDTO dictItemDTO) {
        SysDictItem item = dictItemService.updateById(id, dictItemDTO);
        return Result.succeed(Objects.nonNull(item));
    }

    @ApiOperation("根据id删除字典项详情")
    @RequestOperateLog(description = "根据id删除字典项详情")
    @PreAuthorize("hasAuthority('sys:dictItem:del')")
    @DeleteMapping("/{id:[0-9]*}")
    public Result<Boolean> deleteById(@PathVariable("id") Long id) {
        Boolean result = dictItemService.deleteById(id);
        return Result.succeed(result);
    }

    @ApiOperation("分页查询字典详情")
    @RequestOperateLog(description = "分页查询字典详情")
    @GetMapping("/page")
    public PageResult<DictItemVO> getPage(@Validated DictItemPageQuery dictItemPageQuery) {
        Pageable pageable = PageRequest.of(dictItemPageQuery.getCurrentPage(), dictItemPageQuery.getPageSize());
        Page<SysDictItem> page = dictItemService.getPage(pageable, dictItemPageQuery);
        List<DictItemVO> collect = page.stream().filter(Objects::nonNull)
                .map(tuple -> {
                    DictItemVO vo = DictItemVO.builder().build();
                    BeanUtil.copyProperties(tuple, vo, CopyOptions.create().ignoreNullValue().ignoreError());
                    return vo;
                }).collect(Collectors.toList());
        Page<DictItemVO> result = new PageImpl<>(collect, pageable, page.getTotalElements());
        return PageResult.succeed(result);
    }

    @ApiOperation("查询字典详情")
    @RequestOperateLog(description = "查询字典详情")
    @GetMapping("/info")
    public Result<List<DictItemVO>> getInfo(@Validated DictItemQuery dictItemQuery) {
        List<SysDictItem> itmes = dictItemService.getInfo(dictItemQuery);
        List<DictItemVO> collect = itmes.stream().filter(Objects::nonNull)
                .map(tuple -> {
                    DictItemVO vo = DictItemVO.builder().build();
                    BeanUtil.copyProperties(tuple, vo, CopyOptions.create().ignoreNullValue().ignoreError());
                    return vo;
                }).collect(Collectors.toList());
        return Result.succeed(collect);
    }
}
