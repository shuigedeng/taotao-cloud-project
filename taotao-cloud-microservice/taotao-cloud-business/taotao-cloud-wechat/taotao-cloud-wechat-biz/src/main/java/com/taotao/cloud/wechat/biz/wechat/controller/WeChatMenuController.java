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
import cn.bootx.starter.wechat.core.menu.service.WeChatMenuService;
import cn.bootx.starter.wechat.dto.menu.WeChatMenuDto;
import cn.bootx.starter.wechat.param.menu.WeChatMenuParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 微信菜单管理
 *
 * @author xxm
 * @since 2022/8/6
 */
@Tag(name = "微信菜单管理")
@RestController
@RequestMapping("/wechat/menu")
@RequiredArgsConstructor
public class WeChatMenuController {
    private final WeChatMenuService weChatMenuService;

    @Operation(summary = "添加")
    @PostMapping(value = "/add")
    public ResResult<Void> add(@RequestBody WeChatMenuParam param) {
        weChatMenuService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改")
    @PostMapping(value = "/update")
    public ResResult<Void> update(@RequestBody WeChatMenuParam param) {
        weChatMenuService.update(param);
        return Res.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete")
    public ResResult<Void> delete(Long id) {
        weChatMenuService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "通过ID查询")
    @GetMapping(value = "/findById")
    public ResResult<WeChatMenuDto> findById(Long id) {
        return Res.ok(weChatMenuService.findById(id));
    }

    @Operation(summary = "查询所有")
    @GetMapping(value = "/findAll")
    public ResResult<List<WeChatMenuDto>> findAll() {
        return Res.ok(weChatMenuService.findAll());
    }

    @Operation(summary = "分页查询")
    @GetMapping(value = "/page")
    public ResResult<PageResult<WeChatMenuDto>> page(PageQuery PageQuery, WeChatMenuParam weChatMenuParam) {
        return Res.ok(weChatMenuService.page(PageQuery, weChatMenuParam));
    }

    @Operation(summary = "发布菜单")
    @PostMapping("/publish")
    public ResResult<Void> publish(Long id) {
        weChatMenuService.publish(id);
        return Res.ok();
    }

    @Operation(summary = "导入微信自定义菜单到系统中")
    @PostMapping("/importMenu")
    public ResResult<Void> importMenu() {
        weChatMenuService.importMenu();
        return Res.ok();
    }

    @Operation(summary = "清空微信自定义菜单")
    @PostMapping("/clearMenu")
    public ResResult<Void> clearMenu() {
        weChatMenuService.clearMenu();
        return Res.ok();
    }
}
