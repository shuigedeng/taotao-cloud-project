package com.taotao.cloud.im.biz.platform.modules.push.vo;

import cn.hutool.core.lang.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 推送对象
 */
@Data
@Accessors(chain = true) // 链式调用
public class PushNoticeVo {

    /**
     * 帖子_小红点
     */
    private Dict topicRed = Dict.create();
    /**
     * 帖子_回复
     */
    private Dict topicReply = Dict.create();
    /**
     * 好友_申请
     */
    private Dict friendApply = Dict.create();

}
