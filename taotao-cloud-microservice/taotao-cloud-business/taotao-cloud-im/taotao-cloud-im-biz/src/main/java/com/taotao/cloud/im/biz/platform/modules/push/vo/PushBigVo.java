package com.taotao.cloud.im.biz.platform.modules.push.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 大消息
 */
@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class PushBigVo {

    /**
     * 消息内容(消息Id)
     */
    private String content;

    public PushBigVo(String content) {
        this.content = content;
    }

}
