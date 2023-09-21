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

package com.taotao.cloud.wechat.biz.wechat.controller;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.Res;
import cn.bootx.common.core.rest.ResResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.starter.auth.util.SecurityUtil;
import cn.bootx.starter.wechat.core.notice.service.WeChatTemplateService;
import cn.bootx.starter.wechat.dto.notice.WeChatTemplateDto;
import cn.bootx.starter.wechat.param.notice.WeChatTemplateParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxm
 * @since 2022/7/16
 */
@Tag(name = "微信模板消息")
@RestController
@RequestMapping("/wechat/template")
@RequiredArgsConstructor
public class WeChatTemplateController {

    private final WeChatTemplateService weChatTemplateService;

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody WeChatTemplateParam param) {
        weChatTemplateService.update(param);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<WeChatTemplateDto> findById(Long id) {
        return Res.ok(weChatTemplateService.findById(id));
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<WeChatTemplateDto>> page(PageQuery PageQuery, WeChatTemplateParam weChatTemplateParam) {
        return Res.ok(weChatTemplateService.page(PageQuery, weChatTemplateParam));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(weChatTemplateService.existsByCode(code, id));
    }

    @Operation(summary = "同步消息模板数据")
    @PostMapping("/sync")
    public ResResult<Void> sync() {
        // 为了获取用户生效, 测试用
        SecurityUtil.getUserId();
        weChatTemplateService.sync();
        return Res.ok();
    }
}
