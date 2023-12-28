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

package com.taotao.cloud.im.biz.platform.common.shiro.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/** Md5Util */
public class Md5Utils {

    /**
     * credentials
     *
     * @param password
     * @param salt
     * @return
     */
    public static final String credentials(String password, String salt) {
        // 加密方式
        String hashAlgorithmName = "MD5";
        // 盐：为了即使相同的密码不同的盐加密后的结果也不同
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        // 密码
        Object source = password;
        // 加密次数
        int hashIterations = 1;
        SimpleHash result = new SimpleHash(hashAlgorithmName, source, byteSalt, hashIterations);
        return result.toString();
    }

    /**
     * md5
     *
     * @param str
     * @return
     */
    public static final String md5(String str) {
        return SecureUtil.md5(str);
    }

    /**
     * 加密盐
     *
     * @return
     */
    public static String salt() {
        return RandomUtil.randomString(4);
    }

    /** 基础密码 */
    private static final String baseStr = "abcdefghgkmnprstwxyz123456789";

    /**
     * 密码
     *
     * @return
     */
    public static String password() {
        return RandomUtil.randomString(baseStr, 8);
    }

    public static void main(String[] args) {
        String pass1 = credentials("0192023a7bbd73250516f069df18b500", "hwyl");
        LogUtils.info(pass1);
        String pass2 = SecureUtil.md5("123456");
        LogUtils.info(pass2);
    }
}
