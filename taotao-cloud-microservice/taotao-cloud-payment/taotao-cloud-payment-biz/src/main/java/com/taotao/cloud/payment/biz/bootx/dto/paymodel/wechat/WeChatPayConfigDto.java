package com.taotao.cloud.payment.biz.bootx.dto.paymodel.wechat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
* @author xxm
* @date 2021/3/19
*/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信支付配置")
public class WeChatPayConfigDto extends BaseDto implements Serializable {

    @Schema(description= "名称")
    private String name;
    @Schema(description= "备注")
    private String remark;
    @Schema(description= "微信应用Id")
    private String appId;
    @Schema(description= "微信商户号")
    private String mchId;
    @Schema(description= "微信服务商应用编号")
    private String slAppId;
    @Schema(description= "微信服务商商户号")
    private String slMchId;
    @Schema(description= "同 apiKey 后续版本会舍弃")
    private String partnerKey;
    @Schema(description= "商户平台「API安全」中的 API 密钥")
    private String apiKey;
    @Schema(description= "商户平台「API安全」中的 APIv3 密钥")
    private String apiKey3;
    @Schema(description= "应用域名，回调中会使用此参数")
    private String domain;
    @Schema(description= "服务器异步通知页面路径")
    private String notifyUrl;
    @Schema(description= "页面跳转同步通知页面路径")
    private String returnUrl;
    @Schema(description= "API 证书中的 p12")
    private String certPath;
    @Schema(description= "API 证书中的 key.pem")
    private String keyPemPath;
    @Schema(description= "API 证书中的 cert.pem")
    private String certPemPath;
    @Schema(description= "是否沙箱环境")
    private boolean sandbox;
    @Schema(description= "可用支付方式")
    private String payWays;
    @Schema(description= "可用支付方式")
    private List<String> payWayList;
    @Schema(description= "是否启用")
    private Boolean activity;
    @Schema(description= "状态")
    private Integer state;
}
