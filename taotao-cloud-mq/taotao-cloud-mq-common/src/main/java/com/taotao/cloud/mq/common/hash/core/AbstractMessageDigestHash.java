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

package com.taotao.cloud.mq.common.hash.core;

import com.taotao.cloud.mq.common.hash.api.IHashContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AbstractMessageDigestHash extends AbstractHash {
    protected abstract String algorithmName();

    public byte[] doHash(byte[] source, IHashContext context) {
        String algorithmName = this.algorithmName();
        return this.innerHash(source, context.salt(), context.times(), algorithmName);
    }

    protected MessageDigest getDigest(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            String msg =
                    "No native '"
                            + algorithmName
                            + "' MessageDigest instance available on the current JVM.";
            throw new HashRuntimeException(msg, e);
        }
    }

    protected byte[] innerHash(
            byte[] bytes, byte[] salt, int hashIterations, String algorithmName) {
        MessageDigest digest = this.getDigest(algorithmName);
        if (salt != null) {
            digest.reset();
            digest.update(salt);
        }

        byte[] hashed = digest.digest(bytes);
        int times = hashIterations - 1;

        for (int i = 0; i < times; ++i) {
            digest.reset();
            hashed = digest.digest(hashed);
        }

        return hashed;
    }
}
