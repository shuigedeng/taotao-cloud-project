/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 租户第三方登录信息配置表
 *
 * @since 2022-05-19
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@Schema(description = "租户第三方登录信息配置表数据导出")
public class JustAuthSourceExport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "名称")
    @ExcelProperty(value = "名称", index = 0)
    @ColumnWidth(20)
    private String sourceName;

    @Schema(description = "登录类型")
    @ExcelProperty(value = "登录类型", index = 1)
    @ColumnWidth(20)
    private String sourceType;

    @Schema(description = "自定义Class")
    @ExcelProperty(value = "自定义Class", index = 2)
    @ColumnWidth(20)
    private String requestClass;

    @Schema(description = "客户端id")
    @ExcelProperty(value = "客户端id", index = 3)
    @ColumnWidth(20)
    private String clientId;

    @Schema(description = "客户端Secret")
    @ExcelProperty(value = "客户端Secret", index = 4)
    @ColumnWidth(20)
    private String clientSecret;

    @Schema(description = "回调地址")
    @ExcelProperty(value = "回调地址", index = 5)
    @ColumnWidth(20)
    private String redirectUri;

    @Schema(description = "支付宝公钥")
    @ExcelProperty(value = "支付宝公钥", index = 6)
    @ColumnWidth(20)
    private String alipayPublicKey;

    @Schema(description = "unionid")
    @ExcelProperty(value = "unionid", index = 7)
    @ColumnWidth(20)
    private Boolean unionId;

    @Schema(description = "Stack Overflow Key")
    @ExcelProperty(value = "Stack Overflow Key", index = 8)
    @ColumnWidth(20)
    private String stackOverflowKey;

    @Schema(description = "企业微信网页应用ID")
    @ExcelProperty(value = "企业微信网页应用ID", index = 9)
    @ColumnWidth(20)
    private String agentId;

    @Schema(description = "企业微信用户类型")
    @ExcelProperty(value = "企业微信用户类型", index = 10)
    @ColumnWidth(20)
    private String userType;

    @Schema(description = "DomainPrefix")
    @ExcelProperty(value = "DomainPrefix", index = 11)
    @ColumnWidth(20)
    private String domainPrefix;

    @Schema(description = "忽略校验code state")
    @ExcelProperty(value = "忽略校验code state", index = 12)
    @ColumnWidth(20)
    private Boolean ignoreCheckState;

    @Schema(description = "自定义授权scope")
    @ExcelProperty(value = "自定义授权scope", index = 13)
    @ColumnWidth(20)
    private String scopes;

    @Schema(description = "设备ID")
    @ExcelProperty(value = "设备ID", index = 14)
    @ColumnWidth(20)
    private String deviceId;

    @Schema(description = "客户端操作系统类型")
    @ExcelProperty(value = "客户端操作系统类型", index = 15)
    @ColumnWidth(20)
    private Integer clientOsType;

    @Schema(description = "客户端包名")
    @ExcelProperty(value = "客户端包名", index = 16)
    @ColumnWidth(20)
    private String packId;

    @Schema(description = "开启PKC模式")
    @ExcelProperty(value = "开启PKC模式", index = 17)
    @ColumnWidth(20)
    private Boolean pkce;

    @Schema(description = "Okta授权服务器的 ID")
    @ExcelProperty(value = "Okta授权服务器的 ID", index = 18)
    @ColumnWidth(20)
    private String authServerId;

    @Schema(description = "忽略校验RedirectUri")
    @ExcelProperty(value = "忽略校验RedirectUri", index = 19)
    @ColumnWidth(20)
    private Boolean ignoreCheckRedirectUri;

    @Schema(description = "Http代理类型")
    @ExcelProperty(value = "Http代理类型", index = 20)
    @ColumnWidth(20)
    private String proxyType;

    @Schema(description = "Http代理Host")
    @ExcelProperty(value = "Http代理Host", index = 21)
    @ColumnWidth(20)
    private String proxyHostName;

    @Schema(description = "Http代理Port")
    @ExcelProperty(value = "Http代理Port", index = 22)
    @ColumnWidth(20)
    private Integer proxyPort;

    @Schema(description = "状态")
    @ExcelProperty(value = "状态", index = 23)
    @ColumnWidth(20)
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty(value = "备注", index = 24)
    @ColumnWidth(20)
    private String remark;
}
