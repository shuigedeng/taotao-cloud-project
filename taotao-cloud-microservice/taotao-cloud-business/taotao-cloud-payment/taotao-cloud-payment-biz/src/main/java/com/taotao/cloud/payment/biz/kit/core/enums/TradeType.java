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

package com.taotao.cloud.payment.biz.kit.core.enums;

/**
 * IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。
 *
 * <p>IJPay 交流群: 723992875
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX
 *
 * <p>支付方式
 *
 * @author Javen
 */
public enum TradeType {
    /** 微信公众号支付或者小程序支付 */
    JSAPI("JSAPI"),
    /** 微信扫码支付 */
    NATIVE("NATIVE"),
    /** 微信APP支付 */
    APP("APP"),
    /** 付款码支付 */
    MICROPAY("MICROPAY"),
    /** H5支付 */
    MWEB("MWEB");

    TradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    /** 交易类型 */
    private final String tradeType;

    public String getTradeType() {
        return tradeType;
    }
}
