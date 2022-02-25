package com.taotao.cloud.stream.consumer.event;

import cn.lili.modules.member.entity.dos.MemberEvaluation;

/**
 * 订单状态改变事件
 */
public interface GoodsCommentCompleteEvent {

    /**
     * 商品评价
     * @param memberEvaluation 会员评价
     */
    void goodsComment(MemberEvaluation memberEvaluation);
}
