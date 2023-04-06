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

package com.taotao.cloud.im.biz.platform.modules.chat.controller;

import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.chat.service.ChatFriendService;
import com.platform.modules.chat.vo.*;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 好友 */
@RestController
@Slf4j
@RequestMapping("/friend")
public class FriendController extends BaseController {

    @Resource
    private ChatFriendService chatFriendService;

    /** 搜索好友 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/findFriend")
    public AjaxResult findFriend(@Validated @RequestBody FriendVo01 friendVo) {
        return AjaxResult.success(chatFriendService.findFriend(friendVo.getParam()));
    }

    /** 好友列表 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/friendList")
    public AjaxResult friendList(@Validated @RequestBody FriendVo08 friendVo) {
        List<FriendVo06> list = chatFriendService.friendList(friendVo.getParam());
        return AjaxResult.success(list);
    }

    /** 好友详情 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/info/{friendId}")
    public AjaxResult getInfo(@PathVariable Long friendId) {
        return AjaxResult.success(chatFriendService.getInfo(friendId));
    }

    /** 设置黑名单 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/black")
    public AjaxResult black(@Validated @RequestBody FriendVo03 friendVo) {
        chatFriendService.setBlack(friendVo);
        return AjaxResult.success();
    }

    /** 删除好友 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/delFriend")
    public AjaxResult delFriend(@Validated @RequestBody FriendVo04 friendVo) {
        chatFriendService.delFriend(friendVo.getUserId());
        return AjaxResult.success();
    }

    /** 设置备注 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/remark")
    public AjaxResult remark(@Validated @RequestBody FriendVo05 friendVo) {
        chatFriendService.setRemark(friendVo);
        return AjaxResult.success();
    }

    /** 设置是否置顶 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/top")
    public AjaxResult top(@Validated @RequestBody FriendVo09 friendVo) {
        chatFriendService.setTop(friendVo);
        return AjaxResult.success();
    }
}
