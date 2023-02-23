package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class ChatVo04 {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户id
     */
    private String trtcId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String portrait;

}
