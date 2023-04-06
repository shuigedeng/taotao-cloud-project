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

package com.taotao.cloud.wechat.biz.niefy.modules.sys.controller;

import com.github.niefy.common.annotation.SysLog;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;
import com.github.niefy.common.validator.ValidatorUtils;
import com.github.niefy.modules.sys.entity.SysConfigEntity;
import com.github.niefy.modules.sys.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置信息
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/config")
@Api(tags = {"系统配置信息"})
public class SysConfigController extends AbstractController {
    @Autowired
    private SysConfigService sysConfigService;

    /** 所有配置列表 */
    @GetMapping("/list")
    @RequiresPermissions("sys:config:list")
    @ApiOperation(value = "配置项列表", notes = "配置项需专业人员修改")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysConfigService.queryPage(params);

        return R.ok().put("page", page);
    }

    /** 配置信息 */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:config:info")
    @ApiOperation(value = "配置详情", notes = "")
    public R info(@PathVariable("id") Long id) {
        SysConfigEntity config = sysConfigService.getById(id);

        return R.ok().put("config", config);
    }

    /** 保存配置 */
    @SysLog("保存配置")
    @PostMapping("/save")
    @RequiresPermissions("sys:config:save")
    @ApiOperation(value = "保存配置", notes = "")
    public R save(@RequestBody SysConfigEntity config) {
        ValidatorUtils.validateEntity(config);

        sysConfigService.saveConfig(config);

        return R.ok();
    }

    /** 修改配置 */
    @SysLog("修改配置")
    @PostMapping("/update")
    @RequiresPermissions("sys:config:update")
    @ApiOperation(value = "修改配置", notes = "")
    public R update(@RequestBody SysConfigEntity config) {
        ValidatorUtils.validateEntity(config);

        sysConfigService.update(config);

        return R.ok();
    }

    /** 删除配置 */
    @SysLog("删除配置")
    @PostMapping("/delete")
    @RequiresPermissions("sys:config:delete")
    @ApiOperation(value = "删除配置", notes = "")
    public R delete(@RequestBody Long[] ids) {
        sysConfigService.deleteBatch(ids);

        return R.ok();
    }
}
