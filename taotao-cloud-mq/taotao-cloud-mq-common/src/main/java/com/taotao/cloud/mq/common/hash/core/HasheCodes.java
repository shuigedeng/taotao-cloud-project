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

import com.taotao.boot.common.support.instance.impl.Instances;
import com.taotao.cloud.mq.common.hash.api.IHashCode;

public final class HasheCodes {
    private HasheCodes() {}

    public static IHashCode crc() {
        return (IHashCode) Instances.singleton(HashCodeCRC.class);
    }

    public static IHashCode fnv() {
        return (IHashCode) Instances.singleton(HashCodeFnv.class);
    }

    public static IHashCode jdk() {
        return (IHashCode) Instances.singleton(HashCodeJdk.class);
    }

    public static IHashCode ketama() {
        return (IHashCode) Instances.singleton(HashCodeKetama.class);
    }

    public static IHashCode murmur() {
        return (IHashCode) Instances.singleton(HashCodeMurmur.class);
    }
}
