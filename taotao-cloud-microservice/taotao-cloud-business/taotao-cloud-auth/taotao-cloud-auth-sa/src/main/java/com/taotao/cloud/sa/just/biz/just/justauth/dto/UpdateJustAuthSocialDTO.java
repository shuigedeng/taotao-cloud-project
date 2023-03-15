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
 * 第三方用户信息
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="JustAuthSocial对象", description="第三方用户信息")
public class UpdateJustAuthSocialDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "第三方ID")
    @NotBlank(message="第三方ID不能为空")
    @Length(min=1,max=100)
    private String uuid;

    @ApiModelProperty(value = "第三方来源")
    @NotBlank(message="第三方来源不能为空")
    @Length(min=1,max=32)
    private String source;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message="用户名不能为空")
    @Length(min=1,max=64)
    private String username;

    @ApiModelProperty(value = "用户昵称")
    @NotBlank(message="用户昵称不能为空")
    @Length(min=1,max=64)
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    @NotBlank(message="用户头像不能为空")
    @Length(min=1,max=500)
    private String avatar;

    @ApiModelProperty(value = "用户网址")
    @NotBlank(message="用户网址不能为空")
    @Length(min=1,max=500)
    private String blog;

    @ApiModelProperty(value = "所在公司")
    @NotBlank(message="所在公司不能为空")
    @Length(min=1,max=255)
    private String company;

    @ApiModelProperty(value = "位置")
    @NotBlank(message="位置不能为空")
    @Length(min=1,max=100)
    private String location;

    @ApiModelProperty(value = "用户邮箱")
    @NotBlank(message="用户邮箱不能为空")
    @Length(min=1,max=100)
    private String email;

    @ApiModelProperty(value = "用户备注")
    @NotBlank(message="用户备注不能为空")
    @Length(min=1,max=500)
    private String remark;

    @ApiModelProperty(value = "性别")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min=1,max=3)
    private Integer gender;

    @ApiModelProperty(value = "授权令牌")
    @NotBlank(message="授权令牌不能为空")
    @Length(min=1,max=500)
    private String accessToken;

    @ApiModelProperty(value = "令牌有效期")
    @NotBlank(message="令牌有效期不能为空")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min=1,max=10)
    private Integer expireIn;

    @ApiModelProperty(value = "刷新令牌")
    @NotBlank(message="刷新令牌不能为空")
    @Length(min=1,max=500)
    private String refreshToken;

    @ApiModelProperty(value = "刷新令牌有效期")
    @NotBlank(message="刷新令牌有效期不能为空")
    @Min(-2147483648L)
    @Max(2147483647L)
    @Length(min=1,max=10)
    private Integer accessTokenExpireIn;

    @ApiModelProperty(value = "第三方用户ID")
    @NotBlank(message="第三方用户ID不能为空")
    @Length(min=1,max=100)
    private String uid;

    @ApiModelProperty(value = "第三方用户OpenId")
    @NotBlank(message="第三方用户OpenId不能为空")
    @Length(min=1,max=100)
    private String openId;

    @ApiModelProperty(value = "AccessCode")
    @NotBlank(message="AccessCode不能为空")
    @Length(min=1,max=255)
    private String accessCode;

    @ApiModelProperty(value = "第三方用户UnionId")
    @NotBlank(message="第三方用户UnionId不能为空")
    @Length(min=1,max=255)
    private String unionId;

    @ApiModelProperty(value = "Google Scope")
    @NotBlank(message="Google Scope不能为空")
    @Length(min=1,max=255)
    private String scope;

    @ApiModelProperty(value = "Google TokenType")
    @NotBlank(message="Google TokenType不能为空")
    @Length(min=1,max=255)
    private String tokenType;

    @ApiModelProperty(value = "Google IdToken")
    @NotBlank(message="Google IdToken不能为空")
    @Length(min=1,max=255)
    private String idToken;

    @ApiModelProperty(value = "小米MacAlgorithm")
    @NotBlank(message="小米MacAlgorithm不能为空")
    @Length(min=1,max=255)
    private String macAlgorithm;

    @ApiModelProperty(value = "小米Mac_Key")
    @NotBlank(message="小米Mac_Key不能为空")
    @Length(min=1,max=255)
    private String macKey;

    @ApiModelProperty(value = "企业微信code")
    @NotBlank(message="企业微信code不能为空")
    @Length(min=1,max=255)
    private String code;

    @ApiModelProperty(value = "Twitter OauthToken")
    @NotBlank(message="Twitter OauthToken不能为空")
    @Length(min=1,max=255)
    private String oauthToken;

    @ApiModelProperty(value = "Twitter OauthTokenSecret")
    @NotBlank(message="Twitter OauthTokenSecret不能为空")
    @Length(min=1,max=255)
    private String oauthTokenSecret;

    @ApiModelProperty(value = "Twitter UserId")
    @NotBlank(message="Twitter UserId不能为空")
    @Length(min=1,max=100)
    private String userId;

    @ApiModelProperty(value = "Twitter ScreenName")
    @NotBlank(message="Twitter ScreenName不能为空")
    @Length(min=1,max=255)
    private String screenName;

    @ApiModelProperty(value = "Twitter OauthCallbackConfirmed")
    @NotBlank(message="Twitter OauthCallbackConfirmed不能为空")
    @Length(min=1,max=1)
    private Boolean oauthCallbackConfirmed;

    @ApiModelProperty(value = "原始用户信息")
    @NotBlank(message="原始用户信息不能为空")
    @Length(min=1,max=65535)
    private String rawUserInfo;
}
