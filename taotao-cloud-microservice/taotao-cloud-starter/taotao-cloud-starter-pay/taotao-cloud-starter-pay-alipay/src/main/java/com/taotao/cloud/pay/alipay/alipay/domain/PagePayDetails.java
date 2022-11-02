/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.pay.alipay.alipay.domain;

import cn.herodotus.engine.assistant.core.definition.domain.AbstractDto;
import cn.hutool.core.lang.RegexPool;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>Description: 电脑网站支付DTO </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/9 13:51
 */
@Schema(name = "电脑网站支付DTO", title = "用于支付宝电脑网站支付请求传递参数")
public class PagePayDetails extends AbstractDto {

    @Schema(name = "标识信息", title = "支付宝配置的标识信息")
    @NotBlank
    private String identity;

    @Schema(name = "交易订单编号", title = "由商家自定义，64个字符以内，仅支持字母、数字、下划线且需保证在商户端不重复。")
    @Size(min = 1, max = 64, message = "64个字符以内")
    @Pattern(regexp = RegexPool.GENERAL, message = "仅支持字母、数字、下划线")
    private String tradeNo;

    @Size(min = 1, max = 256, message = "256个字符以内")
    @Pattern(regexp = RegexPool.GENERAL_WITH_CHINESE, message = "不能包含特殊字符")
    private String subject;

    @Schema(name = "订单附加信息", title = "如果请求时传递了该参数，将在异步通知、对账单中原样返回，同时会在商户和用户的pc账单详情中作为交易描述展示")
    private String body;

    @Schema(name = "订单总金额， ", title = "单位为元，精确到小数点后两位，取值范围为, [0.01,100000000]。金额不能为0")
    @Size(max = 11, message = "11个字符以内")
    private String totalAmount;

    /**
     * PC扫码支付的方式。
     * 支持前置模式和跳转模式。
     * 前置模式是将二维码前置到商户的订单确认页的模式。需要商户在自己的页面中以 iframe 方式请求支付宝页面。具体支持的枚举值有以下几种：
     * 0：订单码-简约前置模式，对应 iframe 宽度不能小于600px，高度不能小于300px；
     * 1：订单码-前置模式，对应iframe 宽度不能小于 300px，高度不能小于600px；
     * 3：订单码-迷你前置模式，对应 iframe 宽度不能小于 75px，高度不能小于75px；
     * 4：订单码-可定义宽度的嵌入式二维码，商户可根据需要设定二维码的大小。
     * <p>
     * 跳转模式下，用户的扫码界面是由支付宝生成的，不在商户的域名下。支持传入的枚举值有：
     * 2：订单码-跳转模式
     */
    @Schema(name = "支付显示模式")
    private String mode = "2";

    @Schema(name = "应用授权Token", title = "系统服务商（ISV）在取得商户授权后，可以代商户调用支付宝开放接口，以完成相应的业务逻辑")
    private String appAuthToken;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("identity", identity)
                .add("tradeNo", tradeNo)
                .add("subject", subject)
                .add("body", body)
                .add("totalAmount", totalAmount)
                .add("mode", mode)
                .add("appAuthToken", appAuthToken)
                .toString();
    }
}
