package com.taotao.cloud.sa.just.biz.just.justauth.dto;

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
@ApiModel(value="JustAuthSocialDTO对象", description="第三方用户信息")
public class JustAuthSocialDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "第三方ID")
    private String uuid;

    @ApiModelProperty(value = "第三方来源")
    private String source;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户网址")
    private String blog;

    @ApiModelProperty(value = "所在公司")
    private String company;

    @ApiModelProperty(value = "位置")
    private String location;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户备注")
    private String remark;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "授权令牌")
    private String accessToken;

    @ApiModelProperty(value = "令牌有效期")
    private Integer expireIn;

    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;

    @ApiModelProperty(value = "刷新令牌有效期")
    private Integer accessTokenExpireIn;

    @ApiModelProperty(value = "第三方用户ID")
    private String uid;

    @ApiModelProperty(value = "第三方用户OpenId")
    private String openId;

    @ApiModelProperty(value = "AccessCode")
    private String accessCode;

    @ApiModelProperty(value = "第三方用户UnionId")
    private String unionId;

    @ApiModelProperty(value = "Google Scope")
    private String scope;

    @ApiModelProperty(value = "Google TokenType")
    private String tokenType;

    @ApiModelProperty(value = "Google IdToken")
    private String idToken;

    @ApiModelProperty(value = "小米MacAlgorithm")
    private String macAlgorithm;

    @ApiModelProperty(value = "小米Mac_Key")
    private String macKey;

    @ApiModelProperty(value = "企业微信code")
    private String code;

    @ApiModelProperty(value = "Twitter OauthToken")
    private String oauthToken;

    @ApiModelProperty(value = "Twitter OauthTokenSecret")
    private String oauthTokenSecret;

    @ApiModelProperty(value = "Twitter UserId")
    private String userId;

    @ApiModelProperty(value = "Twitter ScreenName")
    private String screenName;

    @ApiModelProperty(value = "Twitter OauthCallbackConfirmed")
    private Boolean oauthCallbackConfirmed;

    @ApiModelProperty(value = "原始用户信息")
    private String rawUserInfo;
}
