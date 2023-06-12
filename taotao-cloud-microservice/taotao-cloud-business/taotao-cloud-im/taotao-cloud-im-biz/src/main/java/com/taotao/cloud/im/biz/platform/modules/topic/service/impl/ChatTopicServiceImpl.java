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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.common.constant.ApiConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.redis.RedisUtils;
import com.platform.common.web.page.PageDomain;
import com.platform.common.web.page.TableSupport;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatFriendService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.push.enums.PushNoticeTypeEnum;
import com.platform.modules.push.service.ChatPushService;
import com.platform.modules.push.vo.PushParamVo;
import com.platform.modules.topic.dao.ChatTopicDao;
import com.platform.modules.topic.domain.ChatTopic;
import com.platform.modules.topic.domain.ChatTopicLike;
import com.platform.modules.topic.domain.ChatTopicReply;
import com.platform.modules.topic.enums.TopicNoticeTypeEnum;
import com.platform.modules.topic.enums.TopicReplyTypeEnum;
import com.platform.modules.topic.service.ChatTopicLikeService;
import com.platform.modules.topic.service.ChatTopicReplyService;
import com.platform.modules.topic.service.ChatTopicService;
import com.platform.modules.topic.vo.*;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** 主题 服务层实现 q3z3 */
@Service("chatTopicService")
public class ChatTopicServiceImpl extends BaseServiceImpl<ChatTopic> implements ChatTopicService {

    @Resource
    private ChatTopicDao chatTopicDao;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatFriendService chatFriendService;

    @Resource
    private ChatTopicLikeService chatTopicLikeService;

    @Resource
    private ChatTopicReplyService chatTopicReplyService;

    @Resource
    private ChatPushService chatPushService;

    @Autowired
    private RedisUtils redisUtils;

    private static final Integer DEFAULT_COUNT = 10;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatTopicDao);
    }

    @Override
    public List<ChatTopic> queryList(ChatTopic t) {
        List<ChatTopic> dataList = chatTopicDao.queryList(t);
        return dataList;
    }

    @Override
    public void sendTopic(TopicVo01 topicVo) {
        Long userId = ShiroUtils.getUserId();
        ChatTopic topic = new ChatTopic()
                .setUserId(userId)
                .setTopicType(topicVo.getTopicType())
                .setContent(topicVo.getContent())
                .setLocation(topicVo.getLocation())
                .setCreateTime(DateUtil.date());
        this.add(topic);
        // 好友列表
        List<Long> userList = chatFriendService.queryFriendId(userId);
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        ChatUser chatUser = ChatUser.initUser(chatUserService.getById(userId));
        List<PushParamVo> paramList = new ArrayList<>();
        userList.forEach(e -> {
            paramList.add(new PushParamVo().setToId(e).setPortrait(chatUser.getPortrait()));
        });
        // 推送
        chatPushService.pushNotice(paramList, PushNoticeTypeEnum.TOPIC_RED);
    }

    @Transactional
    @Override
    public void delTopic(Long topicId) {
        ChatTopic topic = this.getById(topicId);
        if (topic == null) {
            return;
        }
        // 删除帖子
        this.deleteById(topicId);
        // 删除点赞
        chatTopicLikeService.delByTopic(topicId);
        // 删除回复
        chatTopicReplyService.delByTopic(topicId);
    }

    @Override
    public PageInfo userTopic(Long friendId) {
        Long userId = ShiroUtils.getUserId();
        // 获取分页对象
        PageDomain pageDomain = TableSupport.getPageDomain();
        // 查询用户
        ChatUser chatUser = ChatUser.initUser(chatUserService.getById(friendId));
        // 昵称
        String nickName = chatUser.getNickName();
        // 判断是否是自己
        if (!userId.equals(friendId)) {
            // 判断是否是好友
            ChatFriend chatFriend = chatFriendService.getFriend(userId, friendId);
            // 好友
            if (chatFriend != null) {
                nickName = chatFriend.getRemark();
            }
            // 非好友
            else {
                pageDomain.setPageNum(1);
                pageDomain.setPageSize(DEFAULT_COUNT);
            }
        }
        // 查询数据
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), "create_time desc");
        List<ChatTopic> topicList = this.queryList(new ChatTopic().setUserId(friendId));
        List<TopicVo03> dataList = new ArrayList<>();
        String finalNickName = nickName;
        topicList.forEach(e -> {
            TopicVo04 topic = BeanUtil.copyProperties(e, TopicVo04.class)
                    .setNickName(finalNickName)
                    .setPortrait(chatUser.getPortrait())
                    .setTopicId(e.getId());
            dataList.add(formatTopic(topic));
        });
        return getPageInfo(dataList, topicList);
    }

    @Override
    public PageInfo topicList() {
        Long userId = ShiroUtils.getUserId();
        // 分页
        List<TopicVo04> topicList = chatTopicDao.topicList(userId);
        List<TopicVo03> dataList = new ArrayList<>();
        topicList.forEach(e -> {
            if (StringUtils.isEmpty(e.getPortrait())) {
                ChatUser chatUser = ChatUser.initUser(null);
                e.setNickName(chatUser.getNickName());
                e.setPortrait(chatUser.getPortrait());
            }
            dataList.add(formatTopic(e));
        });
        return getPageInfo(dataList, topicList);
    }

    @Override
    public TopicVo03 topicInfo(Long topicId) {
        Long userId = ShiroUtils.getUserId();
        ChatTopic topic = this.getById(topicId);
        if (topic == null) {
            throw new BaseException("帖子不存在");
        }
        // 查询用户
        ChatUser chatUser = ChatUser.initUser(chatUserService.getById(topic.getUserId()));
        String nickName = chatUser.getNickName();
        // 判断是否是自己
        if (!userId.equals(topic.getUserId())) {
            // 判断是否是好友
            ChatFriend chatFriend = chatFriendService.getFriend(userId, topic.getUserId());
            // 好友
            if (chatFriend != null) {
                nickName = chatFriend.getRemark();
            }
        }
        TopicVo04 topicVo = BeanUtil.copyProperties(topic, TopicVo04.class)
                .setNickName(nickName)
                .setPortrait(chatUser.getPortrait())
                .setTopicId(topic.getId());
        return formatTopic(topicVo);
    }

    @Override
    public List<TopicVo09> queryNoticeList() {
        Long userId = ShiroUtils.getUserId();
        // 清空通知数
        redisUtils.delete(ApiConstant.REDIS_TOPIC_NOTICE + userId);
        String key = ApiConstant.REDIS_TOPIC_REPLY + userId;
        if (!redisUtils.hasKey(key)) {
            return new ArrayList<>();
        }
        List<String> jsonStr = redisUtils.lAll(key);
        List<TopicVo09> dataList = new ArrayList<>();
        jsonStr.forEach(e -> {
            dataList.add(JSONUtil.toBean(e, TopicVo09.class));
        });
        return dataList;
    }

    @Override
    public void clearNotice() {
        String key = ApiConstant.REDIS_TOPIC_REPLY + ShiroUtils.getUserId();
        redisUtils.delete(key);
    }

    @Override
    public void like(Long topicId) {
        ChatTopic topic = this.getById(topicId);
        if (topic == null) {
            return;
        }
        Long userId = ShiroUtils.getUserId();
        ChatTopicLike topicLike = chatTopicLikeService.queryUserLike(topicId, userId);
        // 点过赞
        if (topicLike != null) {
            // 未点赞状态
            if (!YesOrNoEnum.YES.equals(topicLike.getHasLike())) {
                chatTopicLikeService.updateById(
                        new ChatTopicLike().setId(topicLike.getId()).setHasLike(YesOrNoEnum.YES));
            }
            return;
        }
        // 点赞操作
        chatTopicLikeService.add(
                new ChatTopicLike().setTopicId(topicId).setUserId(userId).setHasLike(YesOrNoEnum.YES));
        // 如果是自己给自己点赞，则不处理
        if (userId.equals(topic.getUserId())) {
            return;
        }
        // 给贴主发送通知
        ChatUser fromUser = ChatUser.initUser(chatUserService.getById(userId));
        PushParamVo paramVo = new PushParamVo().setToId(topic.getUserId()).setPortrait(fromUser.getPortrait());
        chatPushService.pushNotice(paramVo, PushNoticeTypeEnum.TOPIC_REPLY);
        // 通知
        this.addNotice(topic.getUserId(), topic, fromUser, TopicNoticeTypeEnum.LIKE, null);
    }

    @Override
    public void cancelLike(Long topicId) {
        ChatTopic topic = this.getById(topicId);
        if (topic == null) {
            return;
        }
        Long userId = ShiroUtils.getUserId();
        ChatTopicLike topicLike = chatTopicLikeService.queryUserLike(topicId, userId);
        if (topicLike == null) {
            return;
        }
        if (YesOrNoEnum.YES.equals(topicLike.getHasLike())) {
            chatTopicLikeService.updateById(
                    new ChatTopicLike().setId(topicLike.getId()).setHasLike(YesOrNoEnum.NO));
        }
    }

    @Override
    public TopicVo06 reply(TopicVo07 topicVo) {
        // 回复帖子
        if (TopicReplyTypeEnum.TOPIC.equals(topicVo.getReplyType())) {
            return replyTopic(topicVo);
        }
        // 回复回复
        return replyReply(topicVo);
    }

    /** 回复帖子 */
    private TopicVo06 replyTopic(TopicVo07 topicVo) {
        Long topicId = topicVo.getReplyId();
        String content = topicVo.getContent();
        Long userId = ShiroUtils.getUserId();
        // 查询帖子
        ChatTopic topic = this.getById(topicId);
        if (topic == null) {
            return new TopicVo06();
        }
        ChatTopicReply topicReply = new ChatTopicReply()
                .setReplyType(TopicReplyTypeEnum.TOPIC)
                .setContent(content)
                .setTopicId(topic.getId())
                .setUserId(userId)
                .setTargetId(topic.getId())
                .setReplyStatus(YesOrNoEnum.YES)
                .setCreateTime(DateUtil.date());
        chatTopicReplyService.add(topicReply);
        // 给贴主发送通知
        ChatUser fromUser = ChatUser.initUser(chatUserService.getById(userId));
        TopicVo06 result = BeanUtil.toBean(topicReply, TopicVo06.class)
                .setUserId(fromUser.getUserId())
                .setNickName(fromUser.getNickName())
                .setPortrait(fromUser.getPortrait())
                .setCanDeleted(YesOrNoEnum.YES);
        if (!topic.getUserId().equals(userId)) {
            // 帖主推送
            PushParamVo paramVo = new PushParamVo().setToId(topic.getUserId()).setPortrait(fromUser.getPortrait());
            chatPushService.pushNotice(paramVo, PushNoticeTypeEnum.TOPIC_REPLY);
            // 帖主推送
            addNotice(topic.getUserId(), topic, fromUser, TopicNoticeTypeEnum.REPLY, content);
        }
        return result;
    }

    /** 回复回复 */
    private TopicVo06 replyReply(TopicVo07 topicVo) {
        Long replyId = topicVo.getReplyId();
        String content = topicVo.getContent();
        Long userId = ShiroUtils.getUserId();
        // 回复评论
        ChatTopicReply reply = chatTopicReplyService.getById(replyId);
        if (reply == null) {
            return new TopicVo06();
        }
        ChatTopic topic = this.getById(reply.getTopicId());
        if (topic == null) {
            return new TopicVo06();
        }
        ChatTopicReply topicReply = new ChatTopicReply()
                .setReplyType(TopicReplyTypeEnum.USER)
                .setContent(content)
                .setTopicId(reply.getTopicId())
                .setUserId(userId)
                .setTargetId(reply.getUserId())
                .setReplyStatus(YesOrNoEnum.YES)
                .setCreateTime(DateUtil.date());
        chatTopicReplyService.add(topicReply);
        // 给贴主发送通知
        ChatUser fromUser = ChatUser.initUser(chatUserService.getById(userId));
        // 帖主推送
        if (!topic.getUserId().equals(userId)) {
            PushParamVo paramVo = new PushParamVo().setToId(topic.getUserId()).setPortrait(fromUser.getPortrait());
            chatPushService.pushNotice(paramVo, PushNoticeTypeEnum.TOPIC_REPLY);
            // 帖主推送
            addNotice(topic.getUserId(), topic, fromUser, TopicNoticeTypeEnum.REPLY, content);
        }
        // 用户推送
        if (!reply.getUserId().equals(userId)) {
            PushParamVo paramVo = new PushParamVo().setToId(reply.getUserId()).setPortrait(fromUser.getPortrait());
            chatPushService.pushNotice(paramVo, PushNoticeTypeEnum.TOPIC_REPLY);
            // 用户推送
            addNotice(reply.getUserId(), topic, fromUser, TopicNoticeTypeEnum.REPLY, content);
        }
        ChatUser toUser = ChatUser.initUser(chatUserService.getById(reply.getUserId()));
        String nickName = toUser.getNickName();
        ChatFriend chatFriend = chatFriendService.getFriend(userId, reply.getUserId());
        // 好友
        if (chatFriend != null) {
            nickName = chatFriend.getRemark();
        }
        return BeanUtil.toBean(topicReply, TopicVo06.class)
                .setUserId(fromUser.getUserId())
                .setNickName(fromUser.getNickName())
                .setPortrait(fromUser.getPortrait())
                .setCanDeleted(YesOrNoEnum.YES)
                .setToUserId(toUser.getUserId())
                .setToNickName(nickName)
                .setToPortrait(toUser.getPortrait());
    }

    @Override
    public void delReply(Long replyId) {
        Long userId = ShiroUtils.getUserId();
        ChatTopicReply reply = chatTopicReplyService.getById(replyId);
        if (reply == null || YesOrNoEnum.NO.equals(reply.getReplyStatus())) {
            return;
        }
        String errMsg = "不能删除此评论";
        if (TopicReplyTypeEnum.TOPIC.equals(reply.getReplyType())) {
            ChatTopic topic = this.getById(reply.getTopicId());
            if (topic == null) {
                return;
            }
            if (!topic.getUserId().equals(userId)) {
                throw new BaseException(errMsg);
            }
        } else if (!reply.getUserId().equals(userId)) {
            throw new BaseException(errMsg);
        }
        // 帖主/自己发布的帖子
        chatTopicReplyService.updateById(
                new ChatTopicReply().setReplyId(replyId).setReplyStatus(YesOrNoEnum.NO));
    }

    /** 执行添加 */
    private void addNotice(
            Long userId, ChatTopic topic, ChatUser fromUser, TopicNoticeTypeEnum noticeType, String content) {
        ThreadUtil.execAsync(() -> {
            TopicVo09 topicVo = new TopicVo09()
                    .setTopicId(topic.getId())
                    .setTopicType(topic.getTopicType())
                    .setTopicContent(topic.getContent())
                    .setNoticeType(noticeType)
                    .setUserId(fromUser.getUserId())
                    .setNickName(fromUser.getNickName())
                    .setPortrait(fromUser.getPortrait())
                    .setReplyContent(content)
                    .setReplyTime(DateUtil.date());
            redisUtils.lLeftPush(ApiConstant.REDIS_TOPIC_REPLY + userId, JSONUtil.toJsonStr(topicVo));
        });
    }

    /** 格式化帖子 */
    private TopicVo03 formatTopic(TopicVo04 topic) {
        // 查询点赞信息
        List<TopicVo05> likeList = chatTopicLikeService.queryTopicLike(topic.getTopicId());
        // 查询自己点赞
        YesOrNoEnum like = selfLike(likeList);
        // 查询评论信息
        List<TopicVo06> replyList = chatTopicReplyService.queryReplyList(topic.getTopicId());
        TopicVo03 topicVo = new TopicVo03()
                .setTopic(topic)
                .setLikeList(likeList)
                .setLike(like)
                .setReplyList(replyList);
        return topicVo;
    }

    /** 是否点赞 */
    private YesOrNoEnum selfLike(List<TopicVo05> likeList) {
        Long userId = ShiroUtils.getUserId();
        List<Long> userList = likeList.stream().map(TopicVo05::getUserId).toList();
        return userList.contains(userId) ? YesOrNoEnum.YES : YesOrNoEnum.NO;
    }
}
