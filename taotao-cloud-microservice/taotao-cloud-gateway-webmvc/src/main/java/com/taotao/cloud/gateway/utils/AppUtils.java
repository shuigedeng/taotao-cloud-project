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

package com.taotao.cloud.gateway.utils;

import com.google.common.collect.Maps;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

/**
 * AppUtils
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class AppUtils {

    /**
     * key:appId、value:appSecret
     */
    static Map<String, String> appMap = Maps.newConcurrentMap();

    /**
     * 分别保存生成的公私钥对 key:appId，value:公私钥对
     */
    static Map<String, Map<String, String>> appKeyPair = Maps.newConcurrentMap();

    public static void main( String[] args ) throws Exception {
        // 模拟生成appId、appSecret
        String appId = initAppInfo();

        // 根据appId生成公私钥对
        initKeyPair(appId);

        // 模拟请求方
        String requestParam = clientCall();

        // 模拟提供方验证
        serverVerify(requestParam);
    }

    private static String initAppInfo() {
        // appId、appSecret生成规则，依据之前介绍过的方式，保证全局唯一即可
        String appId = "123456";
        String appSecret = "654321";
        appMap.put(appId, appSecret);
        return appId;
    }

    private static void serverVerify( String requestParam ) throws Exception {
        //        APIRequestEntity apiRequestEntity = JSONObject.parseObject(requestParam,
        // APIRequestEntity.class);
        //        Header header = apiRequestEntity.getHeader();
        //        UserEntity userEntity =
        // JSONObject.parseObject(JSONObject.toJSONString(apiRequestEntity.getBody()),
        // UserEntity.class);
        //
        //        // 首先，拿到参数后同样进行签名
        //        String sign = getSHA256Str(JSONObject.toJSONString(userEntity));
        //        if (!sign.equals(header.getSign())) {
        //            throw new Exception("数据签名错误！");
        //        }
        //
        //        // 从header中获取相关信息，其中appSecret需要自己根据传过来的appId来获取
        //        String appId = header.getAppId();
        //        String appSecret = getAppSecret(appId);
        //        String nonce = header.getNonce();
        //        String timestamp = header.getTimestamp();
        //
        //        // 按照同样的方式生成appSign，然后使用公钥进行验签
        //        Map<String, String> data = Maps.newHashMap();
        //        data.put("appId", appId);
        //        data.put("nonce", nonce);
        //        data.put("sign", sign);
        //        data.put("timestamp", timestamp);
        //        Set<String> keySet = data.keySet();
        //        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //        Arrays.sort(keyArray);
        //        StringBuilder sb = new StringBuilder();
        //        for (String k : keyArray) {
        //            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
        //                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        //        }
        //        sb.append("appSecret=").append(appSecret);
        //
        //
        //        if (!rsaVerifySignature(sb.toString(), appKeyPair.get(appId).get("publicKey"),
        // header.getAppSign())) {
        //            throw new Exception("公钥验签错误！");
        //        }
        //
        //        LogUtils.info();
        //        LogUtils.info("【提供方】验证通过！");

    }

    public static String clientCall() {
        // 假设接口请求方与接口提供方，已经通过其他渠道，确认了双方交互的appId、appSecret
        //        String appId = "123456";
        //        String appSecret = "654321";
        //        String timestamp = String.valueOf(System.currentTimeMillis());
        //        // 应该为随机数，演示随便写一个
        //        String nonce = "1234";
        //
        //        // 业务请求参数
        //        UserEntity userEntity = new UserEntity();
        //        userEntity.setUserId("1");
        //        userEntity.setPhone("13912345678");
        //
        //        // 使用sha256的方式生成签名
        //        String sign = getSHA256Str(JSONObject.toJSONString(userEntity));
        //
        //        Map<String, String> data = Maps.newHashMap();
        //        data.put("appId", appId);
        //        data.put("nonce", nonce);
        //        data.put("sign", sign);
        //        data.put("timestamp", timestamp);
        //        Set<String> keySet = data.keySet();
        //        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //        Arrays.sort(keyArray);
        //        StringBuilder sb = new StringBuilder();
        //        for (String k : keyArray) {
        //            if (!data.get(k).trim().isEmpty()) // 参数值为空，则不参与签名
        //                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        //        }
        //        sb.append("appSecret=").append(appSecret);
        //
        //        LogUtils.info("【请求方】拼接后的参数：" + sb.toString());
        //        LogUtils.info();
        //
        //        // 使用sha256withRSA的方式对header中的内容加签
        //        String appSign = sha256withRSASignature(appKeyPair.get(appId).get("privateKey"),
        // sb.toString());
        //        LogUtils.info("【请求方】appSign：" + appSign);
        //        LogUtils.info();
        //
        //        // 请求参数组装
        //        Header header = Header.builder()
        //                .appId(appId)
        //                .nonce(nonce)
        //                .sign(sign)
        //                .timestamp(timestamp)
        //                .appSign(appSign)
        //                .build();
        //        APIRequestEntity apiRequestEntity = new APIRequestEntity();
        //        apiRequestEntity.setHeader(header);
        //        apiRequestEntity.setBody(userEntity);
        //
        //        String requestParam = JSONObject.toJSONString(apiRequestEntity);
        //        LogUtils.info("【请求方】接口请求参数: " + requestParam);

        return null;
    }

    /**
     * 私钥签名
     */
    public static String sha256withRSASignature( String privateKeyStr, String dataStr ) {
        try {
            byte[] key = Base64.getDecoder().decode(privateKeyStr);
            byte[] data = dataStr.getBytes();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            return new String(Base64.getEncoder().encode(signature.sign()));
        } catch (Exception e) {
            throw new RuntimeException("签名计算出现异常", e);
        }
    }

    /**
     * 公钥验签
     */
    public static boolean rsaVerifySignature( String dataStr, String publicKeyStr, String signStr )
            throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509EncodedKeySpec =
                new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(dataStr.getBytes());
        return signature.verify(Base64.getDecoder().decode(signStr));
    }

    /**
     * 生成公私钥对
     */
    public static void initKeyPair( String appId ) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, String> keyMap = Maps.newHashMap();
        keyMap.put("publicKey", new String(Base64.getEncoder().encode(publicKey.getEncoded())));
        keyMap.put("privateKey", new String(Base64.getEncoder().encode(privateKey.getEncoded())));
        appKeyPair.put(appId, keyMap);
    }

    private static String getAppSecret( String appId ) {
        return String.valueOf(appMap.get(appId));
    }

    @SneakyThrows
    public static String getSHA256Str( String str ) {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }
}
