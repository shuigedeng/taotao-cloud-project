package com.taotao.cloud.sys.api.setting.connect;

import com.taotao.cloud.sys.api.setting.connect.dto.WechatConnectSettingItem;
import lombok.Data;

import java.util.List;

/**
 * 微信设置
 *
 */
@Data
public class WechatConnectSetting {


    /**
     * 微信联合登陆配置
     */
    List<WechatConnectSettingItem> wechatConnectSettingItems;

}
