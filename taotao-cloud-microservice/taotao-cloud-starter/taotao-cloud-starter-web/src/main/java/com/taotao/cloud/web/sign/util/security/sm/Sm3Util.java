package com.taotao.cloud.web.sign.util.security.sm;

import cn.hutool.crypto.SmUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.web.sign.properties.EncryptBodyProperties;
import com.taotao.cloud.web.sign.exception.EncryptDtguaiException;
import com.taotao.cloud.web.sign.util.ISecurity;

/**
 * sm3 加密解密工具类
 *
 * @since 2021年3月15日18:28:42
 */
public class Sm3Util implements ISecurity {


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
        return SmUtil.sm3(content);
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
        LogUtil.error("SM3消息摘要加密,可以用MD5作为对比理解,无法解密");
        throw new EncryptDtguaiException("SM3消息摘要加密,可以用MD5作为对比理解,无法解密");
    }
}
