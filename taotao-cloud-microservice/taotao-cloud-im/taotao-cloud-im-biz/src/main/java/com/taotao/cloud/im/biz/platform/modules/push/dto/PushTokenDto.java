package com.taotao.cloud.im.biz.platform.modules.push.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 推送Token对象
 */
@Data
@Accessors(chain = true) // 链式调用
public class PushTokenDto {

    /**
     * 过期时间
     */
    private String expireTime;
    /**
     * token
     */
    private String token;
    /**
     * appId
     */
    private String appId;

}
