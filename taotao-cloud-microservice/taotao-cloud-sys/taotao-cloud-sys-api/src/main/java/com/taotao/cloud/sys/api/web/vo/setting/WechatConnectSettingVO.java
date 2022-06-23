package com.taotao.cloud.sys.api.web.vo.setting;

import lombok.Data;

import java.util.List;

/**
 * 微信设置
 *
 */
@Data
public class WechatConnectSettingVO {


    /**
     * 微信联合登陆配置
     */
    List<WechatConnectSettingItemVO> wechatConnectSettingItemVOS;

}
