package com.taotao.cloud.im.biz.platform.modules.push.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 弹窗消息
 */
@Data
@Accessors(chain = true) // 链式调用
public class PushResultVo {

    /**
     * 推送结果
     */
    private boolean result;

    /**
     * 是否在线
     */
    private boolean online;

}
