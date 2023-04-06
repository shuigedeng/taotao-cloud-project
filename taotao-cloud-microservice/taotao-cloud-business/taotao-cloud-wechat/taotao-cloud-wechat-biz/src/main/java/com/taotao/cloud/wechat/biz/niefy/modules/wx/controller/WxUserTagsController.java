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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.controller;

import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.entity.WxUser;
import com.github.niefy.modules.wx.form.WxUserTaggingForm;
import com.github.niefy.modules.wx.service.WxUserService;
import com.github.niefy.modules.wx.service.WxUserTagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 粉丝标签 */
@RestController
@RequestMapping("/wxUserTags")
@RequiredArgsConstructor
@Api(tags = {"粉丝标签"})
public class WxUserTagsController {
    @Autowired
    WxUserTagsService wxUserTagsService;

    @Autowired
    WxUserService wxUserService;

    private final WxMpService wxMpService;

    @GetMapping("/userTags")
    @ApiOperation(value = "当前用户的标签")
    public R userTags(@CookieValue String appid, @CookieValue String openid) {
        if (openid == null) {
            return R.error("none_openid");
        }
        this.wxMpService.switchoverTo(appid);
        WxUser wxUser = wxUserService.getById(openid);
        if (wxUser == null) {
            wxUser = wxUserService.refreshUserInfo(openid, appid);
            if (wxUser == null) {
                return R.error("not_subscribed");
            }
        }
        return R.ok().put(wxUser.getTagidList());
    }

    @PostMapping("/tagging")
    @ApiOperation(value = "给用户绑定标签")
    public R tagging(@CookieValue String appid, @CookieValue String openid, @RequestBody WxUserTaggingForm form) {
        this.wxMpService.switchoverTo(appid);
        try {
            wxUserTagsService.tagging(form.getTagid(), openid);
        } catch (WxErrorException e) {
            WxError error = e.getError();
            if (50005 == error.getErrorCode()) { // 未关注公众号
                return R.error("not_subscribed");
            } else {
                return R.error(error.getErrorMsg());
            }
        }
        return R.ok();
    }

    @PostMapping("/untagging")
    @ApiOperation(value = "解绑标签")
    public R untagging(@CookieValue String appid, @CookieValue String openid, @RequestBody WxUserTaggingForm form)
            throws WxErrorException {
        this.wxMpService.switchoverTo(appid);
        wxUserTagsService.untagging(form.getTagid(), openid);
        return R.ok();
    }
}
