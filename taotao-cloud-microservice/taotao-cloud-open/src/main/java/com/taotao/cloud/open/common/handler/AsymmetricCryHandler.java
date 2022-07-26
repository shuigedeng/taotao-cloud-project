package com.taotao.cloud.open.common.handler;



import com.taotao.cloud.open.common.enums.AsymmetricCryEnum;
import com.taotao.cloud.open.common.handler.asymmetric.RSAAsymmetricCryHandler;
import com.taotao.cloud.open.common.handler.asymmetric.SM2AsymmetricCryHandler;

import java.util.EnumMap;
import java.util.Map;

/**
 * 非对称加密处理器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:09:47
 */
public interface AsymmetricCryHandler {

    /**
     * 定义所有的非对称加密处理器
     */
    Map<AsymmetricCryEnum, AsymmetricCryHandler> handlerMap = new EnumMap<AsymmetricCryEnum, AsymmetricCryHandler>(AsymmetricCryEnum.class) {{
        put(AsymmetricCryEnum.RSA, new RSAAsymmetricCryHandler());
        put(AsymmetricCryEnum.SM2, new SM2AsymmetricCryHandler());
    }};

    /**
     * 加签
     *
     * @param privateKey 私钥
     * @param content    内容
     * @return 签名
     */
    String sign(String privateKey, String content);

    /**
     * 加签
     *
     * @param privateKey 私钥
     * @param content    内容
     * @return 签名
     */
    String sign(String privateKey, byte[] content);


    /**
     * 验签
     *
     * @param publicKey 公钥
     * @param content   加签的内容
     * @param sign      签名
     * @return 是否验签成功
     */
    boolean verifySign(String publicKey, String content, String sign);

    /**
     * 验签
     *
     * @param publicKey 公钥
     * @param content   加签的内容
     * @param sign      签名
     * @return 是否验签成功
     */
    boolean verifySign(String publicKey, byte[] content, String sign);

    /**
     * 非对称加密
     *
     * @param publicKey 公钥
     * @param content   待加密的内容 （普通字符串）
     * @return 加密后的内容（RSA:Base64字符串,SM2:ASCII字符串）
     */
    String cry(String publicKey, String content);

    /**
     * 非对称加密
     *
     * @param publicKey 公钥
     * @param content   待加密的内容 （字节数组）
     * @return 内容密文（字节数组）
     */
    byte[] cry(String publicKey, byte[] content);


    /**
     * 非对称解密
     *
     * @param privateKey 私钥
     * @param content    内容密文（RSA:Base64字符串,SM2:ASCII字符串）
     * @return 解密后的内容（普通字符串）
     */
    String deCry(String privateKey, String content);

    /**
     * 非对称解密
     *
     * @param privateKey 私钥
     * @param content    内容密文（字节数组）
     * @return 内容明文（字节数组）
     */
    byte[] deCry(String privateKey, byte[] content);
}
