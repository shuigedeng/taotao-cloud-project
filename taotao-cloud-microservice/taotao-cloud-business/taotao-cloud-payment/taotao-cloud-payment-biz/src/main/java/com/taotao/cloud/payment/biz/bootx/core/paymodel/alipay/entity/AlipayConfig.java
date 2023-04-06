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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.convert.AlipayConvert;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.alipay.AlipayConfigDto;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.alipay.AlipayConfigParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝支付配置
 *
 * @author xxm
 * @date 2020/12/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alipay_config")
public class AlipayConfig extends MpBaseEntity implements EntityBaseFunction<AlipayConfigDto> {

    /** 名称 */
    private String name;

    /** 支付宝商户appId */
    private String appId;

    /** 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 */
    private String notifyUrl;

    /** 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址 */
    private String returnUrl;

    /** 请求网关地址 */
    private String serverUrl;

    /** 认证类型 证书/公钥 */
    private Integer authType;

    /** 签名类型 */
    public String signType;

    /** 支付宝公钥 */
    @BigField
    public String alipayPublicKey;

    /** 私钥 */
    @BigField
    @EncryptionField
    private String privateKey;

    /** 应用公钥证书 */
    @BigField
    @EncryptionField
    private String appCert;

    /** 支付宝公钥证书 */
    @BigField
    @EncryptionField
    private String alipayCert;

    /** 支付宝CA根证书 */
    @BigField
    @EncryptionField
    private String alipayRootCert;

    /** 是否沙箱环境 */
    private boolean sandbox;

    /** 超时配置 */
    private String expireTime;

    /** 可用支付方式 */
    private String payWays;

    /** 是否启用 */
    private Boolean activity;

    /** 状态 */
    private Integer state;

    /** 备注 */
    private String remark;

    @Override
    public AlipayConfigDto toDto() {
        AlipayConfigDto convert = AlipayConvert.CONVERT.convert(this);
        if (StrUtil.isNotBlank(this.getPayWays())) {
            convert.setPayWayList(StrUtil.split(this.getPayWays(), ','));
        }
        return convert;
    }

    public static AlipayConfig init(AlipayConfigParam in) {
        AlipayConfig convert = AlipayConvert.CONVERT.convert(in);
        if (CollUtil.isNotEmpty(in.getPayWayList())) {
            convert.setPayWays(String.join(",", in.getPayWayList()));
        }
        return convert;
    }
}
