package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* <p>
* 第三方用户信息
* </p>
*
* @author GitEgg
* @since 2022-05-23
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="JustAuthSocial对象", description="第三方用户信息数据导入模板")
public class JustAuthSocialImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "第三方ID")
    @ExcelProperty(value = "第三方ID" ,index = 0)
    @ColumnWidth(20)
    private String uuid;

    @ApiModelProperty(value = "第三方来源")
    @ExcelProperty(value = "第三方来源" ,index = 1)
    @ColumnWidth(20)
    private String source;

    @ApiModelProperty(value = "用户名")
    @ExcelProperty(value = "用户名" ,index = 2)
    @ColumnWidth(20)
    private String username;

    @ApiModelProperty(value = "用户昵称")
    @ExcelProperty(value = "用户昵称" ,index = 3)
    @ColumnWidth(20)
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    @ExcelProperty(value = "用户头像" ,index = 4)
    @ColumnWidth(20)
    private String avatar;

    @ApiModelProperty(value = "用户网址")
    @ExcelProperty(value = "用户网址" ,index = 5)
    @ColumnWidth(20)
    private String blog;

    @ApiModelProperty(value = "所在公司")
    @ExcelProperty(value = "所在公司" ,index = 6)
    @ColumnWidth(20)
    private String company;

    @ApiModelProperty(value = "位置")
    @ExcelProperty(value = "位置" ,index = 7)
    @ColumnWidth(20)
    private String location;

    @ApiModelProperty(value = "用户邮箱")
    @ExcelProperty(value = "用户邮箱" ,index = 8)
    @ColumnWidth(20)
    private String email;

    @ApiModelProperty(value = "用户备注")
    @ExcelProperty(value = "用户备注" ,index = 9)
    @ColumnWidth(20)
    private String remark;

    @ApiModelProperty(value = "性别")
    @ExcelProperty(value = "性别" ,index = 10)
    @ColumnWidth(20)
    private Integer gender;

    @ApiModelProperty(value = "授权令牌")
    @ExcelProperty(value = "授权令牌" ,index = 11)
    @ColumnWidth(20)
    private String accessToken;

    @ApiModelProperty(value = "令牌有效期")
    @ExcelProperty(value = "令牌有效期" ,index = 12)
    @ColumnWidth(20)
    private Integer expireIn;

    @ApiModelProperty(value = "刷新令牌")
    @ExcelProperty(value = "刷新令牌" ,index = 13)
    @ColumnWidth(20)
    private String refreshToken;

    @ApiModelProperty(value = "刷新令牌有效期")
    @ExcelProperty(value = "刷新令牌有效期" ,index = 14)
    @ColumnWidth(20)
    private Integer accessTokenExpireIn;

    @ApiModelProperty(value = "第三方用户ID")
    @ExcelProperty(value = "第三方用户ID" ,index = 15)
    @ColumnWidth(20)
    private String uid;

    @ApiModelProperty(value = "第三方用户OpenId")
    @ExcelProperty(value = "第三方用户OpenId" ,index = 16)
    @ColumnWidth(20)
    private String openId;

    @ApiModelProperty(value = "AccessCode")
    @ExcelProperty(value = "AccessCode" ,index = 17)
    @ColumnWidth(20)
    private String accessCode;

    @ApiModelProperty(value = "第三方用户UnionId")
    @ExcelProperty(value = "第三方用户UnionId" ,index = 18)
    @ColumnWidth(20)
    private String unionId;

    @ApiModelProperty(value = "Google Scope")
    @ExcelProperty(value = "Google Scope" ,index = 19)
    @ColumnWidth(20)
    private String scope;

    @ApiModelProperty(value = "Google TokenType")
    @ExcelProperty(value = "Google TokenType" ,index = 20)
    @ColumnWidth(20)
    private String tokenType;

    @ApiModelProperty(value = "Google IdToken")
    @ExcelProperty(value = "Google IdToken" ,index = 21)
    @ColumnWidth(20)
    private String idToken;

    @ApiModelProperty(value = "小米MacAlgorithm")
    @ExcelProperty(value = "小米MacAlgorithm" ,index = 22)
    @ColumnWidth(20)
    private String macAlgorithm;

    @ApiModelProperty(value = "小米Mac_Key")
    @ExcelProperty(value = "小米Mac_Key" ,index = 23)
    @ColumnWidth(20)
    private String macKey;

    @ApiModelProperty(value = "企业微信code")
    @ExcelProperty(value = "企业微信code" ,index = 24)
    @ColumnWidth(20)
    private String code;

    @ApiModelProperty(value = "Twitter OauthToken")
    @ExcelProperty(value = "Twitter OauthToken" ,index = 25)
    @ColumnWidth(20)
    private String oauthToken;

    @ApiModelProperty(value = "Twitter OauthTokenSecret")
    @ExcelProperty(value = "Twitter OauthTokenSecret" ,index = 26)
    @ColumnWidth(20)
    private String oauthTokenSecret;

    @ApiModelProperty(value = "Twitter UserId")
    @ExcelProperty(value = "Twitter UserId" ,index = 27)
    @ColumnWidth(20)
    private String userId;

    @ApiModelProperty(value = "Twitter ScreenName")
    @ExcelProperty(value = "Twitter ScreenName" ,index = 28)
    @ColumnWidth(20)
    private String screenName;

    @ApiModelProperty(value = "Twitter OauthCallbackConfirmed")
    @ExcelProperty(value = "Twitter OauthCallbackConfirmed" ,index = 29)
    @ColumnWidth(20)
    private Boolean oauthCallbackConfirmed;

    @ApiModelProperty(value = "原始用户信息")
    @ExcelProperty(value = "原始用户信息" ,index = 30)
    @ColumnWidth(20)
    private String rawUserInfo;
}
