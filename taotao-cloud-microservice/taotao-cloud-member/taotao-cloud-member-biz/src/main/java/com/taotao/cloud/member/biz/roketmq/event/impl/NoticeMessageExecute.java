package com.taotao.cloud.member.biz.roketmq.event.impl;

import com.taotao.cloud.member.api.dto.MemberPointMessage;
import com.taotao.cloud.member.api.enums.MemberWithdrawalDestinationEnum;
import com.taotao.cloud.member.api.enums.PointTypeEnum;
import com.taotao.cloud.member.api.enums.WithdrawStatusEnum;
import com.taotao.cloud.member.biz.roketmq.event.MemberPointChangeEvent;
import com.taotao.cloud.member.biz.roketmq.event.MemberWithdrawalEvent;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 通知类消息实现
 *
 **/
@Service
public class NoticeMessageExecute implements MemberPointChangeEvent, MemberWithdrawalEvent {

    @Autowired
    private NoticeMessageService noticeMessageService;

    @Override
    public void memberPointChange(MemberPointMessage memberPointMessage) {
        if (memberPointMessage == null) {
            return;
        }
        //组织站内信参数
        NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
        noticeMessageDTO.setMemberId(memberPointMessage.getMemberId());
        Map<String, String> params = new HashMap<>(2);
        if (memberPointMessage.getType().equals(PointTypeEnum.INCREASE.name())) {
            params.put("expenditure_points", "0");
            params.put("income_points", memberPointMessage.getPoint().toString());
        } else {
            params.put("expenditure_points", memberPointMessage.getPoint().toString());
            params.put("income_points", "0");
        }
        noticeMessageDTO.setParameter(params);
        noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.POINT_CHANGE);
        //发送站内通知信息
        noticeMessageService.noticeMessage(noticeMessageDTO);
    }


    @Override
    public void memberWithdrawal(MemberWithdrawalMessage memberWithdrawalMessage) {
        NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
        noticeMessageDTO.setMemberId(memberWithdrawalMessage.getMemberId());
        //如果提现状态为申请则发送申请提现站内消息
        if (memberWithdrawalMessage.getStatus().equals(WithdrawStatusEnum.APPLY.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_CREATE);
            Map<String, String> params = new HashMap<>(2);
            params.put("price", memberWithdrawalMessage.getPrice().toString());
            noticeMessageDTO.setParameter(params);
            //发送提现申请成功消息
            noticeMessageService.noticeMessage(noticeMessageDTO);
        }
        //如果提现状态为通过则发送审核通过站内消息
        if (memberWithdrawalMessage.getStatus().equals(WithdrawStatusEnum.VIA_AUDITING.name())) {
            //如果提现到余额
            if (memberWithdrawalMessage.getDestination().equals(MemberWithdrawalDestinationEnum.WALLET.name())) {
                //组织参数
                Map<String, String> params = new HashMap<>(2);
                params.put("income", memberWithdrawalMessage.getPrice().toString());
                noticeMessageDTO.setParameter(params);
                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_SUCCESS);
                //发送提现成功消息
                noticeMessageService.noticeMessage(noticeMessageDTO);
                params.put("income", memberWithdrawalMessage.getPrice().toString());
                params.put("expenditure", "0");
                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_CHANGE);
                noticeMessageDTO.setParameter(params);
                //发送余额变动消息
                noticeMessageService.noticeMessage(noticeMessageDTO);
            }
            //如果提现到微信
            if (memberWithdrawalMessage.getDestination().equals(MemberWithdrawalDestinationEnum.WECHAT.name())) {
                Map<String, String> params = new HashMap<>(2);
                params.put("income", memberWithdrawalMessage.getPrice().toString());
                noticeMessageDTO.setParameter(params);
                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_WEICHAT_SUCCESS);
                //发送提现成功消息
                noticeMessageService.noticeMessage(noticeMessageDTO);

                params.put("income", "0");
                params.put("expenditure", memberWithdrawalMessage.getPrice().toString());
                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_CHANGE);
                noticeMessageDTO.setParameter(params);
                //发送余额变动消息
                noticeMessageService.noticeMessage(noticeMessageDTO);
            }
        }
        //如果提现状态为拒绝则发送审核拒绝站内消息
        if (memberWithdrawalMessage.getStatus().equals(WithdrawStatusEnum.FAIL_AUDITING.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_ERROR);
            Map<String, String> params = new HashMap<>(2);
            params.put("price", memberWithdrawalMessage.getPrice().toString());
            noticeMessageDTO.setParameter(params);
            //发送提现申请成功消息
            noticeMessageService.noticeMessage(noticeMessageDTO);
        }


    }
}
