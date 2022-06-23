package com.taotao.cloud.sys.api.web.vo.setting.connect;

import com.taotao.cloud.sys.api.web.vo.setting.connect.dto.QQConnectSettingItem;
import lombok.Data;

import java.util.List;

/**
 * QQ联合登录设置
 *
 */
@Data
public class QQConnectSetting {

    /**
     * qq联合登陆配置
     */
    List<QQConnectSettingItem> qqConnectSettingItemList;

}
