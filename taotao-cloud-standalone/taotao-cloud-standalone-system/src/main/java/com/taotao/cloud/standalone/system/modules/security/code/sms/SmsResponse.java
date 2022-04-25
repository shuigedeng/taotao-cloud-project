package com.taotao.cloud.standalone.system.modules.security.code.sms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Classname SmsResponse
 * @Description TODO
 * @Author shuigedeng
 * @since 2019-08-25 18:42
 * 
 */
@Setter
@Getter
@ToString
public class SmsResponse {

    private String smsPhone;
    private String smsTime;
    private String smsCode;


}
