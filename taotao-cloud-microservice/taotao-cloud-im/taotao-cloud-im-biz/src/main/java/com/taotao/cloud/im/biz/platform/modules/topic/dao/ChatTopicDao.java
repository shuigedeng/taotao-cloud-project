package com.taotao.cloud.im.biz.platform.modules.topic.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.topic.domain.ChatTopic;
import com.platform.modules.topic.vo.TopicVo04;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 主题 数据库访问层
 * q3z3
 * </p>
 */
@Repository
public interface ChatTopicDao extends BaseDao<ChatTopic> {

    /**
     * 查询列表
     *
     * @return
     */
    List<ChatTopic> queryList(ChatTopic chatTopic);

    /**
     * 查询
     */
    List<TopicVo04> topicList(Long userId);

}
