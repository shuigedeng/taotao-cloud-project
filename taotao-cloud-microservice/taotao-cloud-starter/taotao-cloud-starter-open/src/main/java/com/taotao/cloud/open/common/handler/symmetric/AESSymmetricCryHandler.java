package com.taotao.cloud.open.common.handler.symmetric;

import cn.hutool.crypto.symmetric.AES;
import com.taotao.cloud.open.common.handler.SymmetricCryHandler;

/**
 * AES对称加密处理器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:06:44
 */
public class AESSymmetricCryHandler implements SymmetricCryHandler {
    @Override
    public String cry(String content, byte[] keyBytes) {
        AES aes = new AES(keyBytes);
        return aes.encryptBase64(content);
    }

    @Override
    public byte[] cry(byte[] content, byte[] keyBytes) {
        AES aes = new AES(keyBytes);
        return aes.encrypt(content);
    }

    @Override
    public String deCry(String content, byte[] keyBytes) {
        AES aes = new AES(keyBytes);
        return aes.decryptStr(content);
    }

    @Override
    public byte[] deCry(byte[] content, byte[] keyBytes) {
        AES aes = new AES(keyBytes);
        return aes.decrypt(content);
    }
}
