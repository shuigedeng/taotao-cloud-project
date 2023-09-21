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

package com.taotao.cloud.stock.biz.domain.captcha.repository;

import com.taotao.cloud.stock.biz.domain.captcha.model.entity.Captcha;
import com.taotao.cloud.stock.biz.domain.captcha.model.vo.Uuid;
import com.taotao.cloud.stock.biz.domain.model.captcha.Captcha;
import com.taotao.cloud.stock.biz.domain.model.captcha.Uuid;

/**
 * 验证码Repository
 *
 * @author shuigedeng
 * @since 2021-05-10
 */
public interface CaptchaRepository {

    /**
     * 获取编码
     *
     * @param uuid
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.captcha.Captcha find(
            com.taotao.cloud.stock.biz.domain.model.captcha.Uuid uuid);

    /**
     * 保存
     *
     * @param captcha
     */
    void store(Captcha captcha);

    /**
     * 删除
     *
     * @param uuid
     */
    void remove(Uuid uuid);
}
