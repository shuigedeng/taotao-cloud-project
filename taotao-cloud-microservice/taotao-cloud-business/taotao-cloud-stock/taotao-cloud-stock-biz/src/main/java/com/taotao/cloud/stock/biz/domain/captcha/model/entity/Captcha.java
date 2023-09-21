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

package com.taotao.cloud.stock.biz.domain.captcha.model.entity;

import com.taotao.cloud.stock.api.common.domain.Entity;
import com.taotao.cloud.stock.biz.domain.captcha.model.vo.CaptchaCode;
import com.taotao.cloud.stock.biz.domain.captcha.model.vo.Uuid;
import java.util.Date;

/**
 * 验证码
 *
 * @author shuigedeng
 * @since 2021-05-10
 */
public class Captcha implements Entity<Captcha> {

    private com.taotao.cloud.stock.biz.domain.model.captcha.Uuid uuid;
    /** 验证码 */
    private CaptchaCode captchaCode;
    /** 过期时间 */
    private Date expireTime;

    public Captcha(
            com.taotao.cloud.stock.biz.domain.model.captcha.Uuid uuid, CaptchaCode captchaCode, Date expireTime) {
        this.uuid = uuid;
        this.captchaCode = captchaCode;
        this.expireTime = expireTime;
    }

    public static Captcha createCaptcha(
            com.taotao.cloud.stock.biz.domain.model.captcha.Uuid uuid, CaptchaCode captchaCode) {
        return new Captcha(uuid, captchaCode, DateUtil.addDateMinutes(new Date(), 5));
    }

    public boolean validate(CaptchaCode captchaCode) {
        return this.captchaCode.sameValueAs(captchaCode) && this.expireTime.getTime() >= System.currentTimeMillis();
    }

    @Override
    public boolean sameIdentityAs(Captcha other) {
        return false;
    }

    public Uuid getUuid() {
        return uuid;
    }

    public CaptchaCode getCaptchaCode() {
        return captchaCode;
    }

    public Date getExpireTime() {
        return expireTime;
    }
}
