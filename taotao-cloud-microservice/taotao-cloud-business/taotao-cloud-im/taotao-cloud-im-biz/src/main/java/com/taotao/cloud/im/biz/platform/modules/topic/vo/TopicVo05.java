package com.taotao.cloud.im.biz.platform.modules.topic.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class TopicVo05 {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String portrait;

}
