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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.manage;

import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.entity.TemplateMsgLog;
import com.github.niefy.modules.wx.service.TemplateMsgLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.Map;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 模版消息发送记录
 *
 * @author niefy
 * @email niefy@qq.com
 * @since 2019-11-12 18:30:15
 */
@RestController
@RequestMapping("/manage/templateMsgLog")
@Api(tags = {"模板消息发送记录-管理后台"})
public class TemplateMsgLogManageController {
    @Autowired
    private TemplateMsgLogService templateMsgLogService;

    @Autowired
    private WxMpService wxMpService;

    /** 列表 */
    @GetMapping("/list")
    @RequiresPermissions("wx:templatemsglog:list")
    @ApiOperation(value = "列表")
    public R list(@CookieValue String appid, @RequestParam Map<String, Object> params) {
        params.put("appid", appid);
        PageUtils page = templateMsgLogService.queryPage(params);

        return R.ok().put("page", page);
    }

    /** 信息 */
    @GetMapping("/info/{logId}")
    @RequiresPermissions("wx:templatemsglog:info")
    @ApiOperation(value = "详情")
    public R info(@CookieValue String appid, @PathVariable("logId") Integer logId) {
        TemplateMsgLog templateMsgLog = templateMsgLogService.getById(logId);

        return R.ok().put("templateMsgLog", templateMsgLog);
    }

    /** 保存 */
    @PostMapping("/save")
    @RequiresPermissions("wx:templatemsglog:save")
    @ApiOperation(value = "保存")
    public R save(@CookieValue String appid, @RequestBody TemplateMsgLog templateMsgLog) {
        templateMsgLogService.save(templateMsgLog);

        return R.ok();
    }

    /** 修改 */
    @PostMapping("/update")
    @RequiresPermissions("wx:templatemsglog:update")
    @ApiOperation(value = "修改")
    public R update(@CookieValue String appid, @RequestBody TemplateMsgLog templateMsgLog) {
        templateMsgLogService.updateById(templateMsgLog);

        return R.ok();
    }

    /** 删除 */
    @PostMapping("/delete")
    @RequiresPermissions("wx:templatemsglog:delete")
    @ApiOperation(value = "删除")
    public R delete(@CookieValue String appid, @RequestBody Integer[] logIds) {
        templateMsgLogService.removeByIds(Arrays.asList(logIds));

        return R.ok();
    }
}
