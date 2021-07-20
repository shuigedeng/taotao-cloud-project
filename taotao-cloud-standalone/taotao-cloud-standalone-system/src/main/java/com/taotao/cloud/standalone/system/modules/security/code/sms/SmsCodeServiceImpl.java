package com.taotao.cloud.standalone.system.modules.security.code.sms;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Classname SmsCodeServiceImpl
 * @Description 短信服务实现层
 * @Author shuigedeng
 * @since 2019-07-08 11:04
 * @Version 1.0
 */
@Service
public class SmsCodeServiceImpl implements SmsCodeService {

    /**
     * 随机数
     */
    private static int randNum = (int) ((Math.random() * 9 + 1) * 100000);
    /**
     * 登录短信
     */
    private final static String LOGIN_TEMPLATE = "【小东科技】登录验证码：" + randNum + "，如非本人操作，请忽略此短信。";


    @Override
    public Map<String, Object> sendCode(String phone) {
        return SendMsg.sendSMSG(phone, LOGIN_TEMPLATE, randNum);
    }
}
