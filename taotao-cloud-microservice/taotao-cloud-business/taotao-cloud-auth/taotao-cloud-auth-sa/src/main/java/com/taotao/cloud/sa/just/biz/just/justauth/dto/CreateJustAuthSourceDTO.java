package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;

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
@ApiModel(value="JustAuthSource对象", description="租户第三方登录信息配置表")
public class CreateJustAuthSourceDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    @NotBlank(message="名称不能为空")
    @Length(min=1,max=32)
    private String sourceName;

    @ApiModelProperty(value = "登录类型")
    @NotBlank(message="登录类型不能为空")
    @Length(min=1,max=32)
    private String sourceType;

    @ApiModelProperty(value = "自定义Class")
    @Length(min=1,max=255)
    private String requestClass;

    @ApiModelProperty(value = "客户端id")
    @NotBlank(message="客户端id不能为空")
    @Length(min=1,max=100)
    private String clientId;

    @ApiModelProperty(value = "客户端Secret")
    @NotBlank(message="客户端Secret不能为空")
    @Length(min=1,max=100)
    private String clientSecret;

    @ApiModelProperty(value = "回调地址")
    @NotBlank(message="回调地址不能为空")
    @Length(min=1,max=255)
    private String redirectUri;

    @ApiModelProperty(value = "支付宝公钥")
    @Length(min=1,max=100)
    private String alipayPublicKey;

    @ApiModelProperty(value = "unionid")
    @Length(min=1,max=1)
    private Boolean unionId;

    @ApiModelProperty(value = "Stack Overflow Key")
    @Length(min=1,max=100)
    private String stackOverflowKey;

    @ApiModelProperty(value = "企业微信网页应用ID")
    @Length(min=1,max=100)
    private String agentId;

    @ApiModelProperty(value = "企业微信用户类型")
    @Length(min=1,max=100)
    private String userType;

    @ApiModelProperty(value = "DomainPrefix")
    @Length(min=1,max=255)
    private String domainPrefix;

    @ApiModelProperty(value = "忽略校验code state")
    @Length(min=1,max=1)
    private Boolean ignoreCheckState;

    @ApiModelProperty(value = "自定义授权scope")
    @Length(min=1,max=100)
    private String scopes;

    @ApiModelProperty(value = "设备ID")
    @Length(min=1,max=100)
    private String deviceId;

    @ApiModelProperty(value = "客户端操作系统类型")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min=1,max=10)
    private Integer clientOsType;

    @ApiModelProperty(value = "客户端包名")
    @Length(min=1,max=100)
    private String packId;

    @ApiModelProperty(value = "开启PKC模式")
    @Length(min=1,max=1)
    private Boolean pkce;

    @ApiModelProperty(value = "Okta授权服务器的 ID")
    @Length(min=1,max=255)
    private String authServerId;

    @ApiModelProperty(value = "忽略校验RedirectUri")
    @Length(min=1,max=1)
    private Boolean ignoreCheckRedirectUri;

    @ApiModelProperty(value = "Http代理类型")
    @Length(min=1,max=10)
    private String proxyType;

    @ApiModelProperty(value = "Http代理Host")
    @Length(min=1,max=100)
    private String proxyHostName;

    @ApiModelProperty(value = "Http代理Port")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min=1,max=10)
    private Integer proxyPort;

    @ApiModelProperty(value = "状态")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min=1,max=3)
    private Integer status;

    @ApiModelProperty(value = "备注")
    @Length(min=1,max=255)
    private String remark;
}
