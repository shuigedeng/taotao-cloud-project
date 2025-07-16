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

import com.taotao.cloud.mq.common.hash.api.IRandomGenerator;
import java.security.SecureRandom;

public class SecureRandomGenerator implements IRandomGenerator {
    protected static final int DEFAULT_NEXT_BYTES_SIZE = 16;
    private int defaultNextBytesSize = 16;
    private SecureRandom secureRandom = new SecureRandom();

    public byte[] nextBytes() {
        return this.nextBytes(this.defaultNextBytesSize);
    }

    public byte[] nextBytes(int numBytes) {
        if (numBytes <= 0) {
            throw new IllegalArgumentException(
                    "numBytes argument must be a positive integer (1 or larger)");
        } else {
            byte[] bytes = new byte[numBytes];
            this.secureRandom.nextBytes(bytes);
            return bytes;
        }
    }
}
