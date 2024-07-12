package com.taotao.cloud.media.biz.signature;

import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;

//防篡改机制
//除了在图像中添加独特的标识，还有一种防篡改的机制：数字签名。数字签名是一种基于非对称加密算法（如RSA）的方法，通过将电子印章的哈希值加密生成签名。任何改动都会导致签名验证失败。
//
//数字签名流程
//生成哈希值：对原始印章图像生成哈希值。
//
//使用私钥加密：使用预先生成的私钥加密哈希值，生成签名。
//
//验证签名：在验证过程中，使用公钥解密签名并与重新计算的哈希值比对。
public class DigitalSignatureService {

    private final KeyPair keyPair;

    public DigitalSignatureService() throws NoSuchAlgorithmException {
        // 生成公钥和私钥
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        keyPair = keyGen.genKeyPair();
    }

    // 签名图像数据
    public String signData(byte[] data) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(data);
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);
    }

    // 验证签名
    public boolean verifyData(byte[] data, String signedData) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(keyPair.getPublic());
        signature.update(data);
        byte[] signedBytes = Base64.getDecoder().decode(signedData);
        return signature.verify(signedBytes);
    }
}
