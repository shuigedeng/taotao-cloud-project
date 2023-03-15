package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户第三方登录信息配置表
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="JustAuthSourceDTO对象", description="租户第三方登录信息配置表")
public class JustAuthSourceDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String sourceName;

    @ApiModelProperty(value = "登录类型")
    private String sourceType;

    @ApiModelProperty(value = "自定义Class")
    private String requestClass;

    @ApiModelProperty(value = "客户端id")
    private String clientId;

    @ApiModelProperty(value = "客户端Secret")
    private String clientSecret;

    @ApiModelProperty(value = "回调地址")
    private String redirectUri;

    @ApiModelProperty(value = "支付宝公钥")
    private String alipayPublicKey;

    @ApiModelProperty(value = "unionid")
    private Boolean unionId;

    @ApiModelProperty(value = "Stack Overflow Key")
    private String stackOverflowKey;

    @ApiModelProperty(value = "企业微信网页应用ID")
    private String agentId;

    @ApiModelProperty(value = "企业微信用户类型")
    private String userType;

    @ApiModelProperty(value = "DomainPrefix")
    private String domainPrefix;

    @ApiModelProperty(value = "忽略校验code state")
    private Boolean ignoreCheckState;

    @ApiModelProperty(value = "自定义授权scope")
    private String scopes;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "客户端操作系统类型")
    private Integer clientOsType;

    @ApiModelProperty(value = "客户端包名")
    private String packId;

    @ApiModelProperty(value = "开启PKC模式")
    private Boolean pkce;

    @ApiModelProperty(value = "Okta授权服务器的 ID")
    private String authServerId;

    @ApiModelProperty(value = "忽略校验RedirectUri")
    private Boolean ignoreCheckRedirectUri;

    @ApiModelProperty(value = "Http代理类型")
    private String proxyType;

    @ApiModelProperty(value = "Http代理Host")
    private String proxyHostName;

    @ApiModelProperty(value = "Http代理Port")
    private Integer proxyPort;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
