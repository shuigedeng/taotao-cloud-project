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

package com.taotao.cloud.im.biz.platform.modules.collect.controller;

import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.collect.domain.ChatCollect;
import com.platform.modules.collect.service.ChatCollectService;
import com.platform.modules.collect.vo.CollectVo01;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 收藏 */
@RestController
@Slf4j
@RequestMapping("/collect")
public class CollectController extends BaseController {

    @Resource
    private ChatCollectService collectService;

    /** 增加 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/add")
    public AjaxResult addCollect(@Validated @RequestBody CollectVo01 collectVo) {
        collectService.addCollect(collectVo);
        return AjaxResult.success();
    }

    /** 删除 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/remove/{collectId}")
    public AjaxResult remove(@PathVariable Long collectId) {
        collectService.deleteCollect(collectId);
        return AjaxResult.success();
    }

    /** 列表 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/list")
    public TableDataInfo list(ChatCollect collect) {
        startPage("create_time desc");
        return getDataTable(collectService.collectList(collect));
    }
}
