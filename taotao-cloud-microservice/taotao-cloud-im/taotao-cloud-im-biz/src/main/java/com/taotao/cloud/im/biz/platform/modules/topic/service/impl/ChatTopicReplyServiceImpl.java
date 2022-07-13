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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 帖子回复表 服务层实现
 * q3z3
 * </p>
 */
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
