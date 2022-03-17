package com.taotao.cloud.member.biz.roketmq.event;


import com.taotao.cloud.member.biz.entity.Member;

/**
 * 会员注册消息
 */
public interface MemberRegisterEvent {

	/**
	 * 会员注册
	 *
	 * @param member 会员
	 */
	void memberRegister(Member member);
}
