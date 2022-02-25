package com.taotao.cloud.stream.consumer.event;

import cn.lili.modules.member.entity.dto.MemberPointMessage;

/**
 * 会员积分改变消息
 */
public interface MemberPointChangeEvent {

    /**
     * 会员积分改变消息
     *
     * @param memberPointMessage 会员积分消息
     */
    void memberPointChange(MemberPointMessage memberPointMessage);
}
