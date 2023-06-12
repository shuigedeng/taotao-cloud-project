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

package com.taotao.cloud.im.biz.platform.modules.chat.service.impl;

import com.platform.common.constant.ApiConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatMsgDao;
import com.platform.modules.chat.domain.*;
import com.platform.modules.chat.enums.FriendTypeEnum;
import com.platform.modules.chat.enums.MsgStatusEnum;
import com.platform.modules.chat.service.*;
import com.platform.modules.chat.vo.ChatVo01;
import com.platform.modules.chat.vo.ChatVo02;
import com.platform.modules.chat.vo.ChatVo03;
import com.platform.modules.chat.vo.ChatVo04;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.enums.PushTalkEnum;
import com.platform.modules.push.service.ChatPushService;
import com.platform.modules.push.vo.PushBodyVo;
import com.platform.modules.push.vo.PushParamVo;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 聊天消息 服务层实现 q3z3 */
@Service("chatMsgService")
public class ChatMsgServiceImpl extends BaseServiceImpl<ChatMsg> implements ChatMsgService {

    @Resource
    private ChatMsgDao chatMsgDao;

    @Resource
    private ChatFriendService friendService;

    @Resource
    private ChatGroupService groupService;

    @Resource
    private ChatGroupInfoService groupInfoService;

    @Resource
    private ChatPushService chatPushService;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatTalkService chatTalkService;

    @Resource
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatMsgDao);
    }

    @Override
    public List<ChatMsg> queryList(ChatMsg t) {
        List<ChatMsg> dataList = chatMsgDao.queryList(t);
        return dataList;
    }

    @Transactional
    @Override
    public ChatVo03 sendFriendMsg(ChatVo01 chatVo) {
        Long userId = ShiroUtils.getUserId();
        Long friendId = chatVo.getUserId();
        PushMsgTypeEnum msgType = chatVo.getMsgType();
        ChatVo04 chatVo04 = null;
        // 系统好友
        PushParamVo paramVo = chatTalkService.talk(chatVo.getUserId(), chatVo.getContent());
        if (paramVo != null) {
            friendId = userId;
            msgType = PushMsgTypeEnum.TEXT;
        }
        // 自己给自己发消息
        else if (userId.equals(friendId)) {
            paramVo = ChatUser.initParam(chatUserService.getById(userId))
                    .setUserType(FriendTypeEnum.SELF)
                    .setContent(chatVo.getContent());
        }
        // 发送给好友的消息
        else {
            // 校验好友
            ChatFriend friend1 = friendService.getFriend(userId, friendId);
            if (friend1 == null) {
                return doResult(MsgStatusEnum.FRIEND_TO);
            }
            ChatFriend friend2 = friendService.getFriend(friendId, userId);
            if (friend2 == null) {
                return doResult(MsgStatusEnum.FRIEND_FROM);
            }
            if (YesOrNoEnum.YES.equals(friend2.getBlack())) {
                return doResult(MsgStatusEnum.FRIEND_BLACK);
            }
            ChatUser toUser = chatUserService.getById(friendId);
            if (toUser == null) {
                return doResult(MsgStatusEnum.FRIEND_DELETED);
            }
            paramVo = ChatUser.initParam(chatUserService.getById(userId))
                    .setNickName(friend2.getRemark())
                    .setTop(friend2.getTop())
                    .setContent(chatVo.getContent());
            if (PushMsgTypeEnum.TRTC_VOICE_START.equals(msgType) || PushMsgTypeEnum.TRTC_VIDEO_START.equals(msgType)) {
                chatVo04 = new ChatVo04()
                        .setUserId(friendId)
                        .setTrtcId(ApiConstant.REDIS_TRTC_USER + friendId)
                        .setPortrait(toUser.getPortrait())
                        .setNickName(friend1.getRemark());
            }
        }
        // 保存数据
        ChatMsg chatMsg = new ChatMsg()
                .setFromId(userId)
                .setToId(friendId)
                .setMsgType(msgType)
                .setTalkType(PushTalkEnum.SINGLE)
                .setContent(paramVo.getContent())
                .setCreateTime(DateUtil.date());
        this.add(chatMsg);
        // 推送
        chatPushService.pushMsg(paramVo.setToId(friendId), msgType);
        return doResult(MsgStatusEnum.NORMAL).setUserInfo(chatVo04);
    }

    /** 返回发送结果 */
    private ChatVo03 doResult(MsgStatusEnum status) {
        return new ChatVo03().setStatus(status);
    }

    @Override
    public ChatVo03 sendGroupMsg(ChatVo02 chatVo) {
        String content = chatVo.getContent();
        Long fromId = ShiroUtils.getUserId();
        Long groupId = chatVo.getGroupId();
        // 查询群组
        ChatGroup group = groupService.getById(groupId);
        if (group == null) {
            return doResult(MsgStatusEnum.GROUP_NOT_EXIST);
        }
        // 查询群明细
        ChatGroupInfo groupInfo = groupInfoService.getGroupInfo(groupId, fromId, YesOrNoEnum.NO);
        if (groupInfo == null || YesOrNoEnum.YES.equals(groupInfo.getKicked())) {
            return doResult(MsgStatusEnum.GROUP_INFO_NOT_EXIST);
        }
        // 保存数据
        ChatMsg chatMsg = new ChatMsg()
                .setFromId(fromId)
                .setToId(groupId)
                .setMsgType(chatVo.getMsgType())
                .setTalkType(PushTalkEnum.GROUP)
                .setContent(content)
                .setCreateTime(DateUtil.date());
        this.add(chatMsg);
        // 查询群列表
        List<PushParamVo> userList = groupService.queryFriendPushFrom(groupId, content);
        // 群信息
        PushParamVo groupUser = new PushParamVo()
                .setUserId(group.getId())
                .setNickName(group.getName())
                .setPortrait(group.getPortrait());
        // 推送
        chatPushService.pushMsg(userList, groupUser, chatVo.getMsgType());
        return doResult(MsgStatusEnum.NORMAL);
    }

    @Override
    public PushBodyVo getBigMsg(String msgId) {
        String key = ApiConstant.REDIS_MSG_BIG + msgId;
        if (!redisUtils.hasKey(key)) {
            return null;
        }
        String jsonStr = redisUtils.get(key);
        return JSONUtil.toBean(jsonStr, PushBodyVo.class);
    }
}
