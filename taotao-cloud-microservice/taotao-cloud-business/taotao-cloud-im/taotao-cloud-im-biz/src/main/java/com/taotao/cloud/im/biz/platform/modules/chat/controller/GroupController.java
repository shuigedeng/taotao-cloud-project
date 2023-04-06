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
import com.platform.modules.chat.service.ChatGroupService;
import com.platform.modules.chat.service.ChatMsgService;
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

/** 群组 */
@RestController
@Slf4j
@RequestMapping("/group")
public class GroupController extends BaseController {

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private ChatMsgService chatMsgService;

    /** 建立群组 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/createGroup")
    public AjaxResult createGroup(@Validated @RequestBody List<Long> list) {
        chatGroupService.createGroup(list);
        return AjaxResult.success();
    }

    /** 获取群详情 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getInfo/{groupId}")
    public AjaxResult getInfo(@PathVariable Long groupId) {
        return AjaxResult.success(chatGroupService.getInfo(groupId));
    }

    /** 邀请进群 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/invitationGroup")
    public AjaxResult invitationGroup(@Validated @RequestBody GroupVo01 groupVo) {
        Long groupId = groupVo.getGroupId();
        List<Long> list = groupVo.getList();
        chatGroupService.invitationGroup(groupId, list);
        return AjaxResult.success();
    }

    /** 踢出群组 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/kickedGroup")
    public AjaxResult kickedGroup(@Validated @RequestBody GroupVo01 groupVo) {
        Long groupId = groupVo.getGroupId();
        List<Long> list = groupVo.getList();
        chatGroupService.kickedGroup(groupId, list);
        return AjaxResult.success();
    }

    /** 修改群名 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editGroupName")
    public AjaxResult editGroupName(@Validated @RequestBody GroupVo02 groupVo) {
        chatGroupService.editGroupName(groupVo);
        return AjaxResult.success();
    }

    /** 获取群组二维码 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getGroupQrCode/{groupId}")
    public AjaxResult getGroupQrCode(@PathVariable Long groupId) {
        return AjaxResult.success(chatGroupService.getGroupQrCode(groupId));
    }

    /** 修改群公告 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editGroupNotice")
    public AjaxResult editGroupNotice(@Validated @RequestBody GroupVo03 groupVo) {
        chatGroupService.editGroupNotice(groupVo);
        return AjaxResult.success();
    }

    /** 是否置顶 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editTop")
    public AjaxResult editTop(@Validated @RequestBody GroupVo04 groupVo) {
        Long groupId = groupVo.getGroupId();
        chatGroupService.editTop(groupId, groupVo.getTop());
        return AjaxResult.success();
    }

    /** 是否免打扰 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editDisturb")
    public AjaxResult editDisturb(@Validated @RequestBody GroupVo05 groupVo) {
        Long groupId = groupVo.getGroupId();
        chatGroupService.editDisturb(groupId, groupVo.getDisturb());
        return AjaxResult.success();
    }

    /** 是否保存群组 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editKeepGroup")
    public AjaxResult editKeepGroup(@Validated @RequestBody GroupVo06 groupVo) {
        Long groupId = groupVo.getGroupId();
        chatGroupService.editKeepGroup(groupId, groupVo.getKeepGroup());
        return AjaxResult.success();
    }

    /** 退出群组 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/logoutGroup/{groupId}")
    public AjaxResult logoutGroup(@PathVariable Long groupId) {
        chatGroupService.logoutGroup(groupId);
        return AjaxResult.success();
    }

    /** 解散群组 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/removeGroup/{groupId}")
    public AjaxResult removeGroup(@PathVariable Long groupId) {
        chatGroupService.removeGroup(groupId);
        return AjaxResult.success();
    }

    /** 扫码查询 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/scanCode/{param}")
    public AjaxResult scanCode(@PathVariable String param) {
        return AjaxResult.success(chatGroupService.scanCode(param));
    }

    /** 加入群组 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/joinGroup/{groupId}")
    public AjaxResult joinGroup(@PathVariable Long groupId) {
        chatGroupService.joinGroup(groupId);
        return AjaxResult.success();
    }

    /** 查询群列表 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/groupList")
    public AjaxResult groupList() {
        return AjaxResult.success(chatGroupService.groupList());
    }

    /** 发送信息 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/sendMsg")
    public AjaxResult sendMsg(@Validated @RequestBody ChatVo02 chatVo) {
        return AjaxResult.success(chatMsgService.sendGroupMsg(chatVo));
    }
}
