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

import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.entity.WxAccount;
import com.github.niefy.modules.wx.service.WxAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 公众号账号
 *
 * @author niefy
 * @since 2020-06-17 13:56:51
 */
@RestController
@RequestMapping("/manage/wxAccount")
@Api(tags = {"公众号账号-管理后台"})
public class WxAccountManageController {
    @Autowired
    private WxAccountService wxAccountService;

    /** 列表 */
    @GetMapping("/list")
    @RequiresPermissions("wx:wxaccount:list")
    @ApiOperation(value = "列表")
    public R list() {
        List<WxAccount> list = wxAccountService.list();

        return R.ok().put("list", list);
    }

    /** 信息 */
    @GetMapping("/info/{appid}")
    @RequiresPermissions("wx:wxaccount:info")
    @ApiOperation(value = "详情")
    public R info(@PathVariable("id") String appid) {
        WxAccount wxAccount = wxAccountService.getById(appid);

        return R.ok().put("wxAccount", wxAccount);
    }

    /** 保存 */
    @PostMapping("/save")
    @RequiresPermissions("wx:wxaccount:save")
    @ApiOperation(value = "保存")
    public R save(@RequestBody WxAccount wxAccount) {
        wxAccountService.save(wxAccount);

        return R.ok();
    }

    /** 删除 */
    @PostMapping("/delete")
    @RequiresPermissions("wx:wxaccount:delete")
    @ApiOperation(value = "删除")
    public R delete(@RequestBody String[] appids) {
        wxAccountService.removeByIds(Arrays.asList(appids));

        return R.ok();
    }
}
