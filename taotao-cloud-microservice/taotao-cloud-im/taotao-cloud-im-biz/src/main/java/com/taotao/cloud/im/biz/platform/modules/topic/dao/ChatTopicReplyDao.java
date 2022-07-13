package com.taotao.cloud.im.biz.platform.modules.topic.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.topic.domain.ChatTopicReply;
import com.platform.modules.topic.vo.TopicVo06;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 帖子回复表 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatTopicReplyDao extends BaseDao<ChatTopicReply> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatTopicReply> queryList(ChatTopicReply chatTopicReply);

    /**
     * 根据帖子查询
     */
    List<TopicVo06> queryReplyList(@Param("userId") Long userId, @Param("topicId") Long topicId);
}
