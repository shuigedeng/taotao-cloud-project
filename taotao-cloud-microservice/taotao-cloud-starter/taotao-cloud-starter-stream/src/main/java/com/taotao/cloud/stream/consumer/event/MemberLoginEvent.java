package com.taotao.cloud.stream.consumer.event;

import cn.lili.modules.member.entity.dos.Member;

/**
 * 会员登录消息
 */
public interface MemberLoginEvent {

    /**
     * 会员登录
     *
     * @param member 会员
     */
    void memberLogin(Member member);
}
