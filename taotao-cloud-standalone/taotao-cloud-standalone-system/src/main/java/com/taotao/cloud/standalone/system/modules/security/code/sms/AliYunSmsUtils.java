package com.taotao.cloud.standalone.system.modules.security.code.sms;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @Classname SmsUtils
 * @Description TODO
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-08-25 17:40
 * @Version 1.0
 */
@Slf4j
public class AliYunSmsUtils {

    private static final String ACCESS_KEY_ID = "LTAI6RRoUOP2Zfxs";
    private static final String ACCESS_KEY_SECRET = "I9BXcxj1nicmMOVJ9tFRqo8thFWaDQ";


    public static void main(String[] args) {
        SmsResponse smsResponse = AliYunSmsUtils.sendSms("17521296869", "prex", "登录");
        if (ObjectUtil.isNotNull(smsResponse)) {
            System.out.println(smsResponse);
        }
    }


    public static SmsResponse sendSms(String phone, String signName, String templateCode) {
        SmsResponse smsResponse = new SmsResponse();
        //初始化ascClient
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        //组装请求对象
        CommonRequest request = new CommonRequest();
        /**
         * 使用post提交
         */
        request.setMethod(MethodType.POST);
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("1454");
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        //待发送的手机号
        request.putQueryParameter("PhoneNumbers", phone);
        //短信签名
        request.putQueryParameter("SignName", signName);

        if ("登录".equals(templateCode)){
            templateCode = "SMS_172887387";
        } else if ("注册".equals(templateCode)){
            templateCode = "SMS_172887416";
        }
        //短信模板ID
        request.putQueryParameter("TemplateCode", templateCode);
        //验证码
        /**
         * 可选:模板中的变量替换JSON串,
         */
        String random = RandomStringUtils.random(6, false, true);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + random + "\"}");
        request.putQueryParameter("OutId", random);

        try {
            CommonResponse response = client.getCommonResponse(request);
            String responseData = response.getData();
            JSONObject jsonObject = JSONObject.parseObject(responseData);
            String code = (String) jsonObject.get("Code");
            if (StrUtil.isNotEmpty(code) && "OK".equals(code)) {
                //请求成功
                log.info(phone + ",发送短信成功");
                smsResponse.setSmsCode(random);
                smsResponse.setSmsPhone(phone);
                smsResponse.setSmsTime(System.nanoTime() + "");
                return smsResponse;
            } else {
                log.error(phone + ",发送短信失败:{}", jsonObject.get("Message"));
            }
        } catch (ClientException e) {
            log.error(phone + ",发送短信失败:{}", e.getMessage());
        }

        return null;
    }

}
