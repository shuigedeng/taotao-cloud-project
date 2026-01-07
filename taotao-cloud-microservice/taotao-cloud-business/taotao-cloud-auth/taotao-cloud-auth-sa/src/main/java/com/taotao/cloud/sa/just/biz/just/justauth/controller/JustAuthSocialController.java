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
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocialExport;
import com.taotao.cloud.sa.just.biz.just.justauth.service.JustAuthSocialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户第三方登录渠道配置表 前端控制器
 *
 * @since 2022-05-23
 */
@RestController
@RequestMapping("/extension/justauth/social")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(
        value = "JustAuthSocialController|租户第三方登录渠道配置表前端控制器",
        tags = {"第三方登录渠道配置"})
@RefreshScope
public class JustAuthSocialController {

    private final JustAuthSocialService justAuthSocialService;

    /**
     * 查询租户第三方登录功能配置表列表
     *
     * @param queryJustAuthSocialDTO
     * @param page
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询租户第三方登录功能配置表列表")
    public Result<Page<JustAuthSocialDTO>> list(
            QueryJustAuthSocialDTO queryJustAuthSocialDTO, Page<JustAuthSocialDTO> page) {
        Page<JustAuthSocialDTO> pageJustAuthSocial =
                justAuthSocialService.queryJustAuthSocialList(page, queryJustAuthSocialDTO);
        return Result.success(pageJustAuthSocial);
    }

    /**
     * 查询租户第三方登录功能配置表详情
     *
     * @param queryJustAuthSocialDTO
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "查询租户第三方登录功能配置表详情")
    public Result<?> query(QueryJustAuthSocialDTO queryJustAuthSocialDTO) {
        JustAuthSocialDTO justAuthSocialDTO = justAuthSocialService.queryJustAuthSocial(queryJustAuthSocialDTO);
        return Result.success(justAuthSocialDTO);
    }

    /**
     * 添加租户第三方登录功能配置表
     *
     * @param justAuthSocial
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "添加租户第三方登录功能配置表")
    public Result<?> create(@RequestBody CreateJustAuthSocialDTO justAuthSocial) {
        justAuthSocialService.createJustAuthSocial(justAuthSocial);
        return Result.success();
    }

    /**
     * 修改租户第三方登录功能配置表
     *
     * @param justAuthSocial
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新租户第三方登录功能配置表")
    public Result<?> update(@RequestBody UpdateJustAuthSocialDTO justAuthSocial) {
        boolean result = justAuthSocialService.updateJustAuthSocial(justAuthSocial);
        return Result.success(result);
    }

    /**
     * 删除租户第三方登录功能配置表
     *
     * @param justAuthSocialId
     * @return
     */
    @PostMapping("/delete/{justAuthSocialId}")
    @ApiOperation(value = "删除租户第三方登录功能配置表")
    @ApiImplicitParam(
            paramType = "path",
            name = "justAuthSocialId",
            value = "租户第三方登录功能配置表ID",
            required = true,
            dataTypeClass = Long.class)
    public Result<?> delete(@PathVariable("justAuthSocialId") Long justAuthSocialId) {
        if (null == justAuthSocialId) {
            return Result.error("ID不能为空");
        }
        boolean result = justAuthSocialService.deleteJustAuthSocial(justAuthSocialId);
        return Result.result(result);
    }

    /**
     * 批量删除租户第三方登录功能配置表
     *
     * @param justAuthSocialIds
     * @return
     */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除租户第三方登录功能配置表")
    @ApiImplicitParam(
            name = "justAuthSocialIds",
            value = "租户第三方登录功能配置表ID列表",
            required = true,
            dataTypeClass = List.class)
    public Result<?> batchDelete(@RequestBody List<Long> justAuthSocialIds) {
        if (CollectionUtils.isEmpty(justAuthSocialIds)) {
            return Result.error("租户第三方登录功能配置表ID列表不能为空");
        }
        boolean result = justAuthSocialService.batchDeleteJustAuthSocial(justAuthSocialIds);
        return Result.success(result);
    }

    /**
     * 批量导出租户第三方登录功能配置表数据
     *
     * @param response
     * @param queryJustAuthSocialDTO
     * @throws IOException
     */
    @GetMapping("/download")
    @ApiOperation("导出数据")
    public void download(HttpServletResponse response, QueryJustAuthSocialDTO queryJustAuthSocialDTO)
            throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("租户第三方登录功能配置表数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<JustAuthSocialDTO> justAuthSocialList =
                justAuthSocialService.queryJustAuthSocialList(queryJustAuthSocialDTO);
        List<JustAuthSocialExport> justAuthSocialExportList = new ArrayList<>();
        for (JustAuthSocialDTO justAuthSocialDTO : justAuthSocialList) {
            JustAuthSocialExport justAuthSocialExport =
                    BeanCopierUtils.copyByClass(justAuthSocialDTO, JustAuthSocialExport.class);
            justAuthSocialExportList.add(justAuthSocialExport);
        }
        String sheetName = "租户第三方登录功能配置表数据列表";
        EasyExcel.write(response.getOutputStream(), JustAuthSocialExport.class)
                .sheet(sheetName)
                .doWrite(justAuthSocialExportList);
    }
}
