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

package com.taotao.cloud.workflow.biz.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import taotao.cloud.workflow.biz.base.ActionResult;
import taotao.cloud.workflow.biz.base.SysConfigApi;
import taotao.cloud.workflow.biz.model.BaseSystemInfo;
import taotao.cloud.workflow.biz.util.NoDataSourceBind;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取AppVersion
 *
 * @author ：
 * @version: V3.1.0
 *  
 * @since ：2022/3/31 11:26
 */
@Api(tags = "获取APP版本号", value = "AppVersion")
@RestController
@RequestMapping("/app")
public class AppVersionController {

    @Autowired
    private SysConfigApi sysConfigApi;

    /**
     * 判断是否需要验证码
     *
     * @return
     */
    @NoDataSourceBind
    @ApiOperation("判断是否需要验证码")
    @GetMapping("/Version")
    public ActionResult getAppVersion() {
        BaseSystemInfo sysConfigInfo = sysConfigApi.getSysConfigInfo();
        String sysVersion = "";
        Map<String, String> map = new HashedMap<>();
        if (sysConfigInfo != null) {
            sysVersion = sysConfigInfo.getSysVersion();
        }
        map.put("sysVersion", sysVersion);
        return ActionResult.success(map);
    }
}
