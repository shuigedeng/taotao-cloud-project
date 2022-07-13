package com.taotao.cloud.im.biz.platform.modules.push.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.platform.modules.push.enums.PushBodyTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 推送消息体
 */
@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class PushBodyVo {

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 消息类型（MSG/BIG/NOTICE）
     */
    private String pushType;

    /**
     * 消息时间
     */
    private String createTime;

    /**
     * 消息数据
     */
    private Object msgContent;

    /**
     * 发送人数据
     */
    private PushFromVo fromInfo;

    /**
     * 接收人数据
     */
    private PushToVo groupInfo;

    public PushBodyVo(String msgId, PushBodyTypeEnum pushType, Object data) {
        this.msgId = msgId;
        this.pushType = pushType.getCode();
        this.msgContent = data;
        this.createTime = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_FORMAT);
        this.groupInfo = new PushToVo();
    }

}
