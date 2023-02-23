package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.entity;

import cn.bootx.common.core.function.EntityBaseFunction;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.payment.core.paymodel.wechat.convert.WeChatConvert;
import cn.bootx.payment.dto.paymodel.wechat.WeChatPayConfigDto;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信支付
 * @author xxm
 * @date 2021/3/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_wechat_pay_config")
public class WeChatPayConfig extends MpBaseEntity implements EntityBaseFunction<WeChatPayConfigDto> {

    /** 名称 */
    private String name;
    /** 备注 */
    private String remark;
    /** 微信应用appId */
    private String appId;
    /** 微信商户号 */
    private String mchId;
    /**
     * 认证方式
     * @see cn.bootx.payment.code.paymodel.WeChatPayCode
     */
    private Integer authType;
    /** 微信服务商应用编号 */
    private String slAppId;
    /** 微信服务商商户号 */
    private String slMchId;
    /** 同 apiKey 后续版本会舍弃 */
    private String partnerKey;
    /** 商户平台「API安全」中的 API 密钥 */
    private String apiKey;
    /** 商户平台「API安全」中的 APIv3 密钥 */
    private String apiKey3;
    /** 应用域名，回调中会使用此参数 */
    private String domain;
    /** 服务器异步通知页面路径 通知url必须为直接可访问的url，不能携带参数。公网域名必须为https  */
    private String notifyUrl;
    /** 页面跳转同步通知页面路径 */
    private String returnUrl;
    /**
     * API 证书中的 p12
     */
    private String certPath;
    /**
     * API 证书中的 key.pem
     */
    private String keyPemPath;
    /**
     * API 证书中的 cert.pem
     */
    private String certPemPath;
    /** 是否沙箱环境 */
    private boolean sandbox;
    /** 可用支付方式 */
    private String payWays;
    /** 是否启用 */
    private Boolean activity;
    /** 状态 */
    private Integer state;

    @Override
    public WeChatPayConfigDto toDto() {
        WeChatPayConfigDto convert =  WeChatConvert.CONVERT.convert(this);
        if (StrUtil.isNotBlank(this.getPayWays())){
            convert.setPayWayList(StrUtil.split(this.getPayWays(),','));
        }
        return convert;
    }

    public static WeChatPayConfig init(WeChatPayConfigDto dto){
        WeChatPayConfig convert = WeChatConvert.CONVERT.convert(dto);
        if (CollUtil.isNotEmpty(dto.getPayWayList())){
            convert.setPayWays(String.join(",", dto.getPayWayList()));
        }
        return convert;
    }
}
