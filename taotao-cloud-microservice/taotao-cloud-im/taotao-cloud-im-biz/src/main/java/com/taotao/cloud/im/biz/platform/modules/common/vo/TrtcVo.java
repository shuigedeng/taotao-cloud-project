package com.taotao.cloud.im.biz.platform.modules.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class TrtcVo {

    /**
     * 用户id
     */
    private String userId;
    /**
     * appId
     */
    private String appId;
    /**
     * 过期时间
     */
    private String expire;
    /**
     * 签名
     */
    private String sign;

}
