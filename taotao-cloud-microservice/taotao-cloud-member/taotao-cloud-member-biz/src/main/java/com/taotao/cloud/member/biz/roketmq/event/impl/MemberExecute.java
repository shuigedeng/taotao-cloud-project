package com.taotao.cloud.member.biz.roketmq.event.impl;

import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.member.biz.roketmq.event.MemberLoginEvent;
import com.taotao.cloud.member.biz.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员自身业务
 */
@Service
public class MemberExecute implements MemberLoginEvent {

	@Autowired
	private MemberService memberService;

	@Override
	public void memberLogin(Member member) {
		memberService.updateMemberLoginTime(member.getId());
	}
}
