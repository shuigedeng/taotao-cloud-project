package com.taotao.cloud.im.biz.platform.modules.push.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 推送别名
 */
@Data
@Accessors(chain = true) // 链式调用
public class PushAliasVo {

    /**
     * 客户id
     */
    private String cid;
    /**
     * 别名
     */
    private String alias;

}
