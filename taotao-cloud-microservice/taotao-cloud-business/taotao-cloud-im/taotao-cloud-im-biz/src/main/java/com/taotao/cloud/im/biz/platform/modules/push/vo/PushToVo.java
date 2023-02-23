package com.taotao.cloud.im.biz.platform.modules.push.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息接收人
 */
@Data
@Accessors(chain = true) // 链式调用
public class PushToVo {

    /**
     * 接收人id
     */
    private String userId;

    /**
     * 用户头像
     */
    private String portrait;

    /**
     * 用户昵称
     */
    private String nickName;

}
