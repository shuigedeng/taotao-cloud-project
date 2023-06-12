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

package com.taotao.cloud.im.biz.platform.modules.push.service.impl;

import com.platform.common.constant.ApiConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.utils.redis.RedisUtils;
import com.platform.modules.push.config.PushConfig;
import com.platform.modules.push.dto.PushMsgDto;
import com.platform.modules.push.dto.PushTokenDto;
import com.platform.modules.push.enums.PushBodyTypeEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.enums.PushNoticeTypeEnum;
import com.platform.modules.push.service.ChatPushService;
import com.platform.modules.push.utils.PushUtils;
import com.platform.modules.push.vo.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 用户推送 服务层 q3z3 */
@Service("chatPushService")
@Slf4j
public class ChatPushServiceImpl implements ChatPushService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private PushConfig pushConfig;

    /** 消息长度 */
    private static final Integer MSG_LENGTH = 2048;

    @Override
    public void setAlias(Long userId, String cid) {
        // 异步注册
        PushTokenDto tokenDto = initPushToken();
        ThreadUtil.execAsync(() -> {
            PushAliasVo aliasVo = new PushAliasVo().setCid(cid).setAlias(NumberUtil.toStr(userId));
            PushUtils.setAlias(tokenDto, aliasVo);
        });
    }

    @Override
    public void delAlias(Long userId, String cid) {
        // 异步注册
        PushTokenDto tokenDto = initPushToken();
        ThreadUtil.execAsync(() -> {
            PushAliasVo aliasVo = new PushAliasVo().setCid(cid).setAlias(NumberUtil.toStr(userId));
            PushUtils.delAlias(tokenDto, aliasVo);
        });
    }

    @Override
    public void pushMsg(PushParamVo from, PushMsgTypeEnum msgType) {
        PushTokenDto pushTokenDto = initPushToken();
        // 异步发送
        ThreadUtil.execAsync(() -> {
            doMsg(from, null, msgType, pushTokenDto);
        });
    }

    @Override
    public void pushMsg(List<PushParamVo> userList, PushMsgTypeEnum msgType) {
        PushTokenDto pushTokenDto = initPushToken();
        // 异步发送
        ThreadUtil.execAsync(() -> {
            userList.forEach(e -> {
                doMsg(e, e, msgType, pushTokenDto);
            });
        });
    }

    @Override
    public void pushMsg(List<PushParamVo> userList, PushParamVo group, PushMsgTypeEnum msgType) {
        PushTokenDto pushTokenDto = initPushToken();
        // 异步发送
        ThreadUtil.execAsync(() -> {
            userList.forEach(e -> {
                doMsg(e, group, msgType, pushTokenDto);
            });
        });
    }

    /** 发送消息 */
    private void doMsg(PushParamVo from, PushParamVo to, PushMsgTypeEnum msgType, PushTokenDto pushTokenDto) {
        Long userId = from.getToId();
        // 组装消息体
        PushMsgVo pushMsgVo = new PushMsgVo().setMsgType(msgType.getCode()).setContent(from.getContent());
        YesOrNoEnum top = from.getTop();
        if (top != null) {
            pushMsgVo.setTop(top.getCode());
        }
        YesOrNoEnum disturb = from.getDisturb();
        if (disturb != null) {
            pushMsgVo.setDisturb(disturb.getCode());
        }
        String msgId = IdUtil.objectId();
        PushBodyVo pushBodyVo = new PushBodyVo(msgId, PushBodyTypeEnum.MSG, pushMsgVo);
        // 发送人
        pushBodyVo.setFromInfo(BeanUtil.toBean(from, PushFromVo.class)
                .setUserType(from.getUserType().getCode()));
        // 接收人
        if (to != null) {
            pushBodyVo.setGroupInfo(BeanUtil.toBean(to, PushToVo.class));
        }
        PushMsgDto pushMsgDto = initTransmission(pushBodyVo);
        // 验证消息长度
        if (StrUtil.length(from.getContent()) > MSG_LENGTH) {
            // 组装消息体
            PushMsgDto pushBigDto = initTransmission(new PushBodyVo(msgId, PushBodyTypeEnum.BIG, new PushBigVo(msgId)));
            // 发送通知
            PushUtils.pushAlias(pushTokenDto, pushBigDto, userId);
            // 存离线消息
            String key = ApiConstant.REDIS_MSG_BIG + msgId;
            redisUtils.set(key, JSONUtil.toJsonStr(pushBodyVo), ApiConstant.REDIS_MSG_TIME, TimeUnit.DAYS);
            return;
        }
        PushResultVo pushResult = PushUtils.pushAlias(pushTokenDto, pushMsgDto, userId);
        if (!pushResult.isResult() || !pushResult.isOnline()) {
            setOffLineMsg(userId, pushMsgDto);
        }
    }

    @Override
    public void pullOffLine(Long userId) {
        // 异步执行
        ThreadUtil.execAsync(() -> {
            String key = makeMsgKey(userId);
            Long size = redisUtils.lLen(key);
            if (size.longValue() == 0) {
                return;
            }
            PushTokenDto pushTokenDto = initPushToken();
            for (int i = 0; i < size; i++) {
                String json = redisUtils.lLeftPop(key);
                PushUtils.pushAlias(pushTokenDto, JSONUtil.toBean(json, PushMsgDto.class), userId);
            }
            redisUtils.delete(key);
        });
    }

    @Override
    public void pushNotice(PushParamVo paramVo, PushNoticeTypeEnum pushNoticeType) {
        this.pushNotice(Arrays.asList(paramVo), pushNoticeType);
    }

    @Override
    public void pushNotice(List<PushParamVo> userList, PushNoticeTypeEnum pushNoticeType) {
        PushTokenDto pushTokenDto = initPushToken();
        // 异步发送
        ThreadUtil.execAsync(() -> {
            userList.forEach(e -> {
                this.doNotice(e.getToId(), e, pushTokenDto, pushNoticeType);
            });
        });
    }

    /** 发送消息 */
    private void doNotice(
            Long userId, PushParamVo paramVo, PushTokenDto pushTokenDto, PushNoticeTypeEnum pushNoticeType) {
        // 组装消息体
        PushNoticeVo pushNoticeVo = new PushNoticeVo();
        switch (pushNoticeType) {
            case TOPIC_RED:
                pushNoticeVo.setTopicRed(Dict.create().set("portrait", paramVo.getPortrait()));
                break;
            case TOPIC_REPLY:
                Long topicCount = redisUtils.increment(ApiConstant.REDIS_TOPIC_NOTICE + userId, 1);
                pushNoticeVo.setTopicReply(
                        Dict.create().set("count", topicCount).set("portrait", paramVo.getPortrait()));
                break;
            case FRIEND_APPLY:
                Long applyCount = redisUtils.increment(ApiConstant.REDIS_FRIEND_NOTICE + userId, 1);
                pushNoticeVo.setFriendApply(Dict.create().set("count", applyCount));
                break;
        }
        PushBodyVo pushBodyVo = new PushBodyVo(IdUtil.objectId(), PushBodyTypeEnum.NOTICE, pushNoticeVo);
        PushMsgDto pushMsgDto = initTransmission(pushBodyVo);
        PushUtils.pushAlias(pushTokenDto, pushMsgDto, userId);
    }

    /** 存储离线消息 */
    private void setOffLineMsg(Long userId, PushMsgDto pushMsgDto) {
        String key = makeMsgKey(userId);
        redisUtils.lRightPush(key, JSONUtil.toJsonStr(pushMsgDto));
        redisUtils.expire(key, ApiConstant.REDIS_MSG_TIME, TimeUnit.DAYS);
    }

    /** 组装消息前缀 */
    private String makeMsgKey(Long userId) {
        return ApiConstant.REDIS_MSG + userId;
    }

    /** 组装透传消息 */
    private PushMsgDto initTransmission(PushBodyVo pushBodyVo) {
        return new PushMsgDto().setTransmission(Dict.create().parseBean(pushBodyVo));
    }

    /** 初始化token */
    private PushTokenDto initPushToken() {
        String key = ApiConstant.REDIS_PUSH_TOKEN + pushConfig.getAppId();
        PushTokenDto tokenDto;
        if (redisUtils.hasKey(key)) {
            String json = redisUtils.get(key);
            tokenDto = JSONUtil.toBean(json, PushTokenDto.class);
        } else {
            tokenDto = PushUtils.createToken(pushConfig);
            redisUtils.set(key, JSONUtil.toJsonStr(tokenDto), 1, TimeUnit.HOURS);
        }
        return tokenDto;
    }
}
