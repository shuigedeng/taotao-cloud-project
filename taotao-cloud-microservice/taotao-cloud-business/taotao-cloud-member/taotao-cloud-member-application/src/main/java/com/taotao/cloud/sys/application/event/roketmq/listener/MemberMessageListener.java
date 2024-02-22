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

package com.taotao.cloud.sys.application.event.roketmq.listener;

import java.util.List;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 会员消息 */
@Component
@RocketMQMessageListener(
        topic = "${taotao.data.rocketmq.member-topic}",
        consumerGroup = "${taotao.data.rocketmq.member-group}")
public class MemberMessageListener implements RocketMQListener<MessageExt> {

//    /** 会员签到 */
//    @Autowired
//    private IMemberSignService memberSignService;
//    /** 会员积分变化 */
//    @Autowired
//    private List<MemberPointChangeEvent> memberPointChangeEvents;
//    /** 会员提现 */
//    @Autowired
//    private List<MemberWithdrawalEvent> memberWithdrawalEvents;
//    /** 会员注册 */
//    @Autowired
//    private List<MemberRegisterEvent> memberSignEvents;
//
//    /** 会员注册 */
//    @Autowired
//    private List<MemberLoginEvent> memberLoginEvents;

    @Override
    public void onMessage(MessageExt messageExt) {
//        switch (MemberTagsEnum.valueOf(messageExt.getTags())) {
//                // 会员注册
//            case MEMBER_REGISTER -> {
//                for (MemberRegisterEvent memberRegisterEvent : memberSignEvents) {
//                    try {
//                        Member member = JSONUtil.toBean(new String(messageExt.getBody()), Member.class);
//                        memberRegisterEvent.memberRegister(member);
//                    } catch (Exception e) {
//                        LogUtils.error(
//                                "会员{},在{}业务中，状态修改事件执行异常",
//                                new String(messageExt.getBody()),
//                                memberRegisterEvent.getClass().getName(),
//                                e);
//                    }
//                }
//            }
//            case MEMBER_LOGIN -> {
//                for (MemberLoginEvent memberLoginEvent : memberLoginEvents) {
//                    try {
//                        Member member = JSONUtil.toBean(new String(messageExt.getBody()), Member.class);
//                        memberLoginEvent.memberLogin(member);
//                    } catch (Exception e) {
//                        LogUtils.error(
//                                "会员{},在{}业务中，状态修改事件执行异常",
//                                new String(messageExt.getBody()),
//                                memberLoginEvent.getClass().getName(),
//                                e);
//                    }
//                }
//            }
//                // 会员签到
//            case MEMBER_SING -> {
//                MemberSign memberSign = JSONUtil.toBean(new String(messageExt.getBody()), MemberSign.class);
//                memberSignService.memberSignSendPoint(memberSign.getMemberId(), memberSign.getSignDay());
//            }
//                // 会员积分变动
//            case MEMBER_POINT_CHANGE -> {
//                for (MemberPointChangeEvent memberPointChangeEvent : memberPointChangeEvents) {
//                    try {
//                        MemberPointMessageDTO memberPointMessageDTO =
//                                JSONUtil.toBean(new String(messageExt.getBody()), MemberPointMessageDTO.class);
//                        memberPointChangeEvent.memberPointChange(memberPointMessageDTO);
//                    } catch (Exception e) {
//                        LogUtils.error(
//                                "会员{},在{}业务中，状态修改事件执行异常",
//                                new String(messageExt.getBody()),
//                                memberPointChangeEvent.getClass().getName(),
//                                e);
//                    }
//                }
//            }
//                // 会员提现
//            case MEMBER_WITHDRAWAL -> {
//                for (MemberWithdrawalEvent memberWithdrawalEvent : memberWithdrawalEvents) {
//                    try {
//                        MemberWithdrawalMessage memberWithdrawalMessage =
//                                JSONUtil.toBean(new String(messageExt.getBody()), MemberWithdrawalMessage.class);
//                        memberWithdrawalEvent.memberWithdrawal(memberWithdrawalMessage);
//                    } catch (Exception e) {
//                        LogUtils.error(
//                                "会员{},在{}业务中，提现事件执行异常",
//                                new String(messageExt.getBody()),
//                                memberWithdrawalEvent.getClass().getName(),
//                                e);
//                    }
//                }
//            }
//            default -> {}
//        }
    }
}
