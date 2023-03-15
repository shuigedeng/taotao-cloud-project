package com.taotao.cloud.sa.just.biz.just.justauth.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户第三方登录信息配置表
 * </p>
 *
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "JustAuthSourceDTO对象", description = "租户第三方登录信息配置表")
public class JustAuthSourceDTO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "名称")
	private String sourceName;

	@Schema(description = "登录类型")
	private String sourceType;

	@Schema(description = "自定义Class")
	private String requestClass;

	@Schema(description = "客户端id")
	private String clientId;

	@Schema(description = "客户端Secret")
	private String clientSecret;

	@Schema(description = "回调地址")
	private String redirectUri;

	@Schema(description = "支付宝公钥")
	private String alipayPublicKey;

	@Schema(description = "unionid")
	private Boolean unionId;

	@Schema(description = "Stack Overflow Key")
	private String stackOverflowKey;

	@Schema(description = "企业微信网页应用ID")
	private String agentId;

	@Schema(description = "企业微信用户类型")
	private String userType;

	@Schema(description = "DomainPrefix")
	private String domainPrefix;

	@Schema(description = "忽略校验code state")
	private Boolean ignoreCheckState;

	@Schema(description = "自定义授权scope")
	private String scopes;

	@Schema(description = "设备ID")
	private String deviceId;

	@Schema(description = "客户端操作系统类型")
	private Integer clientOsType;

	@Schema(description = "客户端包名")
	private String packId;

	@Schema(description = "开启PKC模式")
	private Boolean pkce;

	@Schema(description = "Okta授权服务器的 ID")
	private String authServerId;

	@Schema(description = "忽略校验RedirectUri")
	private Boolean ignoreCheckRedirectUri;

	@Schema(description = "Http代理类型")
	private String proxyType;

	@Schema(description = "Http代理Host")
	private String proxyHostName;

	@Schema(description = "Http代理Port")
	private Integer proxyPort;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;
}
