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

package com.taotao.cloud.im.biz.platform.modules.topic.controller;

import com.platform.common.shiro.ShiroUtils;
import com.platform.common.version.ApiVersion;
import com.platform.common.version.VersionEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.topic.service.ChatTopicReplyService;
import com.platform.modules.topic.service.ChatTopicService;
import com.platform.modules.topic.vo.TopicVo01;
import com.platform.modules.topic.vo.TopicVo02;
import com.platform.modules.topic.vo.TopicVo07;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 帖子 */
@RestController
@Slf4j
@RequestMapping("/topic")
public class TopicController extends BaseController {

    @Resource
    private ChatTopicService topicService;

    @Resource
    private ChatTopicReplyService chatTopicReplyService;

    @Resource
    private ChatUserService chatUserService;

    /**
     * 修改封面
     *
     * @return 结果
     */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/editCover")
    public AjaxResult editCover(@Validated @RequestBody TopicVo02 topicVo) {
        // 执行修改
        ChatUser chatUser = new ChatUser().setUserId(ShiroUtils.getUserId()).setCover(topicVo.getCover());
        chatUserService.updateById(chatUser);
        return AjaxResult.successMsg("修改成功");
    }

    /** 发布帖子 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/sendTopic")
    public AjaxResult sendTopic(@Validated @RequestBody TopicVo01 topicVo) {
        topicService.sendTopic(topicVo);
        return AjaxResult.success();
    }

    /** 删除帖子 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/removeTopic/{topicId}")
    public AjaxResult removeTopic(@PathVariable Long topicId) {
        topicService.delTopic(topicId);
        return AjaxResult.success();
    }

    /** 指定人的帖子 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/userTopic/{userId}")
    public TableDataInfo userTopic(@PathVariable Long userId) {
        return getDataTable(topicService.userTopic(userId));
    }

    /** 好友的帖子 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/topicList")
    public TableDataInfo topicList() {
        startPage("create_time desc");
        return getDataTable(topicService.topicList());
    }

    /** 帖子详情 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/topicInfo/{topicId}")
    public AjaxResult topicInfo(@PathVariable Long topicId) {
        return AjaxResult.success(topicService.topicInfo(topicId));
    }

    /** 点赞 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/like/{topicId}")
    public AjaxResult like(@PathVariable Long topicId) {
        topicService.like(topicId);
        return AjaxResult.success();
    }

    /** 取消点赞 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/cancelLike/{topicId}")
    public AjaxResult cancelLike(@PathVariable Long topicId) {
        topicService.cancelLike(topicId);
        return AjaxResult.success();
    }

    /** 回复 */
    @ApiVersion(VersionEnum.V1_0_0)
    @PostMapping("/reply")
    public AjaxResult reply(@Validated @RequestBody TopicVo07 topicVo) {
        return AjaxResult.success(topicService.reply(topicVo));
    }

    /** 删除回复 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/removeReply/{replyId}")
    public AjaxResult removeReply(@PathVariable Long replyId) {
        topicService.delReply(replyId);
        return AjaxResult.success();
    }

    /** 查询通知列表 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/noticeList")
    public AjaxResult noticeList() {
        return AjaxResult.success(topicService.queryNoticeList());
    }

    /** 清空通知列表 */
    @ApiVersion(VersionEnum.V1_0_0)
    @GetMapping("/clearNotice")
    public AjaxResult clearNotice() {
        topicService.clearNotice();
        return AjaxResult.success();
    }
}
