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

package com.taotao.cloud.im.biz.platform.modules.topic.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.topic.domain.ChatTopic;
import com.platform.modules.topic.vo.*;
import java.util.List;

/** 主题 服务层 q3z3 */
public interface ChatTopicService extends BaseService<ChatTopic> {

    /** 发布帖子 */
    void sendTopic(TopicVo01 topicVo);

    /** 删除帖子 */
    void delTopic(Long topicId);

    /** 指定人的帖子 */
    PageInfo userTopic(Long friendId);

    /** 好友的帖子 */
    PageInfo topicList();

    /** 朋友圈详情 */
    TopicVo03 topicInfo(Long topicId);

    /** 查询通知列表 */
    List<TopicVo09> queryNoticeList();

    /** 清空通知列表 */
    void clearNotice();

    /** 点赞 */
    void like(Long topicId);

    /** 取消点赞 */
    void cancelLike(Long topicId);

    /** 回复 */
    TopicVo06 reply(TopicVo07 topicVo);

    /** 删除回复 */
    void delReply(Long replyId);
}
