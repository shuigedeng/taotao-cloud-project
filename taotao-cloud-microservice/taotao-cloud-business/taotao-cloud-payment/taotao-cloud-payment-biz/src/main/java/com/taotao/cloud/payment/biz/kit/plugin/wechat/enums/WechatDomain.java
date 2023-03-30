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

package com.taotao.cloud.payment.biz.kit.plugin.wechat.enums;

/** 微信支付域名 */
public enum WechatDomain {
    /** 中国国内 */
    CHINA("https://api.mch.weixin.qq.com"),
    /** 中国国内(备用域名) */
    CHINA2("https://api2.mch.weixin.qq.com"),
    /** 东南亚 */
    HK("https://apihk.mch.weixin.qq.com"),
    /** 其它 */
    US("https://apius.mch.weixin.qq.com"),
    /** 获取公钥 */
    FRAUD("https://fraud.mch.weixin.qq.com"),
    /** 活动 */
    ACTION("https://action.weixin.qq.com"),
    /** 刷脸支付 PAY_APP */
    PAY_APP("https://payapp.weixin.qq.com");

    /** 域名 */
    private final String domain;

    WechatDomain(String domain) {
        this.domain = domain;
    }

    public String getType() {
        return domain;
    }

    @Override
    public String toString() {
        return domain;
    }
}
