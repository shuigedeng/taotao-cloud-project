package com.taotao.cloud.im.biz.platform.modules.topic.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.topic.domain.ChatTopicReply;
import com.platform.modules.topic.vo.TopicVo06;

import java.util.List;

/**
 * <p>
 * 帖子回复表 服务层
 * q3z3
 * </p>
 */
public interface ChatTopicReplyService extends BaseService<ChatTopicReply> {

    /**
     * 根据帖子id删除
     */
    void delByTopic(Long topicId);

    /**
     * 根据帖子查询
     */
    List<TopicVo06> queryReplyList(Long topicId);

}
