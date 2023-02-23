package com.taotao.cloud.im.biz.platform.modules.topic.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.topic.domain.ChatTopic;
import com.platform.modules.topic.vo.*;

import java.util.List;

/**
 * <p>
 * 主题 服务层
 * q3z3
 * </p>
 */
public interface ChatTopicService extends BaseService<ChatTopic> {

    /**
     * 发布帖子
     */
    void sendTopic(TopicVo01 topicVo);

    /**
     * 删除帖子
     */
    void delTopic(Long topicId);

    /**
     * 指定人的帖子
     */
    PageInfo userTopic(Long friendId);

    /**
     * 好友的帖子
     */
    PageInfo topicList();

    /**
     * 朋友圈详情
     */
    TopicVo03 topicInfo(Long topicId);

    /**
     * 查询通知列表
     */
    List<TopicVo09> queryNoticeList();

    /**
     * 清空通知列表
     */
    void clearNotice();

    /**
     * 点赞
     */
    void like(Long topicId);

    /**
     * 取消点赞
     */
    void cancelLike(Long topicId);

    /**
     * 回复
     */
    TopicVo06 reply(TopicVo07 topicVo);

    /**
     * 删除回复
     */
    void delReply(Long replyId);
}
