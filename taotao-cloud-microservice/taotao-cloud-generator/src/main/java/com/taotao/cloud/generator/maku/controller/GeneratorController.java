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
import com.taotao.cloud.generator.maku.common.utils.Result;
import com.taotao.cloud.generator.maku.service.GeneratorService;
import com.taotao.cloud.generator.maku.vo.PreviewVO;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 代码生成
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("maku-generator/gen/generator")
@AllArgsConstructor
public class GeneratorController {
    private final GeneratorService generatorService;

    /**
     * 生成代码（zip压缩包）
     */
    @GetMapping("download")
    public void download(String tableIds, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        // 生成代码
        for (String tableId : tableIds.split(",")) {
            generatorService.downloadCode(Long.parseLong(tableId), zip);
        }

        IoUtil.close(zip);

        // zip压缩包数据
        byte[] data = outputStream.toByteArray();

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"maku.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), false, data);
    }

    /**
     * 生成代码（自定义目录）
     */
    @ResponseBody
    @PostMapping("code")
    public Result<String> code(@RequestBody Long[] tableIds) throws Exception {
        // 生成代码
        for (Long tableId : tableIds) {
            generatorService.generatorCode(tableId);
        }

        return Result.ok();
    }

    /**
     * 预览代码
     */
    @GetMapping("/preview")
    public Result<List<PreviewVO>> preview(@RequestParam Long tableId) throws Exception {
        List<PreviewVO> results = generatorService.preview(tableId);
        return Result.ok(results);
    }
}
