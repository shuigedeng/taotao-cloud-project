/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;
import me.zhyd.oauth.config.AuthDefaultSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等
 */
@Schema(title = "账号类型")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AccountType {

    /**
     * 登录类型
     */
    INSTITUTION("INSTITUTION", "", "机构人员"),
    SMS("SMS", AccountType.PHONE_NUMBER_HANDLER, "手机验证码"),
    WXAPP("WXAPP", AccountType.WECHAT_MINI_APP_HANDLER, "微信小程序"),
    QQ(AuthDefaultSource.QQ.name(), AccountType.JUST_AUTH_HANDLER, "QQ"),
    WEIBO(AuthDefaultSource.WEIBO.name(), AccountType.JUST_AUTH_HANDLER, "微博"),
    BAIDU(AuthDefaultSource.BAIDU.name(), AccountType.JUST_AUTH_HANDLER, "百度"),
    WECHAT_OPEN(AuthDefaultSource.WECHAT_OPEN.name(), AccountType.JUST_AUTH_HANDLER, "微信开放平台"),
    WECHAT_MP(AuthDefaultSource.WECHAT_MP.name(), AccountType.JUST_AUTH_HANDLER, "微信公众号"),
    WECHAT_ENTERPRISE(AuthDefaultSource.WECHAT_ENTERPRISE.name(), AccountType.JUST_AUTH_HANDLER, "企业微信二维码"),
    WECHAT_ENTERPRISE_WEB(AuthDefaultSource.WECHAT_ENTERPRISE_WEB.name(), AccountType.JUST_AUTH_HANDLER, "企业微信网页"),
    DINGTALK(AuthDefaultSource.DINGTALK.name(), AccountType.JUST_AUTH_HANDLER, "钉钉"),
    DINGTALK_ACCOUNT(AuthDefaultSource.DINGTALK_ACCOUNT.name(), AccountType.JUST_AUTH_HANDLER, "钉钉账号"),
    ALIYUN(AuthDefaultSource.ALIYUN.name(), AccountType.JUST_AUTH_HANDLER, "阿里云"),
    TAOBAO(AuthDefaultSource.TAOBAO.name(), AccountType.JUST_AUTH_HANDLER, "淘宝"),
    ALIPAY(AuthDefaultSource.ALIPAY.name(), AccountType.JUST_AUTH_HANDLER, "支付宝"),
    TEAMBITION(AuthDefaultSource.TEAMBITION.name(), AccountType.JUST_AUTH_HANDLER, "Teambition"),
    HUAWEI(AuthDefaultSource.HUAWEI.name(), AccountType.JUST_AUTH_HANDLER, "华为"),
    FEISHU(AuthDefaultSource.FEISHU.name(), AccountType.JUST_AUTH_HANDLER, "飞书"),
    JD(AuthDefaultSource.JD.name(), AccountType.JUST_AUTH_HANDLER, "京东"),
    DOUYIN(AuthDefaultSource.DOUYIN.name(), AccountType.JUST_AUTH_HANDLER, "抖音"),
    TOUTIAO(AuthDefaultSource.TOUTIAO.name(), AccountType.JUST_AUTH_HANDLER, "今日头条"),
    MI(AuthDefaultSource.MI.name(), AccountType.JUST_AUTH_HANDLER, "小米"),
    RENREN(AuthDefaultSource.RENREN.name(), AccountType.JUST_AUTH_HANDLER, "人人"),
    MEITUAN(AuthDefaultSource.MEITUAN.name(), AccountType.JUST_AUTH_HANDLER, "美团"),
    ELEME(AuthDefaultSource.ELEME.name(), AccountType.JUST_AUTH_HANDLER, "饿了么"),
    KUJIALE(AuthDefaultSource.KUJIALE.name(), AccountType.JUST_AUTH_HANDLER, "酷家乐"),
    XMLY(AuthDefaultSource.XMLY.name(), AccountType.JUST_AUTH_HANDLER, "喜马拉雅"),
    GITEE(AuthDefaultSource.GITEE.name(), AccountType.JUST_AUTH_HANDLER, "码云"),
    OSCHINA(AuthDefaultSource.OSCHINA.name(), AccountType.JUST_AUTH_HANDLER, "开源中国"),
    CSDN(AuthDefaultSource.CSDN.name(), AccountType.JUST_AUTH_HANDLER, "CSDN"),
    GITHUB(AuthDefaultSource.GITHUB.name(), AccountType.JUST_AUTH_HANDLER, "Github"),
    GITLAB(AuthDefaultSource.GITLAB.name(), AccountType.JUST_AUTH_HANDLER, "Gitlab"),
    STACK_OVERFLOW(AuthDefaultSource.STACK_OVERFLOW.name(), AccountType.JUST_AUTH_HANDLER, "Stackoverflow"),
    CODING(AuthDefaultSource.CODING.name(), AccountType.JUST_AUTH_HANDLER, "Coding"),
    GOOGLE(AuthDefaultSource.GOOGLE.name(), AccountType.JUST_AUTH_HANDLER, "谷歌"),
    MICROSOFT(AuthDefaultSource.MICROSOFT.name(), AccountType.JUST_AUTH_HANDLER, "微软"),
    FACEBOOK(AuthDefaultSource.FACEBOOK.name(), AccountType.JUST_AUTH_HANDLER, "脸书"),
    LINKEDIN(AuthDefaultSource.LINKEDIN.name(), AccountType.JUST_AUTH_HANDLER, "领英"),
    TWITTER(AuthDefaultSource.TWITTER.name(), AccountType.JUST_AUTH_HANDLER, "推特"),
    AMAZON(AuthDefaultSource.AMAZON.name(), AccountType.JUST_AUTH_HANDLER, "亚马逊"),
    SLACK(AuthDefaultSource.SLACK.name(), AccountType.JUST_AUTH_HANDLER, "Slack"),
    LINE(AuthDefaultSource.LINE.name(), AccountType.JUST_AUTH_HANDLER, "Line"),
    OKTA(AuthDefaultSource.OKTA.name(), AccountType.JUST_AUTH_HANDLER, "Okta"),
    PINTEREST(AuthDefaultSource.PINTEREST.name(), AccountType.JUST_AUTH_HANDLER, "Pinterest");

    @Schema(title = "枚举值")
    private final String key;
    @Schema(title = "处理器")
    private final String handler;
    @Schema(title = "文字")
    private final String description;

    private static final Map<String, AccountType> INDEX_MAP = new HashMap<>();
    private static final List<Map<String, Object>> JSON_STRUCT = new ArrayList<>();

    static {
        for (AccountType accountType : AccountType.values()) {
            INDEX_MAP.put(accountType.getKey(), accountType);
            JSON_STRUCT.add(accountType.ordinal(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", accountType.ordinal())
                            .put("key", accountType.name())
                            .put("text", accountType.getDescription())
                            .build());
        }
    }

    /**
     * Just Auth 第三方社交登录
     */
    public static final String JUST_AUTH_HANDLER = "JUST_AUTH";

    /**
     * 手机号验证码登录
     */
    public static final String PHONE_NUMBER_HANDLER = "PHONE_NUMBER";

    /**
     * 手机号验证码登录
     */
    public static final String WECHAT_MINI_APP_HANDLER = "WECHAT_MINI_APP";

    AccountType(String key, String handler, String description) {
        this.key = key;
        this.handler = handler;
        this.description = description;
    }

    /**
     * 不加@JsonValue，转换的时候转换出完整的对象。
     * 加了@JsonValue，只会显示相应的属性的值
     * <p>
     * 不使用@JsonValue @JsonDeserializer类里面要做相应的处理
     *
     * @return Enum枚举值
     */
    @JsonValue
    public String getKey() {
        return key;
    }

    public String getDescription() {
        return this.description;
    }

    public String getHandler() {
        return handler;
    }

    public static AccountType getAccountType(String key) {
        return INDEX_MAP.get(key);
    }

    public static List<Map<String, Object>> getJsonStruct() {
        return JSON_STRUCT;
    }
}
