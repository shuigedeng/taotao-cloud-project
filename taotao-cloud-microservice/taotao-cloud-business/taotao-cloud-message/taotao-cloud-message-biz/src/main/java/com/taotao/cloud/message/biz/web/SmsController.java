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

package com.taotao.cloud.message.biz.web;

import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.sms.common.exception.VerificationCodeIsNullException;
import com.taotao.cloud.sms.common.exception.VerifyFailException;
import com.taotao.cloud.sms.common.model.NoticeInfo;
import com.taotao.cloud.sms.common.model.VerifyInfo;
import com.taotao.cloud.sms.common.service.NoticeService;
import com.taotao.cloud.sms.common.service.VerificationCodeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信Controller
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:49:56
 */
@RestController
public class SmsController {

    /** 手机验证码服务 */
    private final VerificationCodeService verificationCodeService;

    /** 短信通知服务 */
    private final NoticeService noticeService;

    public SmsController(VerificationCodeService verificationCodeService, NoticeService noticeService) {
        this.verificationCodeService = verificationCodeService;
        this.noticeService = noticeService;
    }

    /**
     * 获取验证码
     *
     * @param phone 手机号码
     */
    public void sendVerificationCode(@PathVariable("phone") String phone) {
        verificationCodeService.send(phone);
    }

    /**
     * 获取验证码
     *
     * @param phone 手机号码
     * @param identificationCode 识别码
     * @return 验证码信息
     */
    public VerifyInfo getVerificationCode(
            @PathVariable("phone") String phone,
            @RequestParam(value = "identificationCode", required = false, defaultValue = "")
                    String identificationCode) {
        String code = verificationCodeService.find(phone, identificationCode);

        if (StringUtils.isBlank(code)) {
            throw new VerificationCodeIsNullException();
        }

        VerifyInfo info = new VerifyInfo();
        info.setCode(code);
        info.setIdentificationCode(identificationCode);
        info.setPhone(phone);

        return info;
    }

    /**
     * 验证信息
     *
     * @param verifyInfo 验证信息
     */
    public void verifyVerificationCode(@RequestBody VerifyInfo verifyInfo) {
        if (!verificationCodeService.verify(
                verifyInfo.getPhone(), verifyInfo.getCode(), verifyInfo.getIdentificationCode())) {
            throw new VerifyFailException();
        }
    }

    /**
     * 发送通知
     *
     * @param info 通知内容
     */
    public void sendNotice(@RequestBody NoticeInfo info) {
        noticeService.send(info.getNoticeData(), info.getPhones());
    }
}
