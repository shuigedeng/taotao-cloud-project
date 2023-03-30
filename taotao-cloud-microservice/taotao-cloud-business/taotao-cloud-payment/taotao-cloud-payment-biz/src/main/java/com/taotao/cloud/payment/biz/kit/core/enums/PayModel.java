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
 * <p>商户平台模式
 *
 * @author Javen
 */
public enum PayModel {
    /** 商户模式 */
    BUSINESS_MODEL("BUSINESS_MODEL"),
    /** 服务商模式 */
    SERVICE_MODE("SERVICE_MODE");

    PayModel(String payModel) {
        this.payModel = payModel;
    }

    /** 商户模式 */
    private final String payModel;

    public String getPayModel() {
        return payModel;
    }
}
