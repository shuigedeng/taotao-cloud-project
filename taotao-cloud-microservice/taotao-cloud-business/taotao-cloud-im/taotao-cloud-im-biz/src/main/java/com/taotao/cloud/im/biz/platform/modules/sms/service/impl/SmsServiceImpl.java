/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.im.biz.platform.modules.sms.service.impl;

import com.platform.common.config.PlatformConfig;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.utils.redis.RedisUtils;
import com.platform.modules.sms.enums.SmsTemplateEnum;
import com.platform.modules.sms.enums.SmsTypeEnum;
import com.platform.modules.sms.service.SmsService;
import com.platform.modules.sms.vo.SmsVo;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** token 服务层 */
@Service("smsService")
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Dict sendSms(SmsVo smsVo) {
        // 验证手机号
        if (!Validator.isMobile(smsVo.getPhone())) {
            throw new BaseException("请输入正确的手机号");
        }
        SmsTypeEnum smsType = smsVo.getType();
        String key = smsType.getPrefix().concat(smsVo.getPhone());
        // 生成验证码
        String code = String.valueOf(RandomUtil.randomInt(1000, 9999));
        // 发送短信
        if (YesOrNoEnum.YES.equals(PlatformConfig.SMS)) {
            Dict dict = Dict.create().set("code", code);
            doSendSms(smsVo.getPhone(), SmsTemplateEnum.VERIFY_CODE, dict);
        }
        // 存入缓存
        redisUtils.set(key, code, smsType.getTimeout(), TimeUnit.MINUTES);
        return Dict.create().set("code", code).set("expiration", smsType.getTimeout());
    }

    @Override
    public void verifySms(String phone, String code, SmsTypeEnum type) {
        // 验证手机号
        if (!Validator.isMobile(phone)) {
            throw new BaseException("请输入正确的手机号");
        }
        String key = type.getPrefix().concat(phone);
        if (!redisUtils.hasKey(key)) {
            throw new BaseException("验证码已过期，请重新获取");
        }
        String value = redisUtils.get(key);
        if (value.equalsIgnoreCase(code)) {
            redisUtils.delete(key);
        } else {
            throw new BaseException("验证码不正确，请重新获取");
        }
    }

    /**
     * 执行发送短信
     *
     * @param phone
     * @param templateCode
     * @param dict
     */
    private void doSendSms(String phone, SmsTemplateEnum templateCode, Dict dict) {
        // 短信待集成
    }
}
