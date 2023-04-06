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
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.topic.dao.ChatTopicLikeDao;
import com.platform.modules.topic.domain.ChatTopicLike;
import com.platform.modules.topic.service.ChatTopicLikeService;
import com.platform.modules.topic.vo.TopicVo05;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 帖子点赞 服务层实现 q3z3 */
@Service("chatTopicLikeService")
public class ChatTopicLikeServiceImpl extends BaseServiceImpl<ChatTopicLike> implements ChatTopicLikeService {

    @Resource
    private ChatTopicLikeDao chatTopicLikeDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatTopicLikeDao);
    }

    @Override
    public List<ChatTopicLike> queryList(ChatTopicLike t) {
        List<ChatTopicLike> dataList = chatTopicLikeDao.queryList(t);
        return dataList;
    }

    @Override
    public void delByTopic(Long topicId) {
        ChatTopicLike query = new ChatTopicLike().setTopicId(topicId);
        chatTopicLikeDao.delete(new QueryWrapper<>(query));
    }

    @Override
    public List<TopicVo05> queryTopicLike(Long topicId) {
        Long userId = ShiroUtils.getUserId();
        return chatTopicLikeDao.queryTopicLike(topicId, userId);
    }

    @Override
    public ChatTopicLike queryUserLike(Long topicId, Long userId) {
        ChatTopicLike query = new ChatTopicLike().setTopicId(topicId).setUserId(userId);
        return this.queryOne(query);
    }
}
