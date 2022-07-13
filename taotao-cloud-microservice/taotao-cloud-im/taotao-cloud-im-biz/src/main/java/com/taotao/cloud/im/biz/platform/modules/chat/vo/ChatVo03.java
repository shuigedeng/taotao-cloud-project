package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.modules.chat.enums.MsgStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class ChatVo03 {

    /**
     * 发送状态
     */
    private MsgStatusEnum status;

    /**
     * 好友详情
     */
    private ChatVo04 userInfo;

    public String getStatusLabel() {
        if (status == null) {
            return null;
        }
        return status.getInfo();
    }

}
