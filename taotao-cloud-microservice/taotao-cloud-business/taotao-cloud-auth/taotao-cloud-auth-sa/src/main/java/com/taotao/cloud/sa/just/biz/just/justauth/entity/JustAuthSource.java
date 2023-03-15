package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_just_auth_source")
@ApiModel(value = "JustAuthSource对象", description = "租户第三方登录信息配置表")
public class JustAuthSource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "第三方登录的名称")
    @TableField("source_name")
    private String sourceName;

    @ApiModelProperty(value = "第三方登录类型：默认default  自定义custom")
    @TableField("source_type")
    private String sourceType;

    @ApiModelProperty(value = "自定义第三方登录的请求Class")
    @TableField("request_class")
    private String requestClass;

    @ApiModelProperty(value = "客户端id：对应各平台的appKey")
    @TableField("client_id")
    private String clientId;

    @ApiModelProperty(value = "客户端Secret：对应各平台的appSecret")
    @TableField("client_secret")
    private String clientSecret;

    @ApiModelProperty(value = "登录成功后的回调地址")
    @TableField("redirect_uri")
    private String redirectUri;

    @ApiModelProperty(value = "支付宝公钥：当选择支付宝登录时，该值可用")
    @TableField("alipay_public_key")
    private String alipayPublicKey;

    @ApiModelProperty(value = "是否需要申请unionid，目前只针对qq登录")
    @TableField("union_id")
    private Boolean unionId;

    @ApiModelProperty(value = "Stack Overflow Key")
    @TableField("stack_overflow_key")
    private String stackOverflowKey;

    @ApiModelProperty(value = "企业微信，授权方的网页应用ID")
    @TableField("agent_id")
    private String agentId;

    @ApiModelProperty(value = "企业微信第三方授权用户类型，member|admin")
    @TableField("user_type")
    private String userType;

    @ApiModelProperty(value = "域名前缀 使用 Coding 登录和 Okta 登录时，需要传该值。")
    @TableField("domain_prefix")
    private String domainPrefix;

    @ApiModelProperty(value = "忽略校验code state 参数，默认不开启。")
    @TableField("ignore_check_state")
    private Boolean ignoreCheckState;

    @ApiModelProperty(value = "支持自定义授权平台的 scope 内容")
    @TableField("scopes")
    private String scopes;

    @ApiModelProperty(value = "设备ID, 设备唯一标识ID")
    @TableField("device_id")
    private String deviceId;

    @ApiModelProperty(value = "喜马拉雅：客户端操作系统类型，1-iOS系统，2-Android系统，3-Web")
    @TableField("client_os_type")
    private Integer clientOsType;

    @ApiModelProperty(value = "喜马拉雅：客户端包名")
    @TableField("pack_id")
    private String packId;

    @ApiModelProperty(value = " 是否开启 PKCE 模式，该配置仅用于支持 PKCE 模式的平台，针对无服务应用，不推荐使用隐式授权，推荐使用 PKCE 模式")
    @TableField("pkce")
    private Boolean pkce;

    @ApiModelProperty(value = "Okta 授权服务器的 ID， 默认为 default。")
    @TableField("auth_server_id")
    private String authServerId;

    @ApiModelProperty(value = "忽略校验 {@code redirectUri} 参数，默认不开启。")
    @TableField("ignore_check_redirect_uri")
    private Boolean ignoreCheckRedirectUri;

    @ApiModelProperty(value = "Http代理类型")
    @TableField("proxy_type")
    private String proxyType;

    @ApiModelProperty(value = "Http代理Host")
    @TableField("proxy_host_name")
    private String proxyHostName;

    @ApiModelProperty(value = "Http代理Port")
    @TableField("proxy_port")
    private Integer proxyPort;

    @ApiModelProperty(value = "'0'禁用，'1' 启用")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;


}
