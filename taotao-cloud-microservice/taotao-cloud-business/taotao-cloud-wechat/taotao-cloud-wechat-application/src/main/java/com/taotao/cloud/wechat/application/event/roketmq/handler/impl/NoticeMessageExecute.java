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

package com.taotao.cloud.sys.application.event.roketmq.handler.impl;

import com.taotao.cloud.stream.message.MemberWithdrawalMessage;
import com.taotao.cloud.sys.application.event.roketmq.handler.MemberPointChangeEventHandler;
import com.taotao.cloud.sys.application.event.roketmq.handler.MemberWithdrawalEventHandler;
import org.springframework.stereotype.Service;

/** 通知类消息实现 */
@Service
public class NoticeMessageExecute implements MemberPointChangeEventHandler,
	MemberWithdrawalEventHandler {

	@Override
	public void memberWithdrawal(MemberWithdrawalMessage memberWithdrawalMessage) {

	}

//    @Autowired
//    private IFeignNoticeMessageApi noticeMessageService;
//
//    @Override
//    public void memberPointChange(MemberPointMessageDTO memberPointMessageDTO) {
//        if (memberPointMessageDTO == null) {
//            return;
//        }
//        // 组织站内信参数
//        NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
//        noticeMessageDTO.setMemberId(memberPointMessageDTO.getMemberId());
//        Map<String, String> params = new HashMap<>(2);
//        if (memberPointMessageDTO.getType().equals(PointTypeEnum.INCREASE.name())) {
//            params.put("expenditure_points", "0");
//            params.put("income_points", memberPointMessageDTO.getPoint().toString());
//        } else {
//            params.put("expenditure_points", memberPointMessageDTO.getPoint().toString());
//            params.put("income_points", "0");
//        }
//        noticeMessageDTO.setParameter(params);
//        noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.POINT_CHANGE);
//        // 发送站内通知信息
//        noticeMessageService.noticeMessage(noticeMessageDTO);
//    }
//
//    @Override
//    public void memberWithdrawal(MemberWithdrawalMessage memberWithdrawalMessage) {
//        NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
//        noticeMessageDTO.setMemberId(memberWithdrawalMessage.getMemberId());
//        // 如果提现状态为申请则发送申请提现站内消息
//        if (memberWithdrawalMessage.getStatus().equals(WithdrawStatusEnum.APPLY.name())) {
//            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_CREATE);
//            Map<String, String> params = new HashMap<>(2);
//            params.put("price", memberWithdrawalMessage.getPrice().toString());
//            noticeMessageDTO.setParameter(params);
//            // 发送提现申请成功消息
//            noticeMessageService.noticeMessage(noticeMessageDTO);
//        }
//        // 如果提现状态为通过则发送审核通过站内消息
//        if (memberWithdrawalMessage.getStatus().equals(WithdrawStatusEnum.VIA_AUDITING.name())) {
//            // 如果提现到余额
//            if (memberWithdrawalMessage.getDestination().equals(MemberWithdrawalDestinationEnum.WALLET.name())) {
//                // 组织参数
//                Map<String, String> params = new HashMap<>(2);
//                params.put("income", memberWithdrawalMessage.getPrice().toString());
//                noticeMessageDTO.setParameter(params);
//                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_SUCCESS);
//                // 发送提现成功消息
//                noticeMessageService.noticeMessage(noticeMessageDTO);
//                params.put("income", memberWithdrawalMessage.getPrice().toString());
//                params.put("expenditure", "0");
//                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_CHANGE);
//                noticeMessageDTO.setParameter(params);
//                // 发送余额变动消息
//                noticeMessageService.noticeMessage(noticeMessageDTO);
//            }
//            // 如果提现到微信
//            if (memberWithdrawalMessage.getDestination().equals(MemberWithdrawalDestinationEnum.WECHAT.name())) {
//                Map<String, String> params = new HashMap<>(2);
//                params.put("income", memberWithdrawalMessage.getPrice().toString());
//                noticeMessageDTO.setParameter(params);
//                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_WEICHAT_SUCCESS);
//                // 发送提现成功消息
//                noticeMessageService.noticeMessage(noticeMessageDTO);
//
//                params.put("income", "0");
//                params.put("expenditure", memberWithdrawalMessage.getPrice().toString());
//                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_CHANGE);
//                noticeMessageDTO.setParameter(params);
//                // 发送余额变动消息
//                noticeMessageService.noticeMessage(noticeMessageDTO);
//            }
//        }
//        // 如果提现状态为拒绝则发送审核拒绝站内消息
//        if (memberWithdrawalMessage.getStatus().equals(WithdrawStatusEnum.FAIL_AUDITING.name())) {
//            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_ERROR);
//            Map<String, String> params = new HashMap<>(2);
//            params.put("price", memberWithdrawalMessage.getPrice().toString());
//            noticeMessageDTO.setParameter(params);
//            // 发送提现申请成功消息
//            noticeMessageService.noticeMessage(noticeMessageDTO);
//        }
//    }
}
