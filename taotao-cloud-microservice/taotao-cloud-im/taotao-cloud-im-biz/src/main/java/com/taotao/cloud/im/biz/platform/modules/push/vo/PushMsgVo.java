package com.taotao.cloud.im.biz.platform.modules.push.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 普通消息
 */
@Data
@Accessors(chain = true) // 链式调用
public class PushMsgVo {

    /**
     * 是否置顶
     */
    private String top = YesOrNoEnum.NO.getCode();

    /**
     * 免打扰
     */
    private String disturb = YesOrNoEnum.NO.getCode();

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String content;

}
