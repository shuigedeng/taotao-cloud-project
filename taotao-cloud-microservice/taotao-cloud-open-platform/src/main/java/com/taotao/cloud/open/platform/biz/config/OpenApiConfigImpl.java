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

package com.taotao.cloud.open.platform.biz.config;

import com.taotao.cloud.openapi.common.enums.AsymmetricCryEnum;
import com.taotao.cloud.openapi.common.enums.CryModeEnum;
import com.taotao.cloud.openapi.common.enums.SymmetricCryEnum;
import com.taotao.cloud.openapi.server.config.OpenApiServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * openapi的配置信息
 */
@Component
public class OpenApiConfigImpl implements OpenApiServerConfig {

    @Value("${keys.local.rsa.privateKey}")
    private String privateKey;

    @Value("${keys.remote.rsa.publicKey}")
    private String callerPublicKey;

    @Override
    public AsymmetricCryEnum getAsymmetricCry() {
        // 设置非对称加密算法
        return AsymmetricCryEnum.RSA;
    }

    @Override
    public String getCallerPublicKey(String callerId) {
        // TODO 根据调用者ID查找调用者的公钥（可以将所有调用者的公钥存到数据库中）
        return callerPublicKey;
    }

    @Override
    public String getSelfPrivateKey() {
        // 设置服务端私钥
        return privateKey;
    }

    @Override
    public boolean retEncrypt() {
        // 设置返回值是否需要加密
        return true;
    }

    @Override
    public CryModeEnum getCryMode() {
        // 设置加密模式
        return CryModeEnum.SYMMETRIC_CRY;
    }

    @Override
    public SymmetricCryEnum getSymmetricCry() {
        // 设置对称加密算法
        return SymmetricCryEnum.AES;
    }

    @Override
    public boolean enableDoc() {
        // 是否启用接口文档功能
        return true;
    }

    @Override
    public boolean enableCompress() {
        // HTTP传输内容不启用压缩
        return false;
    }
}
