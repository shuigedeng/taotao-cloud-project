package com.taotao.cloud.open.common.handler.asymmetric;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.taotao.cloud.open.common.handler.AsymmetricCryHandler;
import com.taotao.cloud.open.common.util.Base64Util;

import java.nio.charset.StandardCharsets;

/**
 * RSA非对称加密处理器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:06:37
 */
public class RSAAsymmetricCryHandler implements AsymmetricCryHandler {
    @Override
    public String sign(String privateKey, String content) {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        return this.sign(privateKey, data);
    }

    @Override
    public String sign(String privateKey, byte[] content) {
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, privateKey, null);
        byte[] signed = sign.sign(content);
        return Base64Util.bytesToBase64(signed);
    }

    @Override
    public boolean verifySign(String publicKey, String content, String sign) {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        return this.verifySign(publicKey, data, sign);
    }

    @Override
    public boolean verifySign(String publicKey, byte[] content, String sign) {
        Sign signObj = SecureUtil.sign(SignAlgorithm.SHA256withRSA, null, publicKey);
        return signObj.verify(content, Base64Util.base64ToBytes(sign));
    }

    @Override
    public String cry(String publicKey, String content) {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        byte[] encrypt = this.cry(publicKey, data);
        return Base64Util.bytesToBase64(encrypt);
    }

    @Override
    public byte[] cry(String publicKey, byte[] content) {
        RSA rsa = new RSA(null, publicKey);
        return rsa.encrypt(content, KeyType.PublicKey);
    }

    @Override
    public String deCry(String privateKey, String content) {
        byte[] dataBytes = Base64Util.base64ToBytes(content);
        byte[] decrypt = this.deCry(privateKey, dataBytes);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] deCry(String privateKey, byte[] content) {
        RSA rsa = new RSA(privateKey, null);
        return rsa.decrypt(content, KeyType.PrivateKey);
    }
}
