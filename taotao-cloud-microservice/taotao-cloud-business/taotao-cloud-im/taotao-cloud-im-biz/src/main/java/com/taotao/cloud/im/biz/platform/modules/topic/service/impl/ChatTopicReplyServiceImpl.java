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

package com.taotao.cloud.im.biz.platform.modules.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.topic.dao.ChatTopicReplyDao;
import com.platform.modules.topic.domain.ChatTopicReply;
import com.platform.modules.topic.enums.TopicReplyTypeEnum;
import com.platform.modules.topic.service.ChatTopicReplyService;
import com.platform.modules.topic.vo.TopicVo06;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** 帖子回复表 服务层实现 q3z3 */
@Service("chatTopicReplyService")
public class ChatTopicReplyServiceImpl extends BaseServiceImpl<ChatTopicReply> implements ChatTopicReplyService {

    @Resource
    private ChatTopicReplyDao chatTopicReplyDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatTopicReplyDao);
    }

    @Override
    public List<ChatTopicReply> queryList(ChatTopicReply t) {
        List<ChatTopicReply> dataList = chatTopicReplyDao.queryList(t);
        return dataList;
    }

    @Override
    public void delByTopic(Long topicId) {
        chatTopicReplyDao.delete(new QueryWrapper(new ChatTopicReply().setTopicId(topicId)));
    }

    @Override
    public List<TopicVo06> queryReplyList(Long topicId) {
        Long userId = ShiroUtils.getUserId();
        List<TopicVo06> dataList = chatTopicReplyDao.queryReplyList(userId, topicId);
        dataList.forEach(e -> {
            // 是否可以删除
            e.setCanDeleted(userId.equals(e.getUserId()) ? YesOrNoEnum.YES : YesOrNoEnum.NO);
            // 纠正注销用户
            if (StringUtils.isEmpty(e.getPortrait())) {
                ChatUser chatUser = ChatUser.initUser(null);
                e.setUserId(chatUser.getUserId());
                e.setNickName(chatUser.getNickName());
                e.setPortrait(chatUser.getPortrait());
            }
            // 纠正注销用户
            if (TopicReplyTypeEnum.USER.equals(e.getReplyType()) && StringUtils.isEmpty(e.getToPortrait())) {
                ChatUser chatUser = ChatUser.initUser(null);
                e.setToNickName(chatUser.getNickName());
                e.setToPortrait(chatUser.getPortrait());
            }
        });
        return dataList;
    }
}
