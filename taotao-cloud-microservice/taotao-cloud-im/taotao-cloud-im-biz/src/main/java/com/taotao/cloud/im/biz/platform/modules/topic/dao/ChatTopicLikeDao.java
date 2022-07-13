package com.taotao.cloud.im.biz.platform.modules.topic.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.topic.domain.ChatTopicLike;
import com.platform.modules.topic.vo.TopicVo05;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 帖子点赞 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatTopicLikeDao extends BaseDao<ChatTopicLike> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatTopicLike> queryList(ChatTopicLike chatTopicLike);

    /**
     * 查询点赞信息
     */
    List<TopicVo05> queryTopicLike(@Param("topicId") Long topicId, @Param("userId") Long userId);
}
