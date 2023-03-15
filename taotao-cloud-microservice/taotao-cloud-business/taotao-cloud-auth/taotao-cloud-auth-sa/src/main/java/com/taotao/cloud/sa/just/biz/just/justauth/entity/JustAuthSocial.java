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
* 第三方用户信息
* </p>
*
* @author GitEgg
* @since 2022-05-23
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_just_auth_social")
@ApiModel(value = "JustAuthSocial对象", description = "第三方用户信息")
public class JustAuthSocial extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "第三方系统的唯一ID	")
    @TableField("uuid")
    private String uuid;

    @ApiModelProperty(value = "第三方用户来源")
    @TableField("source")
    private String source;

    @ApiModelProperty(value = "用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "用户网址")
    @TableField("blog")
    private String blog;

    @ApiModelProperty(value = "所在公司")
    @TableField("company")
    private String company;

    @ApiModelProperty(value = "位置")
    @TableField("location")
    private String location;

    @ApiModelProperty(value = "用户邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "用户备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "性别 -1未知 1男 0女")
    @TableField("gender")
    private Integer gender;

    @ApiModelProperty(value = "用户的授权令牌")
    @TableField("access_token")
    private String accessToken;

    @ApiModelProperty(value = "第三方用户的授权令牌的有效期")
    @TableField("expire_in")
    private Integer expireIn;

    @ApiModelProperty(value = "刷新令牌")
    @TableField("refresh_token")
    private String refreshToken;

    @ApiModelProperty(value = "第三方刷新令牌的有效期")
    @TableField("access_token_expire_in")
    private Integer accessTokenExpireIn;

    @ApiModelProperty(value = "第三方用户的 ID")
    @TableField("uid")
    private String uid;

    @ApiModelProperty(value = "第三方用户的 open id")
    @TableField("open_id")
    private String openId;

    @ApiModelProperty(value = "个别平台的授权信息")
    @TableField("access_code")
    private String accessCode;

    @ApiModelProperty(value = "第三方用户的 union id")
    @TableField("union_id")
    private String unionId;

    @ApiModelProperty(value = "Google Scope")
    @TableField("scope")
    private String scope;

    @ApiModelProperty(value = "Google TokenType")
    @TableField("token_type")
    private String tokenType;

    @ApiModelProperty(value = "Google IdToken")
    @TableField("id_token")
    private String idToken;

    @ApiModelProperty(value = "小米MacAlgorithm")
    @TableField("mac_algorithm")
    private String macAlgorithm;

    @ApiModelProperty(value = "小米Mac_Key")
    @TableField("mac_key")
    private String macKey;

    @ApiModelProperty(value = "企业微信code")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "Twitter OauthToken")
    @TableField("oauth_token")
    private String oauthToken;

    @ApiModelProperty(value = "Twitter OauthTokenSecret")
    @TableField("oauth_token_secret")
    private String oauthTokenSecret;

    @ApiModelProperty(value = "Twitter UserId")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "Twitter ScreenName")
    @TableField("screen_name")
    private String screenName;

    @ApiModelProperty(value = "Twitter OauthCallbackConfirmed")
    @TableField("oauth_callback_confirmed")
    private Boolean oauthCallbackConfirmed;

    @ApiModelProperty(value = "原始用户信息")
    @TableField("rawUserInfo")
    private String rawUserInfo;


}
