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

import com.taotao.cloud.mq.common.hash.api.IHash;

public final class Hashes {
    private Hashes() {}

    public static IHash md2() {
        return new Md2Hash();
    }

    public static IHash md5() {
        return new Md5Hash();
    }

    public static IHash sha1() {
        return new Sha1Hash();
    }

    public static IHash sha256() {
        return new Sha256Hash();
    }

    public static IHash sha384() {
        return new Sha384Hash();
    }

    public static IHash sha512() {
        return new Sha512Hash();
    }

    public static IHash empty() {
        return new EmptyHash();
    }
}
