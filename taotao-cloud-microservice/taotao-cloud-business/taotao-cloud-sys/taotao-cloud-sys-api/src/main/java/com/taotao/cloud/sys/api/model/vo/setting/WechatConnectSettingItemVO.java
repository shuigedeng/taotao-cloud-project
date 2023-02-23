package com.taotao.cloud.sys.api.model.vo.setting;

import lombok.Data;

/**
 * 微信设置
 *
 */
@Data
public class WechatConnectSettingItemVO {


    /**
     * @See ClientType
     */
    private String clientType;

    private String appId;

    private String appSecret;
}
