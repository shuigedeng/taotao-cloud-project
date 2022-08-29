package com.taotao.cloud.sys.api.model.vo.setting;

import lombok.Data;

import java.util.List;

/**
 * QQ联合登录设置
 *
 */
@Data
public class QQConnectSettingVO {

    /**
     * qq联合登陆配置
     */
    List<QQConnectSettingItemVO> qqConnectSettingItemList;

}
