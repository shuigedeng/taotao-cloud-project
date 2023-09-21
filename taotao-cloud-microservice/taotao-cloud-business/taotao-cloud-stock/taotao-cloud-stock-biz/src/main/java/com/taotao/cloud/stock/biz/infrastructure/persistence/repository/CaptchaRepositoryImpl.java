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

package com.taotao.cloud.stock.biz.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 验证码-Repository实现类
 *
 * @author shuigedeng
 * @since 2021-05-10
 */
@Repository
public class CaptchaRepositoryImpl extends ServiceImpl<SysCaptchaMapper, SysCaptchaDO>
        implements CaptchaRepository, IService<SysCaptchaDO> {

    @Override
    public Captcha find(Uuid uuid) {
        SysCaptchaDO sysCaptchaDO = this.getById(uuid.getId());
        return CaptchaConverter.toCaptcha(sysCaptchaDO);
    }

    @Override
    public void store(Captcha captcha) {
        this.save(CaptchaConverter.fromCaptcha(captcha));
    }

    @Override
    public void remove(Uuid uuid) {
        this.removeById(uuid.getId());
    }
}
