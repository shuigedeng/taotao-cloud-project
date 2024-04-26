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

package com.taotao.cloud.auth.infrastructure.crypto;

/**
 * <p>对称加密算法 </p>
 *
 *
 * @since : 2022/5/1 15:05
 */
public interface SymmetricCryptoProcessor {

    /**
     * 创建 SM4 Key。可以为 16 进制串或字节数组，要求为 128 比特
     *
     * @return SM4 Key
     */
    String createKey();

    /**
     * 用私钥解密
     *
     * @param key  对称算法 秘钥
     * @param data 待解密数据
     * @return 解密后的数据
     */
    String decrypt(String data, String key);

    /**
     * 用公钥加密
     *
     * @param key  对称算法 秘钥
     * @param data 待加密数据
     * @return 加密后的数据
     */
    String encrypt(String data, String key);
}
