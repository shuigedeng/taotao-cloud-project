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
import java.nio.charset.Charset;
import java.util.Arrays;

public class HashContext implements IHashContext {
    private byte[] salt;
    private int times;
    private Charset charset;

    public static HashContext newInstance() {
        return new HashContext();
    }

    public byte[] salt() {
        return this.salt;
    }

    public HashContext salt(byte[] salt) {
        this.salt = salt;
        return this;
    }

    public int times() {
        return this.times;
    }

    public HashContext times(int times) {
        this.times = times;
        return this;
    }

    public Charset charset() {
        return this.charset;
    }

    public HashContext charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public String toString() {
        return "HashContext{salt="
                + Arrays.toString(this.salt)
                + ", times="
                + this.times
                + ", charset="
                + this.charset
                + '}';
    }
}
