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

import com.platform.common.shiro.ShiroUtils;
import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatFeedbackService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 我的 */
@RestController
@Slf4j
@RequestMapping("/my")
public class MyController extends BaseController {

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatFeedbackService feedbackService;

    /**
     * 修改密码
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editPass")
    public AjaxResult editPass(@Validated @RequestBody MyVo01 myVo) {
        // 执行重置
        chatUserService.editPass(myVo.getPassword(), myVo.getPwd());
        return AjaxResult.successMsg("修改成功");
    }

    /** 退出系统 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/logout")
    public AjaxResult logout() {
        chatUserService.logout();
        return AjaxResult.success();
    }

    /** 获取二维码 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getQrCode")
    public AjaxResult getQrCode() {
        return AjaxResult.success(chatUserService.getQrCode());
    }

    /** 重置二维码 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/resetQrCode")
    public AjaxResult resetQrCode() {
        return AjaxResult.success(chatUserService.resetQrCode());
    }

    /** 获取基本信息 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return AjaxResult.success(chatUserService.getInfo());
    }

    /**
     * 修改头像
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editPortrait")
    public AjaxResult editPortrait(@Validated @RequestBody MyVo02 myVo) {
        // 执行修改
        ChatUser chatUser = new ChatUser().setUserId(ShiroUtils.getUserId()).setPortrait(myVo.getPortrait());
        chatUserService.updateById(chatUser);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 修改昵称
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editNick")
    public AjaxResult editNick(@Validated @RequestBody MyVo03 myVo) {
        ChatUser chatUser = new ChatUser().setUserId(ShiroUtils.getUserId()).setNickName(myVo.getNickName());
        chatUserService.updateById(chatUser);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 修改性别
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editGender")
    public AjaxResult editGender(@Validated @RequestBody MyVo05 myVo) {
        // 执行修改
        ChatUser chatUser = new ChatUser().setUserId(ShiroUtils.getUserId()).setGender(myVo.getGender());
        chatUserService.updateById(chatUser);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 修改微聊号
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editChatNo")
    public AjaxResult editChatNo(@Validated @RequestBody MyVo06 myVo) {
        // 执行修改
        chatUserService.editChatNo(myVo.getChatNo());
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 修改个性签名
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editIntro")
    public AjaxResult editIntro(@Validated @RequestBody MyVo07 myVo) {
        // 执行修改
        ChatUser chatUser = new ChatUser().setUserId(ShiroUtils.getUserId()).setIntro(myVo.getIntro());
        chatUserService.updateById(chatUser);
        return AjaxResult.successMsg("修改成功");
    }

    /**
     * 修改省市
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editCity")
    public AjaxResult editCity(@Validated @RequestBody MyVo08 myVo) {
        // 执行修改
        ChatUser chatUser = new ChatUser()
                .setUserId(ShiroUtils.getUserId())
                .setProvinces(myVo.getProvinces())
                .setCity(myVo.getCity());
        chatUserService.updateById(chatUser);
        return AjaxResult.successMsg("修改成功");
    }

    /** 用户注销 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/deleted")
    public AjaxResult deleted() {
        chatUserService.deleted();
        return AjaxResult.successMsg("注销成功");
    }

    /** 建议反馈 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/feedback")
    public AjaxResult feedback(@Validated @RequestBody MyVo04 myVo) {
        feedbackService.addFeedback(myVo);
        return AjaxResult.success();
    }

    /**
     * 更新cid
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/bindCid/{cid}")
    public AjaxResult bindCid(@PathVariable String cid) {
        // 绑定cid
        chatUserService.bindCid(cid);
        return AjaxResult.success();
    }
}
