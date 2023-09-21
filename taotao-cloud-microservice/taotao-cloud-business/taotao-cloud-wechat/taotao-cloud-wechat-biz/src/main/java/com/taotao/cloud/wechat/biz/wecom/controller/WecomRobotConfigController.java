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

package com.taotao.cloud.wechat.biz.wecom.controller;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.Res;
import cn.bootx.common.core.rest.ResResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.starter.wecom.core.robot.service.WecomRobotConfigService;
import cn.bootx.starter.wecom.dto.robot.WecomRobotConfigDto;
import cn.bootx.starter.wecom.param.robot.WecomRobotConfigParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxm
 * @since 2022/7/26
 */
@Tag(name = "")
@RestController
@RequestMapping("/wecom/robot/config")
@RequiredArgsConstructor
public class WecomRobotConfigController {
    private final WecomRobotConfigService robotConfigService;

    @Operation(summary = "新增机器人配置")
    @PostMapping("/add")
    public ResResult<Void> add(@RequestBody WecomRobotConfigParam param) {
        robotConfigService.add(param);
        return Res.ok();
    }

    @Operation(summary = "修改机器人配置")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody WecomRobotConfigParam param) {
        robotConfigService.update(param);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<WecomRobotConfigDto>> page(PageQuery PageQuery, WecomRobotConfigParam param) {
        return Res.ok(robotConfigService.page(PageQuery, param));
    }

    @Operation(summary = "查询全部")
    @GetMapping("/findAll")
    public ResResult<List<WecomRobotConfigDto>> findAll() {
        return Res.ok(robotConfigService.findAll());
    }

    @Operation(summary = "获取详情")
    @GetMapping("/findById")
    public ResResult<WecomRobotConfigDto> findById(Long id) {
        return Res.ok(robotConfigService.findById(id));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public ResResult<Void> delete(Long id) {
        robotConfigService.delete(id);
        return Res.ok();
    }

    @Operation(summary = "编码是否被使用")
    @GetMapping("/existsByCode")
    public ResResult<Boolean> existsByCode(String code) {
        return Res.ok(robotConfigService.existsByCode(code));
    }

    @Operation(summary = "编码是否被使用(不包含自己)")
    @GetMapping("/existsByCodeNotId")
    public ResResult<Boolean> existsByCode(String code, Long id) {
        return Res.ok(robotConfigService.existsByCode(code, id));
    }
}
