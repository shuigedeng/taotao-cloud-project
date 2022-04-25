package com.taotao.cloud.standalone.system.modules.security.code.sms;

import java.util.Map;

/**
 * @Classname SmsCodeService
 * @Description 短信服务
 * @Author shuigedeng
 * @since 2019-07-08 11:03
 * 
 */
public interface SmsCodeService {

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    Map<String, Object> sendCode(String phone);
}
