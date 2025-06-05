package com.taotao.cloud.generator.maku.controller;

import lombok.AllArgsConstructor;
import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.utils.Result;
import com.taotao.cloud.generator.maku.entity.BaseClassEntity;
import com.taotao.cloud.generator.maku.service.BaseClassService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 基类管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("maku-generator/gen/baseclass")
@AllArgsConstructor
public class BaseClassController {
    private final BaseClassService baseClassService;

    @GetMapping("page")
    public Result<PageResult<BaseClassEntity>> page(Query query) {
        PageResult<BaseClassEntity> page = baseClassService.page(query);

        return Result.ok(page);
    }

    @GetMapping("list")
    public Result<List<BaseClassEntity>> list() {
        List<BaseClassEntity> list = baseClassService.getList();

        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<BaseClassEntity> get(@PathVariable("id") Long id) {
        BaseClassEntity data = baseClassService.getById(id);

        return Result.ok(data);
    }

    @PostMapping
    public Result<String> save(@RequestBody BaseClassEntity entity) {
        baseClassService.save(entity);

        return Result.ok();
    }

    @PutMapping
    public Result<String> update(@RequestBody BaseClassEntity entity) {
        baseClassService.updateById(entity);

        return Result.ok();
    }

    @DeleteMapping
    public Result<String> delete(@RequestBody Long[] ids) {
        baseClassService.removeBatchByIds(Arrays.asList(ids));

        return Result.ok();
    }
}
