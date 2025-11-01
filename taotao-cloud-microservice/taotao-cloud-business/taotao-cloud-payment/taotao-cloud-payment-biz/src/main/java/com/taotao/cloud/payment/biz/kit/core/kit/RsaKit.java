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

package com.taotao.cloud.payment.biz.kit.core.kit;


import com.taotao.boot.common.utils.log.LogUtils;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;

/** RSA 非对称加密工具类 */
public class RsaKit {

    /** RSA最大加密明文大小 */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** RSA最大解密密文大小 */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /** 加密算法RSA */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 生成公钥和私钥
     *
     * @throws Exception 异常信息
     */
    public static Map<String, String> getKeys() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String publicKeyStr = getPublicKeyStr(publicKey);
        String privateKeyStr = getPrivateKeyStr(privateKey);

        Map<String, String> map = new HashMap<String, String>(2);
        map.put("publicKey", publicKeyStr);
        map.put("privateKey", privateKeyStr);

        LogUtils.info("公钥\r\n" + publicKeyStr);
        LogUtils.info("私钥\r\n" + privateKeyStr);
        return map;
    }

    /**
     * 使用模和指数生成RSA公钥 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus 模
     * @param exponent 公钥指数
     * @return {@link RSAPublicKey}
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            LogUtils.error("使用模和指数生成RSA公钥错误", e);
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data 需要加密的数据
     * @param publicKey 公钥
     * @return 加密后的数据
     * @throws Exception 异常信息
     */
    public static String encryptByPublicKey(String data, String publicKey) throws Exception {
        return encryptByPublicKey(data, publicKey, "RSA/ECB/PKCS1Padding");
    }

    /**
     * 公钥加密
     *
     * @param data 需要加密的数据
     * @param publicKey 公钥
     * @return 加密后的数据
     * @throws Exception 异常信息
     */
    public static String encryptByPublicKeyByWx(String data, String publicKey) throws Exception {
        return encryptByPublicKey(data, publicKey, "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
    }

    /**
     * 公钥加密
     *
     * @param data 需要加密的数据
     * @param publicKey 公钥
     * @param fillMode 填充模式
     * @return 加密后的数据
     * @throws Exception 异常信息
     */
    public static String encryptByPublicKey(String data, String publicKey, String fillMode) throws Exception {
        byte[] dataByte = data.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key key = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(fillMode);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = dataByte.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataByte, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return StrUtil.str(Base64.encode(encryptedData));
    }

    /**
     * 私钥签名
     *
     * @param data 需要加密的数据
     * @param privateKey 私钥
     * @return 加密后的数据
     * @throws Exception 异常信息
     */
    public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
        PKCS8EncodedKeySpec priPkcs8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(priPkcs8);
        Signature signature = Signature.getInstance("SHA256WithRSA");

        signature.initSign(priKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signed = signature.sign();
        return StrUtil.str(Base64.encode(signed));
    }

    /**
     * 私钥签名
     *
     * @param data 需要加密的数据
     * @param privateKey 私钥
     * @return 加密后的数据
     * @throws Exception 异常信息
     */
    public static String encryptByPrivateKey(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signed = signature.sign();
        return StrUtil.str(Base64.encode(signed));
    }

    /**
     * 公钥验证签名
     *
     * @param data 需要加密的数据
     * @param sign 签名
     * @param publicKey 公钥
     * @return 验证结果
     * @throws Exception 异常信息
     */
    public static boolean checkByPublicKey(String data, String sign, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        byte[] encodedKey = Base64.decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initVerify(pubKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.decode(sign.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 公钥验证签名
     *
     * @param data 需要加密的数据
     * @param sign 签名
     * @param publicKey 公钥
     * @return 验证结果
     * @throws Exception 异常信息
     */
    public static boolean checkByPublicKey(String data, String sign, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.decode(sign.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 私钥解密
     *
     * @param data 需要解密的数据
     * @param privateKey 私钥
     * @return 解密后的数据
     * @throws Exception 异常信息
     */
    public static String decryptByPrivateKey(String data, String privateKey) throws Exception {
        return decryptByPrivateKey(data, privateKey, "RSA/ECB/PKCS1Padding");
    }

    /**
     * 私钥解密
     *
     * @param data 需要解密的数据
     * @param privateKey 私钥
     * @return 解密后的数据
     * @throws Exception 异常信息
     */
    public static String decryptByPrivateKeyByWx(String data, String privateKey) throws Exception {
        return decryptByPrivateKey(data, privateKey, "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
    }

    /**
     * 私钥解密
     *
     * @param data 需要解密的数据
     * @param privateKey 私钥
     * @param fillMode 填充模式
     * @return 解密后的数据
     * @throws Exception 异常信息
     */
    public static String decryptByPrivateKey(String data, String privateKey, String fillMode) throws Exception {
        byte[] encryptedData = Base64.decode(data);
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key key = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(fillMode);

        cipher.init(Cipher.DECRYPT_MODE, key);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 异常信息
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从字符串中加载私钥<br>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr 私钥
     * @return {@link PrivateKey}
     * @throws Exception 异常信息
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    public static String getPrivateKeyStr(PrivateKey privateKey) {
        return Base64.encode(privateKey.getEncoded());
    }

    public static String getPublicKeyStr(PublicKey publicKey) {
        return Base64.encode(publicKey.getEncoded());
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> keys = getKeys();
        String publicKey = keys.get("publicKey");
        String privateKey = keys.get("privateKey");
        String content = "我是,I am ";
        String encrypt = encryptByPublicKey(content, publicKey);
        String decrypt = decryptByPrivateKey(encrypt, privateKey);
        LogUtils.info("加密之后：" + encrypt);
        LogUtils.info("解密之后：" + decrypt);

        LogUtils.info("======华丽的分割线=========");

        content = "我是,I am ";
        encrypt = encryptByPublicKeyByWx(content, publicKey);
        decrypt = decryptByPrivateKeyByWx(encrypt, privateKey);
        LogUtils.info("加密之后：" + encrypt);
        LogUtils.info("解密之后：" + decrypt);

        // OPPO
        String sign = encryptByPrivateKey(content, privateKey);
        LogUtils.info("加密之后：" + sign);
        LogUtils.info(checkByPublicKey(content, sign, publicKey));
    }
}
