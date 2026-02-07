/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.generator.maku.controller;

import cn.hutool.core.io.IoUtil;
import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.utils.Result;
import com.taotao.cloud.generator.maku.entity.ProjectModifyEntity;
import com.taotao.cloud.generator.maku.service.ProjectModifyService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 项目名变更
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("maku-generator/gen/project")
@AllArgsConstructor
public class ProjectModifyController {
    private final ProjectModifyService projectModifyService;

    @GetMapping("page")
    public Result<PageResult<ProjectModifyEntity>> page(@Valid Query query) {
        PageResult<ProjectModifyEntity> page = projectModifyService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    public Result<ProjectModifyEntity> get(@PathVariable("id") Long id) {
        ProjectModifyEntity entity = projectModifyService.getById(id);

        return Result.ok(entity);
    }

    @PostMapping
    public Result<String> save(@RequestBody ProjectModifyEntity entity) {
        projectModifyService.save(entity);

        return Result.ok();
    }

    @PutMapping
    public Result<String> update(@RequestBody @Valid ProjectModifyEntity entity) {
        projectModifyService.updateById(entity);

        return Result.ok();
    }

    @DeleteMapping
    public Result<String> delete(@RequestBody List<Long> idList) {
        projectModifyService.removeByIds(idList);

        return Result.ok();
    }

    /**
     * 源码下载
     */
    @GetMapping("download/{id}")
    public void download(@PathVariable("id") Long id, HttpServletResponse response)
            throws Exception {
        // 项目信息
        ProjectModifyEntity project = projectModifyService.getById(id);

        byte[] data = projectModifyService.download(project);

        response.reset();
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + project.getModifyProjectName() + ".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), false, data);
    }
}
