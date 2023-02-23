package com.taotao.cloud.member.biz.roketmq.event;

import com.taotao.cloud.member.api.model.dto.MemberPointMessageDTO;

/**
 * 会员积分改变消息
 */
public interface MemberPointChangeEvent {

	/**
	 * 会员积分改变消息
	 *
	 * @param memberPointMessageDTO 会员积分消息
	 */
	void memberPointChange(MemberPointMessageDTO memberPointMessageDTO);
}
