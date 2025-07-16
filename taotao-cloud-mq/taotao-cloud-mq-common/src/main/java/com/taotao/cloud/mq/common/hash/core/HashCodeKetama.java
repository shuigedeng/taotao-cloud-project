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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCodeKetama extends AbstractHashCode {
    private static MessageDigest md5Digest;

    public int doHash(String origin) {
        byte[] bKey = computeMd5(origin);
        long rv =
                (long) (bKey[3] & 255) << 24
                        | (long) (bKey[2] & 255) << 16
                        | (long) (bKey[1] & 255) << 8
                        | (long) (bKey[0] & 255);
        return (int) (rv & 4294967295L);
    }

    private static byte[] computeMd5(String k) {
        MessageDigest md5;
        try {
            md5 = (MessageDigest) md5Digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        }

        md5.update(k.getBytes());
        return md5.digest();
    }

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }
}
