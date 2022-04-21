package com.taotao.cloud.message.biz.austin.handler.script;



import com.taotao.cloud.message.biz.austin.handler.domain.sms.SmsParam;
import com.taotao.cloud.message.biz.austin.support.domain.SmsRecord;
import java.util.List;


/**
 * 短信脚本 接口
 * 
 */
public interface SmsScript {

    /**
     * 发送短信
     * @param smsParam
     * @return 渠道商接口返回值
     * @throws Exception
     */
    List<SmsRecord> send(SmsParam smsParam) throws Exception;

}
