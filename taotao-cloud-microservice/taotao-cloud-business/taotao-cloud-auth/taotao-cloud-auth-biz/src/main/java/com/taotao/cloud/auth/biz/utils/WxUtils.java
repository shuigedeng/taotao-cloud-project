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

package com.taotao.cloud.auth.biz.utils;

import com.taotao.boot.common.utils.log.LogUtils;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * WxUtils
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-11-09 10:44
 */
public class WxUtils {

    public static String decrypt(String sessionKey, String iv, String encryptData) {

        String decryptString = "";
        // 解码经过 base64 编码的字符串
        byte[] sessionKeyByte = Base64.getDecoder().decode(sessionKey);
        byte[] ivByte = Base64.getDecoder().decode(iv);
        byte[] encryptDataByte = Base64.getDecoder().decode(encryptData);

        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            // 得到密钥
            Key key = new SecretKeySpec(sessionKeyByte, "AES");
            // AES 加密算法
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("AES");
            algorithmParameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameters);
            byte[] bytes = cipher.doFinal(encryptDataByte);
            decryptString = new String(bytes);
        } catch (Exception e) {
            LogUtils.error(e);
        }

        return decryptString;
    }
}
