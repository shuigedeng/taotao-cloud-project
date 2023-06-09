/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
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
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.management.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>Description: 秘钥缓存存储实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/9/30 18:17
 */
public class SecretKey implements Serializable {

    /**
     * 数据存储身份标识
     */
    private String identity;
    /**
     * 对称加密算法秘钥
     */
    private String symmetricKey;

    /**
     * 服务器端非对称加密算法公钥
     * 1. RSA 为 Base64 格式
     * 2. SM2 为 Hex 格式
     */
    private String publicKey;

    /**
     * 服务器端非对称加密算法私钥
     */
    private String privateKey;

    /**
     * 本系统授权码模式中后台返回的 State
     */
    private String state;

    /**
     * 创建时间戳
     */
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public SecretKey() {
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getSymmetricKey() {
        return symmetricKey;
    }

    public void setSymmetricKey(String symmetricKey) {
        this.symmetricKey = symmetricKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecretKey secretKey = (SecretKey) o;
        return Objects.equal(identity, secretKey.identity) && Objects.equal(timestamp, secretKey.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identity, timestamp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("identity", identity)
                .add("symmetricKey", symmetricKey)
                .add("publicKey", publicKey)
                .add("privateKey", privateKey)
                .add("state", state)
                .add("timestamp", timestamp)
                .toString();
    }
}
