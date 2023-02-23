package com.taotao.cloud.im.biz.platform.modules.topic.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.topic.domain.ChatTopicLike;
import com.platform.modules.topic.vo.TopicVo05;

import java.util.List;

/**
 * <p>
 * 帖子点赞 服务层
 * q3z3
 * </p>
 */
public interface ChatTopicLikeService extends BaseService<ChatTopicLike> {

    /**
     * 删除帖子
     */
    void delByTopic(Long topicId);

    /**
     * 查询点赞信息
     */
    List<TopicVo05> queryTopicLike(Long topicId);

    /**
     * 查询点赞信息
     */
    ChatTopicLike queryUserLike(Long topicId, Long userId);

}
