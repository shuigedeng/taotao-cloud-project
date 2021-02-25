/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.auth.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.auth.biz.service.ISmsCodeService;
import com.taotao.cloud.common.constant.SecurityConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.core.model.SecurityUser;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.security.exception.ValidateCodeException;
import com.taotao.cloud.security.service.IUserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * SmsCodeServiceImpl
 *
 * @author dengtao
 * @date 2020/4/29 16:12
 * @since v1.0
 */
@Service
public class SmsCodeServiceImpl implements ISmsCodeService {

    @Resource
    private RedisRepository redisRepository;
    @Resource
    private IUserDetailsService userService;
//    @Resource
//    private IAliyunSmsMessageService aliyunSmsMessageService;

    @Override
    public void saveImageCode(String phone, String imageCode) {
        redisRepository.setExpire(buildKey(phone), imageCode, SecurityConstant.DEFAULT_IMAGE_EXPIRE);
    }

    /**
     * 发送验证码
     * <p>
     * 1. 先去redis 查询是否 60S内已经发送
     * 2. 未发送： 判断手机号是否存 ? false :产生4位数字  手机号-验证码
     * 3. 发往消息中心 -> 发送信息
     * 4. 保存redis
     *
     * @param phone 手机号
     * @return true、false
     */
    @Override
    public Result<Boolean> sendSmsCode(String phone) {
        Object tempCode = redisRepository.get(buildKey(phone));
        if (tempCode != null) {
            LogUtil.error("用户:{0}验证码未失效{1}", phone, tempCode);
            return Result.failed(false);
        }

        SecurityUser user = userService.loadUserSecurityUser(phone, "fronted", "phone");
        if (user == null) {
            LogUtil.error("根据用户手机号{0}查询用户为空", phone);
            return Result.failed(false);
        }

        String[] phoneNumbers = {phone};
        // 使用阿里短信发送
//        SmsResponse smsResponse = aliyunSmsMessageService.sendSms(phoneNumbers, "惠游重庆", SmsEnum.LOGIN);
//        if (ObjectUtil.isNull(smsResponse)) {
//            return Result.failed("短信发送失败");
//        }
//        String smsCode = smsResponse.getSmsCode();
//        log.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{}", mobile, smsCode);

//        redisRepository.setExpire(buildKey(mobile), smsCode, SecurityConstant.DEFAULT_IMAGE_EXPIRE);
        return Result.succeed(true);
    }


    @Override
    public String getCode(String phone) {
        return (String) redisRepository.get(buildKey(phone));
    }

    @Override
    public void remove(String phone) {
        redisRepository.del(buildKey(phone));
    }

    @Override
    public void validate(String phone, String verifyCode) {
        if (StrUtil.isBlank(phone)) {
            throw new ValidateCodeException("手机参数不能为空");
        }
        String code = this.getCode(phone);

        if (StrUtil.isBlank(verifyCode)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        if (code == null) {
            throw new ValidateCodeException("验证码不存在或已过期");
        }

        if (!StrUtil.equals(code, verifyCode.toLowerCase())) {
            throw new ValidateCodeException("验证码不正确");
        }

        remove(phone);
    }

    private String buildKey(String mobile) {
        return SecurityConstant.DEFAULT_SMS_CODE_KEY + ":" + mobile;
    }
}
