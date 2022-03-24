/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.ws.netty;

import java.util.concurrent.locks.ReentrantLock;

/**
 * NettyByteAndStringUtils
 *
 * @author shuigedeng
 * @since 2020/12/30 下午4:43
 * @version 2022.03
 */
public class NettyByteAndStringUtils {

    private final static ReentrantLock LOCK = new ReentrantLock();

    /**
     * byte数组转hex
     */
    public static String byteToHex(byte[] bytes) {
        final ReentrantLock putLock = LOCK;
        putLock.lock();
        try {
            String strHex = "";
            StringBuilder sb = new StringBuilder("");
            for (byte aByte : bytes) {
                strHex = Integer.toHexString(aByte & 0xFF);
                // 每个字节由两个字符表示，位数不够，高位补0
                sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
            }
            return sb.toString().trim();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * hex转byte数组
     */
    public static byte[] hexToByte(String hex) {
        final ReentrantLock putLock = LOCK;
        putLock.lock();
        try {
            int m = 0, n = 0;
            // 每两个字符描述一个字节
            int byteLen = hex.length() / 2;
            byte[] ret = new byte[byteLen];
            for (int i = 0; i < byteLen; i++) {
                m = i * 2 + 1;
                n = m + 1;
                int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
                ret[i] = (byte) intVal;
            }
            return ret;
        } finally {
            putLock.unlock();
        }
    }

    /**
     * 字节数组的大小
     */
    public static int getByteSize(byte[] bytes) {
        final ReentrantLock putLock = LOCK;
        putLock.lock();
        try {
            int size = 0;
            if (bytes == null || bytes.length == 0) {
                return 0;
            }
            for (byte aByte : bytes) {
                if (aByte == '\0') {
                    break;
                }
                size++;
            }
            return size;
        } finally {
            putLock.unlock();
        }
    }
}
