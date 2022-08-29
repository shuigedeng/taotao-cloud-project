package com.taotao.cloud.sys.biz.model.pojo.connect;

import java.util.List;
import lombok.Data;

/**
 * 微信设置
 */
@Data
public class WechatConnectSetting {

    /**
     * 微信联合登陆配置
     */
    List<WechatConnectSettingItem> wechatConnectSettingItems;

}
