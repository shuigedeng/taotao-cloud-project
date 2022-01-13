package com.taotao.cloud.web.sign.util.security.sm;

import cn.hutool.crypto.SmUtil;
import com.taotao.cloud.web.sign.properties.EncryptBodyProperties;
import com.taotao.cloud.web.sign.util.CheckUtils;
import com.taotao.cloud.web.sign.util.ISecurity;

/**
 * sm4 加密解密工具类
 *
 * @since 2021年3月15日18:28:42
 */
public class Sm4Util implements ISecurity {


    /**
     * 加密
     *
     * @param content  内容
     * @param password 注解中传入的key 可为null或空字符
     * @param config   yml配置类
     * @return String
     */
    @Override
    public String encrypt(String content, String password, EncryptBodyProperties config) {

        String key = CheckUtils.checkAndGetKey(config.getSm4Key(), password, "RSA-KEY加密");
        return SmUtil.sm4(key.getBytes()).encryptHex(content);

    }

    /**
     * 解密
     *
     * @param content  内容
     * @param password 注解中传入的key 可为null或空字符
     * @param config   yml配置类
     * @return String
     */
    @Override
    public String decrypt(String content, String password, EncryptBodyProperties config) {

        String key = CheckUtils.checkAndGetKey(config.getSm4Key(), password, "SM4-KEY解密");
        return SmUtil.sm4(key.getBytes()).decryptStr(content);

    }
}
