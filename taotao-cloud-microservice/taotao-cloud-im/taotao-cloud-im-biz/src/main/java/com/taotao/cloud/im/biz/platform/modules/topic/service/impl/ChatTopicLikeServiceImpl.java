package com.taotao.cloud.im.biz.platform.modules.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.topic.dao.ChatTopicLikeDao;
import com.platform.modules.topic.domain.ChatTopicLike;
import com.platform.modules.topic.service.ChatTopicLikeService;
import com.platform.modules.topic.vo.TopicVo05;
import java.util.List;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 帖子点赞 服务层实现 q3z3
 * </p>
 */
@Service("chatTopicLikeService")
public class ChatTopicLikeServiceImpl extends BaseServiceImpl<ChatTopicLike> implements
		ChatTopicLikeService {

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
