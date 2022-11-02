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

package com.taotao.cloud.pay.alipay.alipay.definition;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * <p>Description: 支付宝支付配置信息 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/7 18:08
 */
public class AlipayProfile implements Serializable {

    /**
     * APPID 即创建应用后生成
     *
     * 代调用接口的本质是第三方应用代其他应用调用接口。调用接口规则为“谁调用就传入谁的APPID”。因此，在代调用场景下公共参数 app_id 需要传入 第三方应用 APPID。
     */
    private String appId;
    /**
     * 开发者私钥，由开发者自己生成。
     */
    private String appPrivateKey;
    /**
     * 支付宝公钥，由支付宝生成。(普通公钥模式使用)
     */
    private String alipayPublicKey;
    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2。
     */
    private String signType = "RSA2";
    private String charset = StandardCharsets.UTF_8.toString();
    /**
     * 应用公钥证书文件本地路径。(公钥证书模式使用)
     */
    private String appCertPath;
    /**
     * 支付宝公钥证书文件本地路径。(公钥证书模式使用)
     */
    private String alipayCertPath;
    /**
     * 支付宝根证书文件本地路径。(公钥证书模式使用)
     */
    private String alipayRootCertPath;

    public AlipayProfile() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppPrivateKey() {
        return appPrivateKey;
    }

    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getAppCertPath() {
        return appCertPath;
    }

    public void setAppCertPath(String appCertPath) {
        this.appCertPath = appCertPath;
    }

    public String getAlipayCertPath() {
        return alipayCertPath;
    }

    public void setAlipayCertPath(String alipayCertPath) {
        this.alipayCertPath = alipayCertPath;
    }

    public String getAlipayRootCertPath() {
        return alipayRootCertPath;
    }

    public void setAlipayRootCertPath(String alipayRootCertPath) {
        this.alipayRootCertPath = alipayRootCertPath;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("appId", appId)
                .add("appPrivateKey", appPrivateKey)
                .add("alipayPublicKey", alipayPublicKey)
                .add("signType", signType)
                .add("charset", charset)
                .add("appCertPath", appCertPath)
                .add("alipayCertPath", alipayCertPath)
                .add("alipayRootCertPath", alipayRootCertPath)
                .toString();
    }
}
