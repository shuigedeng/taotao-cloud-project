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
* 租户第三方登录信息配置表
* </p>
*
* @author GitEgg
* @since 2022-05-19
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="JustAuthSource对象", description="租户第三方登录信息配置表数据导入模板")
public class JustAuthSourceImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    @ExcelProperty(value = "名称" ,index = 0)
    @ColumnWidth(20)
    private String sourceName;

    @ApiModelProperty(value = "登录类型")
    @ExcelProperty(value = "登录类型" ,index = 1)
    @ColumnWidth(20)
    private String sourceType;

    @ApiModelProperty(value = "自定义Class")
    @ExcelProperty(value = "自定义Class" ,index = 2)
    @ColumnWidth(20)
    private String requestClass;

    @ApiModelProperty(value = "客户端id")
    @ExcelProperty(value = "客户端id" ,index = 3)
    @ColumnWidth(20)
    private String clientId;

    @ApiModelProperty(value = "客户端Secret")
    @ExcelProperty(value = "客户端Secret" ,index = 4)
    @ColumnWidth(20)
    private String clientSecret;

    @ApiModelProperty(value = "回调地址")
    @ExcelProperty(value = "回调地址" ,index = 5)
    @ColumnWidth(20)
    private String redirectUri;

    @ApiModelProperty(value = "支付宝公钥")
    @ExcelProperty(value = "支付宝公钥" ,index = 6)
    @ColumnWidth(20)
    private String alipayPublicKey;

    @ApiModelProperty(value = "unionid")
    @ExcelProperty(value = "unionid" ,index = 7)
    @ColumnWidth(20)
    private Boolean unionId;

    @ApiModelProperty(value = "Stack Overflow Key")
    @ExcelProperty(value = "Stack Overflow Key" ,index = 8)
    @ColumnWidth(20)
    private String stackOverflowKey;

    @ApiModelProperty(value = "企业微信网页应用ID")
    @ExcelProperty(value = "企业微信网页应用ID" ,index = 9)
    @ColumnWidth(20)
    private String agentId;

    @ApiModelProperty(value = "企业微信用户类型")
    @ExcelProperty(value = "企业微信用户类型" ,index = 10)
    @ColumnWidth(20)
    private String userType;

    @ApiModelProperty(value = "DomainPrefix")
    @ExcelProperty(value = "DomainPrefix" ,index = 11)
    @ColumnWidth(20)
    private String domainPrefix;

    @ApiModelProperty(value = "忽略校验code state")
    @ExcelProperty(value = "忽略校验code state" ,index = 12)
    @ColumnWidth(20)
    private Boolean ignoreCheckState;

    @ApiModelProperty(value = "自定义授权scope")
    @ExcelProperty(value = "自定义授权scope" ,index = 13)
    @ColumnWidth(20)
    private String scopes;

    @ApiModelProperty(value = "设备ID")
    @ExcelProperty(value = "设备ID" ,index = 14)
    @ColumnWidth(20)
    private String deviceId;

    @ApiModelProperty(value = "客户端操作系统类型")
    @ExcelProperty(value = "客户端操作系统类型" ,index = 15)
    @ColumnWidth(20)
    private Integer clientOsType;

    @ApiModelProperty(value = "客户端包名")
    @ExcelProperty(value = "客户端包名" ,index = 16)
    @ColumnWidth(20)
    private String packId;

    @ApiModelProperty(value = "开启PKC模式")
    @ExcelProperty(value = "开启PKC模式" ,index = 17)
    @ColumnWidth(20)
    private Boolean pkce;

    @ApiModelProperty(value = "Okta授权服务器的 ID")
    @ExcelProperty(value = "Okta授权服务器的 ID" ,index = 18)
    @ColumnWidth(20)
    private String authServerId;

    @ApiModelProperty(value = "忽略校验RedirectUri")
    @ExcelProperty(value = "忽略校验RedirectUri" ,index = 19)
    @ColumnWidth(20)
    private Boolean ignoreCheckRedirectUri;

    @ApiModelProperty(value = "Http代理类型")
    @ExcelProperty(value = "Http代理类型" ,index = 20)
    @ColumnWidth(20)
    private String proxyType;

    @ApiModelProperty(value = "Http代理Host")
    @ExcelProperty(value = "Http代理Host" ,index = 21)
    @ColumnWidth(20)
    private String proxyHostName;

    @ApiModelProperty(value = "Http代理Port")
    @ExcelProperty(value = "Http代理Port" ,index = 22)
    @ColumnWidth(20)
    private Integer proxyPort;

    @ApiModelProperty(value = "状态")
    @ExcelProperty(value = "状态" ,index = 23)
    @ColumnWidth(20)
    private Integer status;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注" ,index = 24)
    @ColumnWidth(20)
    private String remark;
}
