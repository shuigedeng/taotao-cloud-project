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

package com.taotao.cloud.auth.infrastructure.extension.oneClick.mobtech.utils;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {

    public static final String ALGORITHM = "DES";
    public static final String PADDING = "DES/CBC/PKCS5Padding";
    private static final byte[] DEFAULT_INITIALIZATION_VECTOR = ("00000000".getBytes());

    private String padding = PADDING;
    private byte[] initializationVectorBytes = DEFAULT_INITIALIZATION_VECTOR;
    private AlgorithmParameterSpec paramSpec;

    public DES() {
        this.paramSpec = new IvParameterSpec(initializationVectorBytes);
    }

    public DES(String padding) {
        this.padding = padding;
        this.paramSpec = new IvParameterSpec(initializationVectorBytes);
    }

    public DES(String padding, byte[] initializationVectorBytes) {
        this.padding = padding;
        this.initializationVectorBytes = initializationVectorBytes;
        this.paramSpec = new IvParameterSpec(initializationVectorBytes);
    }

    public DES(byte[] initializationVectorBytes) {
        this.initializationVectorBytes = initializationVectorBytes;
        this.paramSpec = new IvParameterSpec(initializationVectorBytes);
    }

    public byte[] encodeBytes(byte[] data, byte[] key) throws Exception {
        Key secretKey = getKey(key);
        Cipher cipher = Cipher.getInstance(padding);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }

    public byte[] decodeBytes(byte[] data, byte[] key) throws Exception {
        Key secretKey = getKey(key);
        Cipher cipher = Cipher.getInstance(padding);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
        return cipher.doFinal(data);
    }

    public String getPadding() {
        return padding;
    }

    public byte[] getInitializationVectorBytes() {
        return initializationVectorBytes;
    }

    private static Key getKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }

    public static byte[] decode(byte[] data, byte[] key) throws Exception {
        return decode(data, key, DEFAULT_INITIALIZATION_VECTOR);
    }

    public static byte[] encode(byte[] data, byte[] key) throws Exception {
        return encode(data, key, DEFAULT_INITIALIZATION_VECTOR);
    }

    public static byte[] decode(byte[] data, byte[] key, byte[] ivBytes) throws Exception {
        DES des = new DES(PADDING, ivBytes);
        return des.decodeBytes(data, key);
    }

    public static byte[] encode(byte[] data, byte[] key, byte[] ivBytes) throws Exception {
        DES des = new DES(PADDING, ivBytes);
        return des.encodeBytes(data, key);
    }
}
