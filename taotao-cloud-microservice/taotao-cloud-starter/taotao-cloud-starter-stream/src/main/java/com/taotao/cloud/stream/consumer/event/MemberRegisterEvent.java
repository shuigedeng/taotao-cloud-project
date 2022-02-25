package com.taotao.cloud.stream.consumer.event;

import cn.lili.modules.member.entity.dos.Member;

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
