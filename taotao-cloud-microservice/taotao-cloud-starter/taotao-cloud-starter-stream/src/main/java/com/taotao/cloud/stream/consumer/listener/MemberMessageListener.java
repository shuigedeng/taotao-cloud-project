//package com.taotao.cloud.stream.consumer.listener;
//
//import cn.hutool.json.JSONUtil;
//import com.taotao.cloud.order.biz.consumer.event.MemberLoginEvent;
//import com.taotao.cloud.order.biz.consumer.event.MemberPointChangeEvent;
//import com.taotao.cloud.order.biz.consumer.event.MemberRegisterEvent;
//import com.taotao.cloud.order.biz.consumer.event.MemberWithdrawalEvent;
//import com.taotao.cloud.stream.framework.rocketmq.tags.MemberTagsEnum;
//import java.util.List;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 会员消息
// **/
//@Component
//@RocketMQMessageListener(topic = "${lili.data.rocketmq.member-topic}", consumerGroup = "${lili.data.rocketmq.member-group}")
//public class MemberMessageListener implements RocketMQListener<MessageExt> {
//
//    /**
//     * 会员签到
//     */
//    @Autowired
//    private MemberSignService memberSignService;
//    /**
//     * 会员积分变化
//     */
//    @Autowired
//    private List<MemberPointChangeEvent> memberPointChangeEvents;
//    /**
//     * 会员提现
//     */
//    @Autowired
//    private List<MemberWithdrawalEvent> memberWithdrawalEvents;
//    /**
//     * 会员注册
//     */
//    @Autowired
//    private List<MemberRegisterEvent> memberSignEvents;
//
//    /**
//     * 会员注册
//     */
//    @Autowired
//    private List<MemberLoginEvent> memberLoginEvents;
//
//
//    @Override
//    public void onMessage(MessageExt messageExt) {
//        switch (MemberTagsEnum.valueOf(messageExt.getTags())) {
//            //会员注册
//            case MEMBER_REGISTER:
//                for (MemberRegisterEvent memberRegisterEvent : memberSignEvents) {
//                    try {
//                        Member member = JSONUtil.toBean(new String(messageExt.getBody()), Member.class);
//                        memberRegisterEvent.memberRegister(member);
//                    } catch (Exception e) {
//                        log.error("会员{},在{}业务中，状态修改事件执行异常",
//                                new String(messageExt.getBody()),
//                                memberRegisterEvent.getClass().getName(),
//                                e);
//                    }
//                }
//                break;
//
//            case MEMBER_LOGIN:
//
//                for (MemberLoginEvent memberLoginEvent : memberLoginEvents) {
//                    try {
//                        Member member = JSONUtil.toBean(new String(messageExt.getBody()), Member.class);
//                        memberLoginEvent.memberLogin(member);
//                    } catch (Exception e) {
//                        log.error("会员{},在{}业务中，状态修改事件执行异常",
//                                new String(messageExt.getBody()),
//                                memberLoginEvent.getClass().getName(),
//                                e);
//                    }
//                }
//                break;
//            //会员签到
//            case MEMBER_SING:
//                MemberSign memberSign = JSONUtil.toBean(new String(messageExt.getBody()), MemberSign.class);
//                memberSignService.memberSignSendPoint(memberSign.getMemberId(), memberSign.getSignDay());
//                break;
//            //会员积分变动
//            case MEMBER_POINT_CHANGE:
//                for (MemberPointChangeEvent memberPointChangeEvent : memberPointChangeEvents) {
//                    try {
//                        MemberPointMessage memberPointMessage = JSONUtil.toBean(new String(messageExt.getBody()), MemberPointMessage.class);
//                        memberPointChangeEvent.memberPointChange(memberPointMessage);
//                    } catch (Exception e) {
//                        log.error("会员{},在{}业务中，状态修改事件执行异常",
//                                new String(messageExt.getBody()),
//                                memberPointChangeEvent.getClass().getName(),
//                                e);
//                    }
//                }
//                break;
//            //会员提现
//            case MEMBER_WITHDRAWAL:
//                for (MemberWithdrawalEvent memberWithdrawalEvent : memberWithdrawalEvents) {
//                    try {
//                        MemberWithdrawalMessage memberWithdrawalMessage = JSONUtil.toBean(new String(messageExt.getBody()), MemberWithdrawalMessage.class);
//                        memberWithdrawalEvent.memberWithdrawal(memberWithdrawalMessage);
//                    } catch (Exception e) {
//                        log.error("会员{},在{}业务中，提现事件执行异常",
//                                new String(messageExt.getBody()),
//                                memberWithdrawalEvent.getClass().getName(),
//                                e);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }
//}
