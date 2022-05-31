package com.taotao.cloud.sys.api.vo.setting;

import lombok.Data;

import java.util.List;

/**
 * QQ联合登录设置
 *
 * @author Chopper
 * @since 2020/11/17 7:59 下午
 */
@Data
public class QQConnectSettingVO {

    /**
     * qq联合登陆配置
     */
    List<QQConnectSettingItemVO> qqConnectSettingItemList;

}
