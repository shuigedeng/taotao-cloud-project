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

package com.taotao.cloud.sa.just.biz.just.justauth.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthConfig;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthConfigExport;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthConfigImport;
import com.taotao.cloud.sa.just.biz.just.justauth.service.JustAuthConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 租户第三方登录功能配置表 前端控制器
 *
 * @since 2022-05-16
 */
@RestController
@RequestMapping("/extension/justauth/config")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(
        value = "JustAuthConfigController|租户第三方登录功能配置表前端控制器",
        tags = {"第三方登录功能配置"})
@RefreshScope
public class JustAuthConfigController {

    private final JustAuthConfigService justAuthConfigService;

    /**
     * 查询租户第三方登录功能配置表列表
     *
     * @param queryJustAuthConfigDTO
     * @param page
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询租户第三方登录功能配置表列表")
    public Result<Page<JustAuthConfigDTO>> list(
            QueryJustAuthConfigDTO queryJustAuthConfigDTO, Page<JustAuthConfigDTO> page) {
        Page<JustAuthConfigDTO> pageJustAuthConfig =
                justAuthConfigService.queryJustAuthConfigList(page, queryJustAuthConfigDTO);
        return Result.success(pageJustAuthConfig);
    }

    /**
     * 查询租户第三方登录功能配置表详情
     *
     * @param queryJustAuthConfigDTO
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "查询租户第三方登录功能配置表详情")
    public Result<?> query(QueryJustAuthConfigDTO queryJustAuthConfigDTO) {
        JustAuthConfigDTO justAuthConfigDTO = justAuthConfigService.queryJustAuthConfig(queryJustAuthConfigDTO);
        return Result.success(justAuthConfigDTO);
    }

    /**
     * 添加租户第三方登录功能配置表
     *
     * @param justAuthConfig
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加租户第三方登录功能配置表")
    public Result<?> create(@RequestBody CreateJustAuthConfigDTO justAuthConfig) {
        boolean result = justAuthConfigService.createJustAuthConfig(justAuthConfig);
        return Result.success(result);
    }

    /**
     * 修改租户第三方登录功能配置表
     *
     * @param justAuthConfig
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新租户第三方登录功能配置表")
    public Result<?> update(@RequestBody UpdateJustAuthConfigDTO justAuthConfig) {
        boolean result = justAuthConfigService.updateJustAuthConfig(justAuthConfig);
        return Result.success(result);
    }

    /**
     * 删除租户第三方登录功能配置表
     *
     * @param justAuthConfigId
     * @return
     */
    @PostMapping("/delete/{justAuthConfigId}")
    @ApiOperation(value = "删除租户第三方登录功能配置表")
    @ApiImplicitParam(
            paramType = "path",
            name = "justAuthConfigId",
            value = "租户第三方登录功能配置表ID",
            required = true,
            dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("justAuthConfigId") Long justAuthConfigId) {
        if (null == justAuthConfigId) {
            return Result.error("ID不能为空");
        }
        boolean result = justAuthConfigService.deleteJustAuthConfig(justAuthConfigId);
        return Result.success(result);
    }

    /**
     * 批量删除租户第三方登录功能配置表
     *
     * @param justAuthConfigIds
     * @return
     */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除租户第三方登录功能配置表")
    @ApiImplicitParam(
            name = "justAuthConfigIds",
            value = "租户第三方登录功能配置表ID列表",
            required = true,
            dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> justAuthConfigIds) {
        if (CollectionUtils.isEmpty(justAuthConfigIds)) {
            return Result.error("租户第三方登录功能配置表ID列表不能为空");
        }
        boolean result = justAuthConfigService.batchDeleteJustAuthConfig(justAuthConfigIds);
        return Result.success(result);
    }

    /**
     * 修改租户第三方登录功能配置表状态
     *
     * @param justAuthConfigId
     * @param status
     * @return
     */
    @PostMapping("/status/{justAuthConfigId}/{status}")
    @ApiOperation(value = "修改租户第三方登录功能配置表状态")
    @ApiImplicitParams({
        @ApiImplicitParam(
                name = "justAuthConfigId",
                value = "租户第三方登录功能配置表ID",
                required = true,
                dataTypeClass = Long.class,
                paramType = "path"),
        @ApiImplicitParam(
                name = "status",
                value = "租户第三方登录功能配置表状态",
                required = true,
                dataTypeClass = Integer.class,
                paramType = "path")
    })
    public Result<?> updateStatus(
            @PathVariable("justAuthConfigId") Long justAuthConfigId, @PathVariable("status") Integer status) {

        if (null == justAuthConfigId || StringUtils.isEmpty(status)) {
            return Result.error("ID和状态不能为空");
        }
        UpdateJustAuthConfigDTO justAuthConfig = new UpdateJustAuthConfigDTO();
        justAuthConfig.setId(justAuthConfigId);
        justAuthConfig.setStatus(status);
        boolean result = justAuthConfigService.updateJustAuthConfig(justAuthConfig);
        return Result.success(result);
    }

    /**
     * 批量导出租户第三方登录功能配置表数据
     *
     * @param response
     * @param queryJustAuthConfigDTO
     * @throws IOException
     */
    @GetMapping("/download")
    @ApiOperation("导出数据")
    public void download(HttpServletResponse response, QueryJustAuthConfigDTO queryJustAuthConfigDTO)
            throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("租户第三方登录功能配置表数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<JustAuthConfigDTO> justAuthConfigList =
                justAuthConfigService.queryJustAuthConfigList(queryJustAuthConfigDTO);
        List<JustAuthConfigExport> justAuthConfigExportList = new ArrayList<>();
        for (JustAuthConfigDTO justAuthConfigDTO : justAuthConfigList) {
            JustAuthConfigExport justAuthConfigExport =
                    BeanCopierUtils.copyByClass(justAuthConfigDTO, JustAuthConfigExport.class);
            justAuthConfigExportList.add(justAuthConfigExport);
        }
        String sheetName = "租户第三方登录功能配置表数据列表";
        EasyExcel.write(response.getOutputStream(), JustAuthConfigExport.class)
                .sheet(sheetName)
                .doWrite(justAuthConfigExportList);
    }

    /**
     * 批量上传租户第三方登录功能配置表数据
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ApiOperation("批量上传数据")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
        List<JustAuthConfigImport> justAuthConfigImportList = EasyExcel.read(
                        file.getInputStream(), JustAuthConfigImport.class, null)
                .sheet()
                .doReadSync();
        if (!CollectionUtils.isEmpty(justAuthConfigImportList)) {
            List<JustAuthConfig> justAuthConfigList = new ArrayList<>();
            justAuthConfigImportList.stream().forEach(justAuthConfigImport -> {
                justAuthConfigList.add(BeanCopierUtils.copyByClass(justAuthConfigImport, JustAuthConfig.class));
            });
            justAuthConfigService.saveBatch(justAuthConfigList);
        }
        return Result.success();
    }

    /**
     * 下载租户第三方登录功能配置表数据导入模板
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/template")
    @ApiOperation("导出上传模板")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("租户第三方登录功能配置表数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "租户第三方登录功能配置表数据列表";
        EasyExcel.write(response.getOutputStream(), JustAuthConfigImport.class)
                .sheet(sheetName)
                .doWrite(new ArrayList<>());
    }
}
