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
package com.taotao.cloud.web.crypto;

/**
 * <p>Description: 加密算法策略 </p>
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-30 11:29:43
 */
public enum CryptoStrategy {

    /**
     * 国密加密算法
     */
    SM,
    /**
     * 传统加密算法，RSA AES 等
     */
    STANDARD;
}
