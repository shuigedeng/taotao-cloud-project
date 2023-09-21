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
import com.github.niefy.modules.wx.entity.MsgReplyRule;
import com.github.niefy.modules.wx.service.MsgReplyRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.Map;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 自动回复规则
 *
 * @author niefy
 * @email niefy@qq.com
 * @since 2019-11-12 18:30:15
 */
@RestController
@RequestMapping("/manage/msgReplyRule")
@Api(tags = {"自动回复规则-管理后台"})
public class MsgReplyRuleManageController {
    @Autowired
    private MsgReplyRuleService msgReplyRuleService;

    @Autowired
    private WxMpService wxMpService;

    /** 列表 */
    @GetMapping("/list")
    @RequiresPermissions("wx:msgreplyrule:list")
    @ApiOperation(value = "列表")
    public R list(@CookieValue String appid, @RequestParam Map<String, Object> params) {
        params.put("appid", appid);
        PageUtils page = msgReplyRuleService.queryPage(params);

        return R.ok().put("page", page);
    }

    /** 信息 */
    @GetMapping("/info/{ruleId}")
    @RequiresPermissions("wx:msgreplyrule:info")
    @ApiOperation(value = "详情")
    public R info(@PathVariable("ruleId") Integer ruleId) {
        MsgReplyRule msgReplyRule = msgReplyRuleService.getById(ruleId);

        return R.ok().put("msgReplyRule", msgReplyRule);
    }

    /** 保存 */
    @PostMapping("/save")
    @RequiresPermissions("wx:msgreplyrule:save")
    @ApiOperation(value = "保存")
    public R save(@RequestBody MsgReplyRule msgReplyRule) {
        msgReplyRuleService.save(msgReplyRule);

        return R.ok();
    }

    /** 修改 */
    @PostMapping("/update")
    @RequiresPermissions("wx:msgreplyrule:update")
    @ApiOperation(value = "修改")
    public R update(@RequestBody MsgReplyRule msgReplyRule) {
        msgReplyRuleService.updateById(msgReplyRule);

        return R.ok();
    }

    /** 删除 */
    @PostMapping("/delete")
    @RequiresPermissions("wx:msgreplyrule:delete")
    @ApiOperation(value = "删除")
    public R delete(@RequestBody Integer[] ruleIds) {
        msgReplyRuleService.removeByIds(Arrays.asList(ruleIds));

        return R.ok();
    }
}
