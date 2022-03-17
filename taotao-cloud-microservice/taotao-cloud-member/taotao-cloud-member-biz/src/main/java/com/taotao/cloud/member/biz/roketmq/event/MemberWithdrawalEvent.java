package com.taotao.cloud.member.biz.roketmq.event;


/**
 * 会员提现消息
 */
public interface MemberWithdrawalEvent {

	/**
	 * 会员提现
	 *
	 * @param memberWithdrawalMessage 提现对象
	 */
	void memberWithdrawal(MemberWithdrawalMessage memberWithdrawalMessage);
}
